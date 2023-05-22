package dev.finleywood.simplesmtpserver;

public class Protocol {

    // Commands
    public static final String HELO = "HELO";
    public static final String MAIL_FROM = "MAIL FROM:";
    public static final String RCP_TO = "RCP TO:";
    public static final String DATA = "DATA";
    public static final String QUIT = "QUIT";

    // Response Codes
    public static final String OK = "250 OK";
    public static final String DATA_ACK = "354";
    public static final String QUIT_ACK = "221";
    public static final String COMMAND_NOT_RECOGNISED = "500";
    public static final String BAD_SEQUENCE_OF_COMMADS = "503";
}