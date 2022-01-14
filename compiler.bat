cd out/production/chat-app
jar -vcfm ../../server.jar ../../../META-INF/manifest-server.mf *.class
jar -vcfm ../../client.jar ../../../META-INF/manifest-client.mf *.class