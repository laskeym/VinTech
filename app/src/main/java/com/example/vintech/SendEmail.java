package com.example.vintech;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendEmail {
    private Context context;
    private Properties props;
    private Session session;
    private ProgressDialog pdialog;
    private String recipient, subject, textMessage;
    private File fileLocation;

    public SendEmail(Context c, File file) {
        context = c;
        recipient = "YOUR_EMAIL";
        subject = "VinTech Vehicle Report";
        textMessage = "Most recent vehicle report sent from app.";
        fileLocation = file;
    }

    public void sendEmail() {
        initProperties();
        initSession();

        pdialog = ProgressDialog.show(context, "", "Sending Mail...", true);

        SendEmailTask task = new SendEmailTask();
        task.execute();
    }

    private void initProperties() {
        props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
    }

    private void initSession() {
        session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(recipient, "YOUR_PASSWORD");
            }
        });
    }

    class SendEmailTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(recipient));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
                message.setSubject(subject);

                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(textMessage);

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);

                messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(fileLocation);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName("VinTechVehicleReport.xls");
                multipart.addBodyPart(messageBodyPart);

                message.setContent(multipart);

                Transport.send(message);
            } catch(MessagingException e) {
                e.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pdialog.dismiss();
            Toast.makeText(context, "Message sent", Toast.LENGTH_LONG).show();
        }
    }
}
