package dev.finleywood.simplesmtpserver;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {

    public static int port;
    public static String hostname;

    private static Logger logger = new Logger(Main.class);

    public static ArrayList<Mail> mailStore = new ArrayList<>();

    public static ConfigModel smtpConfig;

    public static void main(String[] args) {
        if(args.length == 0) {
            throw new RuntimeException("No arguments provided, a port number is needed!");
        }
        String configFilePath;
        try {
            port = Integer.parseInt(args[1]);
            hostname = args[0];
            configFilePath = args[2];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        logger.info("Starting SMTP server on port " + port + "!");

        if(!(new File("data").exists())) {
            logger.info("Data directory does not exist! Creating Now...");
            boolean created = new File("data").mkdir();
            logger.info((created ? "Created" : "Unable to create") + " data directory!");
        }

        smtpConfig = parseConfigFile(configFilePath);

        acceptConnections();
    }

    private static void acceptConnections() {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(port);
            while(true) {
                Socket client = null;
                try {
                    client = ss.accept();
                    new Thread(new ClientHandler(client)).start();
                } catch (Exception e) {
                    logger.error("Error accepting connection from client, error: " + e);
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            logger.error("Error starting on port " + port + ", error: " + e);
            e.printStackTrace();
        }
    }

    private static ConfigModel parseConfigFile(String configFileLocation) {
        Gson gson = new Gson();
        File file = new File(configFileLocation);
        if(!file.exists()) {
            throw new RuntimeException("Config file " + configFileLocation + " does not exist!");
        }
        String fileContent;
        try {
            fileContent = Files.readString(Paths.get(configFileLocation));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ConfigModel configModel;
        try {
            configModel = gson.fromJson(fileContent, ConfigModel.class);
        } catch (JsonSyntaxException e) {
            throw new RuntimeException("Could not parse config file to expected format, error: " + e);
        }
        return configModel;
    }
}
