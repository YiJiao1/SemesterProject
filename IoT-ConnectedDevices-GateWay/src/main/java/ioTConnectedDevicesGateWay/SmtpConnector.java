package ioTConnectedDevicesGateWay;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SmtpConnector {
	
	public static String myEmailAccount = "jiaoyi199507@gmail.com";
	public static String myEmailPassword = "jxjy2006";
	public static String myEmailSMTPHost = "smtp.gmail.com";
	public static String receiveMailAccount = "jiao.yi1@husky.neu.edu";
	public static String smtpPort = "465";

	/**
	 * @param session
	 * @param sendMail    sending email
	 * @param receiveMail receive email
	 * @param Text        Temperature Content
	 * @return message
	 * @throws Exception
	 */
	public static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail, String text)
			throws Exception {
		// create message
		MimeMessage message = new MimeMessage(session);
		// set From address
		message.setFrom(new InternetAddress(sendMail, "IotGateWay", "UTF-8"));
		// set to address
		message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, "SensorHat user", "UTF-8"));
		message.setSubject("Temperatre High Alarm", "UTF-8");
		message.setContent(text, "text/html;charset=UTF-8");
		message.saveChanges();
		return message;
	}

	/**
	 * @param text file_Content
	 * @return
	 * @throws Exception
	 */
	public static void SendMail(String text) {
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.host", myEmailSMTPHost);
		props.setProperty("mail.smtp.auth", "true"); // need auth, and check password
		props.setProperty("mail.smtp.port", smtpPort);
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.socketFactory.port", smtpPort);
		Session session = Session.getInstance(props);
		session.setDebug(true); // set debug to check log
		MimeMessage message;
		try {
			message = createMimeMessage(session, myEmailAccount, receiveMailAccount, text);
			Transport transport = session.getTransport();
			transport.connect(myEmailAccount, myEmailPassword);
			transport.sendMessage(message, message.getAllRecipients());// send msg function
			transport.close(); // close
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Smtp error");
		}
	}

}
