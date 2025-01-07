package org.example;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import static org.example.Main.generateCode;
import static org.example.Main.getSession;

public class Cabinet {
    public static void cabinet() throws MessagingException {
        while (true) {
            System.out.println("""
                    1 - add User.
                    2 - send Message.
                    3 - show all Users.
                    """);
            switch (Input.num("Choose: ")) {
                case 1 -> addUser();
                case 2 -> sendMessage();
                case 3 -> showAllUsers();
            }
        }
    }

    private static void showAllUsers() throws MessagingException {
        while (true){
            reportUsersAll();
            int chosenUser = Input.num("Choose User\n0-Back: ")-1;
            if(chosenUser == -1) {
                return;
            }
            System.out.println("""
                1 - Edit Gmail.
                2 - Edit Password.
                0 - Back.
                """);
            switch (Input.num("Choose: ")) {
                case 1->editGmail(chosenUser);
                case 2->editPassword(chosenUser);
                case 0-> {
                    return;
                }
            }
        }

    }

    private static void editPassword(int chosenUser) throws MessagingException {
        while (true){
            System.out.println("Current User Gmail : "+DB.USERS.get(chosenUser).getGmail());
            String realPassword = Input.str("Enter Password\n0-Back: ");
            if(realPassword.equals("0")) {
                return;
            }
            if(DB.USERS.get(chosenUser).getPassword().equals(realPassword)) {
            while (true){
                    String newPassword = Input.str("Enter New Password\n0-Back: ");
                    if(newPassword.equals("0")) {
                        return;
                    }
                    String newPasswordAgain = Input.str("Enter New Password Again\n0-Back: ");
                    if(newPasswordAgain.equals("0")) {
                        return;
                    }
                    if(newPasswordAgain.equals(newPassword)) {
                        String gmail = DB.USERS.get(chosenUser).getGmail();
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
                        if (verificationCode == inputCode) {
                            DB.USERS.get(chosenUser).setPassword(newPassword);
                            System.out.println("Password Edited");
                            return;
                        }else {
                            System.out.println("Incorrect code");
                        }
                    }else {
                        System.out.println("You Don't enter your new password again");
                    }
                }
            }else {
                System.out.println("Password incorrect");
            }
        }
    }

    private static void editGmail(int chosenUser) {
        while (true){
            System.out.println("Old Gmail : "+DB.USERS.get(chosenUser).getGmail());
            String email = Input.str("Enter New Gmail\n0-Back: ");
            if (email.equals("0")) {
                return;
            }
            if(tekshirish(email)){
                DB.USERS.get(chosenUser).setGmail(email);
                System.out.println("Gmail Edited");
                return;
            }else {
                System.out.println("This gmail already exists");
            }
        }
    }

    private static void reportUsersAll() {
        for (int i = 0; i < DB.USERS.size(); i++) {
            System.out.println(i+1+" "+DB.USERS.get(i).getGmail());
        }
    }

    private static void sendMessage() throws MessagingException {
        reportUsers();
        int chosenUser = Input.num("Choose User\n0-Back: ")-1;
        if(chosenUser == -1) return;
        String msg = Input.str("Enter Message: ");
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        String myAcc = "inosukebest1@gmail.com";
        String pass = "uxxhwgxbptdusvwm";

        Session session = getSession(props, pass, myAcc);

        Message message = new MimeMessage(session);
        message.setSubject("Message:");
        message.setText(msg);
        message.setFrom(new InternetAddress(myAcc));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(DB.USERS.get(chosenUser).getGmail()));
        Transport.send(message);
        System.out.println("Message sent");
    }

    private static void reportUsers() {
        for (int i = 0; i < DB.USERS.size(); i++) {
            if(DB.USERS.get(i).getGmail().equals(Main.currentUser.getGmail())){
                continue;
            }
            System.out.println(i+1+" "+DB.USERS.get(i).getGmail());
        }
    }

    private static void addUser() throws MessagingException {
        while (true) {
            String gmail = Input.str("Enter your Gmail\n0-Back: ");
            if (gmail.equals("0")) {
                return;
            }
            String password = Input.str("Enter your password\n0-Back: ");
            if (password.equals("0")) {
                return;
            }
            if (tekshirish(gmail)) {
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
                    DB.USERS.add(new User(gmail, password));
                    System.out.println("User added");
                    return;
                }
                System.out.println("Verification code error");
            }else {
                System.out.println("Gmail already exsists please try again");
            }
        }
    }

    private static boolean tekshirish(String gmail) {
        for (User user : DB.USERS) {
            if(user.getGmail().equals(gmail)) {
                return false;
            }
        }
        return true;
    }
}
