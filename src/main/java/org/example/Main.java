package org.example;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class Main {
    public static User currentUser = null;

    public static void main(String[] args) throws MessagingException {
        while (true) {
            String gmail = Input.str("Gmail kiriting: ");
            String password = Input.str("Password: ");
            if (tekshiruv(gmail, password)) {
                int verificationCode = generateCode();
                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");

                String myAcc = "inosukebest1@gmail.com";
                String pass = "uxxhwgxbptdusvwm";

                Session session = getSession(props, pass, myAcc);

                Message message = new MimeMessage(session);
                message.setSubject("Verification code");
                message.setText(verificationCode + "");
                message.setFrom(new InternetAddress(myAcc));
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(gmail));
                Transport.send(message);
                System.out.println("Verification code sent");
                int inputCode = Input.num("Enter Code: ");
                if (inputCode == verificationCode) {
                    Cabinet.cabinet();
                    continue;
                }
                System.out.println("Verification code error");

            } else {
                System.out.println("Login or Password incorrect please try again");
            }
        }
    }

    private static boolean tekshiruv(String gmail, String password) {
        for (User user : DB.USERS) {
            if (user.getPassword().equals(password) && user.getGmail().equals(gmail)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    public static Session getSession(Properties props, String pass, String myAcc) {
        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAcc, pass);
            }
        });
    }

    public static int generateCode() {
        return new Random().nextInt(1000, 10000);
    }


}