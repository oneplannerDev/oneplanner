package com.ontide.oneplanner.service;


import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ontide.oneplanner.dao.EnvironmentBean;

@Service("emailSender")
public class EmailSender {
	@Autowired
	private EnvironmentBean environmentBean;

	//public static final String API_KEY; = envi 
			//"AIzaSyA5hfsRi_iJ_8yfWhYvGSez5NV4DGiiXNo";
	private static final Logger logger = LoggerFactory.getLogger(EmailSender.class);

	
    public boolean call(Object data, String tokenId, String callingNum, String callingName, String locale) {
    	boolean result = false;
    	return false;
	}
    
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
    
    public void send(String toEmail, String title, String content) throws MessagingException, UnsupportedEncodingException {
		final String username = environmentBean.getMailUserName();//"username@gmail.com";
		final String password = environmentBean.getMailPassword();//"password";

		Properties props = new Properties();
		props.put("mail.smtp.host", environmentBean.getMailSmtpHost()/*"smtp.gmail.com"*/);
		props.put("mail.smtp.port", environmentBean.getMailSmtpPort()/*"587"*/);

		Session session;
		if (username == null || "".equals(username)) {
			session = Session.getDefaultInstance(props);
		} else {
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			session = Session.getInstance(props,
					  new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(username, password);
						}
					  });
		}

		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(environmentBean.getMailFromMail()/*"from-email@gmail.com"*/));
		message.setRecipients(Message.RecipientType.TO,
			InternetAddress.parse(toEmail/*"to-email@gmail.com"*/));
		message.setSubject(MimeUtility.encodeText(title, "utf-8", "B"));
		//message.setText(content);
		message.setContent(content, "text/html; charset=utf-8");
		logger.info("username:"+username);
		logger.info("password:"+password);
		logger.info("environmentBean.getMailSmtpHost():"+environmentBean.getMailSmtpHost());
		logger.info("environmentBean.getMailSmtpPort():"+environmentBean.getMailSmtpPort());
		logger.info("environmentBean.getMailFromMail():"+environmentBean.getMailFromMail());
		logger.info("content:"+content);
		
		Transport.send(message);

		logger.info("email send done to"+toEmail);
    }

}
