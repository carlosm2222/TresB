package com.example.fdope.tresb.FactoriaProductos;

import android.os.AsyncTask;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**
 * Created by SS on 05-12-2016.
 */

public class RetreiveFeedTask  extends AsyncTask<String, Void, String> {
    Session session;
    String subject,textMessage,email;

    public RetreiveFeedTask(Session session, String subject, String textMessage, String email) {
        this.session = session;
        this.subject = subject;
        this.textMessage = textMessage;
        this.email = email;
    }

    @Override
    protected String doInBackground(String... params) {

        try{
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("tresb.company@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(subject);
            message.setContent(textMessage, "text/html; charset=utf-8");
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

    }

}