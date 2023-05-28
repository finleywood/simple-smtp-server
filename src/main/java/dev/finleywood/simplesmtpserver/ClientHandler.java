package dev.finleywood.simplesmtpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket client;
    private final PrintWriter out;
    private final BufferedReader in;
    private final InetAddress host;
    private final int port;

    private Mail currentMail = null;

    private final Logger logger = new Logger(ClientHandler.class);

    public ClientHandler(Socket client) throws Exception {
        this.client = client;
        this.out = new PrintWriter(this.client.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
        this.host = this.client.getInetAddress();
        this.port = this.client.getPort();
        logger.info("Created client on host " + this.host + ":" + this.port);
    }

    @Override
    public void run() {
        this.sendMessage(Protocol.READY);
        try(client; out; in) {
            try {
                String message;
                while((message = in.readLine()) != null) {
                    logger.info("Message received from client " + this.host + ":" + this.port + " (Message: " + message + ")");
                    processMessage(message);
                }
            } catch (GracefulQuitException e) {
                logger.info("QUIT receieved, closing client " + this.host + ":" + this.port);
                this.client.close();
            }  catch (Exception e) {
                logger.error("Error occurred while processing message from client " + this.host + ":" + this.port + ", error: " + e);
                e.printStackTrace();
            }
        } catch (Exception e) {}
        finally {
            logger.info("Connection to client " + this.host + ":" + this.port + " closed!");
        }
    }

    private void processMessage(String message) throws Exception {
        String[] commandAndArgs = message.split(" ");
        if(message.contains(Protocol.MAIL_FROM) || message.contains(Protocol.RCP_TO)) {
            String[] fromAndEmail = commandAndArgs[1].split(":");
            String emailSubStr = fromAndEmail[1].substring(1,fromAndEmail[1].length()-1);
            commandAndArgs = new String[] {commandAndArgs[0] + " " + fromAndEmail[0] + ":", emailSubStr};
        }
        switch (commandAndArgs[0]) {
            case Protocol.HELO -> this.helo(commandAndArgs);
            case Protocol.MAIL_FROM -> this.mailFrom(commandAndArgs[1]);
            case Protocol.RCP_TO -> this.rcpTo(commandAndArgs[1]);
            case Protocol.DATA -> this.data();
            case Protocol.QUIT -> this.quit();
            default -> {
                logger.info("Invalid command received: " + commandAndArgs[0]);
                this.sendMessage(Protocol.COMMAND_NOT_RECOGNISED);
            }
        }
    }

    private void helo(String[] commandAndArgs) {
        if(this.currentMail == null) {
            this.currentMail = new Mail();
            if(commandAndArgs.length > 1) {
                this.currentMail.setOutgoing(false);
                this.currentMail.setIncomingHostName(commandAndArgs[1]);
            }
            this.sendMessage(Protocol.OK);
        } else {
            this.sendMessage(Protocol.BAD_SEQUENCE_OF_COMMADS);
        }
    }

    private void mailFrom(String from) {
        if(this.currentMail == null) {
            logger.warning("MAIL FROM command received, but no HELO command received yet!");
            this.sendMessage(Protocol.BAD_SEQUENCE_OF_COMMADS);
        } else {
            this.currentMail.setFrom(from);
            this.sendMessage(Protocol.OK);
        }
    }

    private void rcpTo(String to) {
        if(this.currentMail == null) {
            logger.warning("RCP TO command received, but no HELO command received yet!");
            this.sendMessage(Protocol.BAD_SEQUENCE_OF_COMMADS);
        } else {
            this.currentMail.setTo(to);
            this.sendMessage(Protocol.OK);
        }
    }

    private void data() {
        if(this.currentMail == null) {
            logger.warning("DATA command received, but no HELO command received yet!");
            this.sendMessage(Protocol.BAD_SEQUENCE_OF_COMMADS);
        } else {
            this.sendMessage(Protocol.DATA_ACK);
            try {
                String dataLine;
                while(!(dataLine = in.readLine()).equals(".")) {
                    this.currentMail.addMessageLine(dataLine);
                }
                this.sendMessage(Protocol.OK);
            } catch (IOException e) {
                logger.warning("Problem in receiving data, e: " + e);
                e.printStackTrace();
            }
        }
    }

    private void quit() throws GracefulQuitException {
        if(this.currentMail != null) {
            Main.mailStore.add(this.currentMail);
            logger.info("New " + (this.currentMail.isOutgoing() ? "outgoing" : "incoming") + " mail, with UUID " + this.currentMail.getUuid());
            if(!this.currentMail.isOutgoing()) {
                logger.info("Received From Host: " + this.currentMail.getIncomingHostName());
            }
            logger.info("Mail received from " + this.currentMail.getFrom() + ", to " + this.currentMail.getTo());
            logger.info("Message Contents: " + this.currentMail.getMessage());
            this.currentMail.store();
            this.currentMail = null;
        }
        this.sendMessage(Protocol.QUIT_ACK);
        throw new GracefulQuitException("QUIT Received!");
    }

    public void sendMessage(String message) {
        logger.info("Sending message to client " + this.host + ":" + this.port + " (Message: " + message + ")");
        this.out.println(message);
    }

    private static class GracefulQuitException extends Exception {
        public GracefulQuitException(String message) {
            super(message);
        }
    }
}
