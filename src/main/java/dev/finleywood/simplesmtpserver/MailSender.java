package dev.finleywood.simplesmtpserver;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.internal.MailerRegularBuilderImpl;

public class MailSender implements Runnable {
    private final Mail mailToSend;

    public MailSender(Mail mailToSend) {
        this.mailToSend = mailToSend;
    }

    @Override
    public void run() {

    }

    private void sendEmail() {
        Email email = EmailBuilder.startingBlank().from(Main.smtpConfig.emailFrom).to(Main.smtpConfig.emailTo)
                .withSubject("Received Message from " + this.mailToSend.getFrom() + " to " + this.mailToSend.getTo())
                .withH
        MailerRegularBuilderImpl mb = MailerBuilder.withSMTPServer(Main.smtpConfig.server, Main.smtpConfig.port, Main.smtpConfig.authUsername, Main.smtpConfig.authPassword);
    }

    private String constructMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Message received with UUID " + this.mailToSend.getUuid())
                .append('\n')
                .append("From: " + this.mailToSend.getFrom())
                .append('\n')
                .append("To: " + this.mailToSend.getTo())
                .append('\n')
                .append()
                // TODO: MIME message parsing and get content/time maybe?
    }
}
