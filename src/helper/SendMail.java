package helper;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import config.ConfigProperties;

public class SendMail {
	private static SendMail sendMail;
	private static Session session;

	private SendMail() {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
		props.put("mail.smtp.port", "587"); // TLS Port
		props.put("mail.smtp.auth", "true"); // enable authentication
		props.put("mail.smtp.starttls.enable", "true"); // enable STARTTLS
		Authenticator auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(ConfigProperties.EMAIL_SERVICE,
						ConfigProperties.PASSWORD_EMAIL_SERVICE);
			}
		};
		session = Session.getInstance(props, auth);
	}

	public static void Send(String subject, String body) {
		if (sendMail == null)
			sendMail = new SendMail();
		if(session == null) return;
		try {
			MimeMessage msg = new MimeMessage(session);
			// set message headers
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			msg.setFrom(new InternetAddress(ConfigProperties.EMAIL_SERVICE, "NoReply-JD"));

			msg.setReplyTo(InternetAddress.parse(ConfigProperties.EMAIL_SERVICE, false));

			msg.setSubject(subject, "UTF-8");

			msg.setText(body, "UTF-8");

			msg.setSentDate(new Date());

			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(ConfigProperties.EMAIL_TO, false));
			Transport.send(msg);

		} catch (MessagingException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Gui mail thanh cong");
	}
}
