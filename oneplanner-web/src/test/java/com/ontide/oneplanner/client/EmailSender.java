package com.ontide.oneplanner.client;


import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailSender {

	//public static final String API_KEY; = envi 
			//"AIzaSyA5hfsRi_iJ_8yfWhYvGSez5NV4DGiiXNo";
	private static final Logger logger = LoggerFactory.getLogger(EmailSender.class);

	/*
     * mail property:
     * 1. mail.username
     * 2. mail.password
     * 3. mail.smtp.host
     * 4. mail.smtp.port
     * 5. mail.from.mail
     * 6. mail.title
     * 7. mail.content
     */
    public void send() {
		final String username = "oneplanner.sys@gmail.com";
		final String password = "Oneplanner2017*";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		try {
			Session session = Session.getInstance(props,
					  new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(username, password);
						}
					  });

					Message message = new MimeMessage(session);
					message.setFrom(new InternetAddress("oneplanner.sys@gmail.com"));
					message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse("jinnonsbox@gmail.com"));
			message.setSubject("test");
			message.setText("test");
			
			Transport.send(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

}
