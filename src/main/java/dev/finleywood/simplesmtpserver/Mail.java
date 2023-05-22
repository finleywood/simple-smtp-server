package dev.finleywood.simplesmtpserver;

public class Mail {
    private String from;
    private String to;
    private String message = "";

    public Mail(String from, String to, String message) {
        this.from = from;
        this.to = to;
        this.message = message;
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

}