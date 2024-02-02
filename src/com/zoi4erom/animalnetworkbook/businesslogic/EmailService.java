package com.zoi4erom.animalnetworkbook.businesslogic;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Provides functionality for sending verification code emails.
 */
public class EmailService {

	private static final String CONFIG_FILE = "config.properties";
	private static final Properties properties = loadProperties();

	private EmailService() {
	}

	/**
	 * Loads email configuration properties from the specified file.
	 *
	 * @return properties loaded from the configuration file
	 */
	private static Properties loadProperties() {
		Properties props = new Properties();
		try (InputStream input = EmailService.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
			props.load(input);
		} catch (IOException e) {
			throw new RuntimeException("Failed to load configuration file " + CONFIG_FILE, e);
		}
		return props;
	}

	/**
	 * Sends a verification code email to the specified email address.
	 *
	 * @param email           the recipient's email address
	 * @param verificationCode the verification code to be included in the email
	 */
	public static void sendVerificationCodeEmail(String email, String verificationCode) {
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(properties.getProperty("mail.username"),
				    properties.getProperty("mail.password"));
			}
		});
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(properties.getProperty("mail.from")));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject("Verification Code");
			message.setText("Your verification code: " + verificationCode);

			Transport.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException("Error sending email: " + e.getMessage());
		}
	}
}
