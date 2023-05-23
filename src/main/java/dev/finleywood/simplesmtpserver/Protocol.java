package dev.finleywood.simplesmtpserver;

public class Protocol {

    // Commands
    public static final String HELO = "HELO";
    public static final String MAIL_FROM = "MAIL FROM:";
    public static final String RCP_TO = "RCPT TO:";
    public static final String DATA = "DATA";
    public static final String QUIT = "QUIT";

    // Response Codes
    public static final String READY = "220 Ready";
    public static final String OK = "250 OK";
    public static final String DATA_ACK = "354 Send data";
    public static final String QUIT_ACK = "221 Bye";
    public static final String COMMAND_NOT_RECOGNISED = "500 Invalid command";
    public static final String BAD_SEQUENCE_OF_COMMADS = "503 Invalid sequence of commands";
}