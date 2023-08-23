package com.example.kafka.mail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

//import com.example.kafka.MyService;
import com.example.kafka.entity.KafkaEntity;

@Service
public class SendMailByGmail {
	@Autowired
	private TemplateEngine templateEngine;

	@Value("${user.mailid.config}")
	String user;
	@Value("${user.password.config}")
	String password;

	public void sendMail(KafkaEntity kafkaEntity, Long lag, String clusterName) {

		String host = "smtp.gmail.com";
		String port = "587";

		String to = kafkaEntity.getEmailid();

		// Get the session object
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.starttls.enable", "true");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(user));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject("Lag Exceeds");
			message.setContentID("Cluster name : " + clusterName + "\n" + "Topic name : " + kafkaEntity.getTopicname()
					+ "\n" + "Threshold value  : " + kafkaEntity.getThreshold() + "\n" + "Lag value : " + lag + "\n"
					+ "Consumer group : " + kafkaEntity.getConsumergroup() + "\n" + "Owner : "
					+ kafkaEntity.getOwner());

			String ClusterName = clusterName;
			String TopicName = kafkaEntity.getTopicname();
			int threshold = kafkaEntity.getThreshold();
			long lags = lag;
			String ConsumerGroup = kafkaEntity.getConsumergroup();
			String Owner = kafkaEntity.getOwner();

			String threshold1 = String.valueOf(threshold);
			String lags1 = String.valueOf(lags);

			String template = readTemplateFile("\\kafka2\\src\\main\\resources\\templates\\email-template.html");

			template = template.replace("{ClusterName}", ClusterName);
			template = template.replace("{TopicName}", TopicName);
			template = template.replace("{threshold}", threshold1);
			template = template.replace("{lags}", lags1);
			template = template.replace("{ConsumerGroup}", ConsumerGroup);
			template = template.replace("{Owner}", Owner);

			message.setContent(template, "text/html");

			Transport.send(message);

			System.out.println("\n" + "<<<<<<<<< Mail Alert for increased lags sent successfully >>>>> For Cluster "
					+ clusterName + " And Topic " + kafkaEntity.getTopicname() + "\n");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String generateEmailContent(List<KafkaEntity> entities) {
		Context context = new Context();
		context.setVariable("entities", entities);
		return templateEngine.process("email-template", context);
	}

	String readTemplateFile(String fileName) throws Exception {
		File file = new File(fileName);
		String content = new String(Files.readAllBytes(file.toPath()));
		return content;
	}

}