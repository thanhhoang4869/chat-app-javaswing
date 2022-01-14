import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {
    JTextArea text = new JTextArea(20, 10);
    JScrollPane log;
    JPanel notify = new JPanel();
    BufferedWriter writer;
    BufferedReader reader;
    HashMap<String,ClientHandler> onl = new HashMap<>();

    public HashMap<String, ClientHandler> getOnline() {
        return onl;
    }

    public static void main(String args[]) {
        new Server();
    }

    public JPanel getNotify(){
        return this.notify;
    }

    public JTextArea getText(){
        return this.text;
    }

    private class CreateClient implements Runnable{
        Socket ss;
        Server log;

        public CreateClient(Socket ss, Server log) {
            this.ss = ss;
            this.log = log;
        }

        @Override
        public void run() {
            try {
                new ClientHandler(ss, log);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void connect() {
        try {
            ServerSocket s = new ServerSocket(3200);

            do {
                text.append("Waiting for a Client...\n");
                Socket ss = s.accept(); //synchronous
                text.append("Talking to client\n");

                Thread t = new Thread(new CreateClient(ss,this));
                t.start();
            } while (true);
        } catch (IOException e) {
            text.append("There are some error\n");
        } finally {
            try {
                writer.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Server() {
        JFrame frame = new JFrame("Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(400, 300));
        frame.setLayout(new BorderLayout());

        log = setJPanel();
        frame.add(log);
        frame.pack();

        frame.setVisible(true);

        connect();
    }

    public JScrollPane setJPanel() {
        text.setEditable(false);
        text.setBorder(new EmptyBorder(4, 4, 4, 4));
        JScrollPane log = new JScrollPane(text);
        log.setBorder(new CompoundBorder(new TitledBorder("Logs"), new EmptyBorder(4, 4, 4, 4)));
        return log;
    }
}
