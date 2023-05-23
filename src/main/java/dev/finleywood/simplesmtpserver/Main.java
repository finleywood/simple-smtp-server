package dev.finleywood.simplesmtpserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Main {

    public static int port;
    public static String hostname;

    private static Logger logger = new Logger(Main.class);

    public static ArrayList<Mail> mailStore = new ArrayList<>();

    public static void main(String[] args) {
        if(args.length == 0) {
            throw new RuntimeException("No arguments provided, a port number is needed!");
        }
        try {
            port = Integer.parseInt(args[1]);
            hostname = args[0];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        logger.info("Starting SMTP server on port " + port + "!");

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
}
