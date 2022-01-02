package com.ipn.mx.utilities;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EnviarMail {
    public boolean enviarCorreo(String destinatario, String asunto, String mensaje){
        Properties p = new Properties();
        p.setProperty("mail.smtp.host", "smtp.gmail.com");
        p.setProperty("mail.smtp.starttls.enable", "true");
        p.setProperty("mail.smtp.port", "587");
        p.setProperty("mail.smtp.user", "cripto.equipo.5.escom@gmail.com");
        p.setProperty("mail.smtp.auth", "true");

        Session s = Session.getDefaultInstance(p);
        MimeMessage elMensaje = new MimeMessage(s);
        try {
            elMensaje.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(destinatario));
            elMensaje.setSubject(asunto);
            elMensaje.setText(mensaje);

            Transport t = s.getTransport("smtp");
            t.connect(p.getProperty("mail.smtp.user"),"ErickDiegoEscom");

            t.sendMessage(elMensaje,elMensaje.getAllRecipients());
            t.close();
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;


    }

    public static void main(String[] args) {
        EnviarMail em = new EnviarMail();
        em.enviarCorreo("erz_ra@hotmail.com","Si funciona","pos si jalo");

    }
}
