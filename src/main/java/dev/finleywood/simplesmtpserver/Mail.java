package dev.finleywood.simplesmtpserver;

public class Mail {
    private String from;
    private String to;
    private String incomingHostName;
    private String message = "";

    private boolean outgoing = true;

    public Mail(String from, String to, String incomingHostName, String message, boolean outgoing) {
        this.from = from;
        this.to = to;
        this.incomingHostName = incomingHostName;
        this.message = message;
        this.outgoing = outgoing;
    }

    public Mail() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void addMessageLine(String line) {
        this.message += (line + '\n');
    }

    public boolean isOutgoing() {
        return outgoing;
    }

    public void setOutgoing(boolean outgoing) {
        this.outgoing = outgoing;
    }

    public String getIncomingHostName() {
        return incomingHostName;
    }

    public void setIncomingHostName(String incomingHostName) {
        this.incomingHostName = incomingHostName;
    }
}
