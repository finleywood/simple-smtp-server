package dev.finleywood.simplesmtpserver;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class Mail {
    private String uuid;
    private String from;
    private String to;
    private String incomingHostName;
    private String message = "";

    private boolean outgoing = true;

    public Mail(String uuid, String from, String to, String incomingHostName, String message, boolean outgoing) {
        this.uuid = uuid;
        this.from = from;
        this.to = to;
        this.incomingHostName = incomingHostName;
        this.message = message;
        this.outgoing = outgoing;
        this.generateUUID();
    }

    public Mail() {
        this.generateUUID();
    }

    private void generateUUID() {
        this.uuid = UUID.randomUUID().toString().replace("-", "");
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

    public String getUuid() {
        return uuid;
    }

    public void store() {
        try {
            FileWriter fw = new FileWriter("data/" + this.uuid + ".json");
            Gson gson = new Gson();
            fw.write(gson.toJson(this));
            fw.close();
        } catch (IOException e) {}

    }
}
