package com.example.kafka.mail;

import java.io.File;
import java.nio.file.Files;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import com.example.kafka.entity.KafkaEntity;

@Service
public class  SendMailByGmail 
{	
	@Value("${user.mailid.config}")
	String user;
	@Value("${user.password.config}")
	String password;
	
	@SuppressWarnings("null")
	String Example(String CN, String TN, String CG, String OW, int thres, long lags) {
		String cn = CN;
		String tn = TN;
		String cg = CG;
		String ow = OW;
		int th = thres;
		long la = lags;
		
		Model model= new ConcurrentModel();
		model.addAttribute("cn",cn);
		model.addAttribute("tn",tn);
		model.addAttribute("cg",cg);
		model.addAttribute("ow",ow);
		model.addAttribute("th",th);
		model.addAttribute("la",la);
		
		return "email-template";
	}	
	public void sendMail(KafkaEntity kafkaEntity,Long lag,String clusterName) { 
		  
		  String host="smtp.gmail.com";
		  String port ="587";
		    
		  String to=kafkaEntity.getEmailid();  
		  
		   //Get the session object  
		   Properties props = new Properties();  
		   props.put("mail.smtp.host",host);  
		   props.put("mail.smtp.auth", "true");
		   props.put("mail.smtp.port", port);
		   props.put("mail.smtp.starttls.enable", "true");
		   
		   Session session = Session.getDefaultInstance(props,  
				    new javax.mail.Authenticator() {  
				      protected PasswordAuthentication getPasswordAuthentication() {  
				    return new PasswordAuthentication(user,password);  
				      }  
				    });  
		   try {
				MimeMessage message = new MimeMessage(session);
				message.setFrom(new InternetAddress(user));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
				message.setSubject("Lag Exceeds");
				message.setText("Cluster name : " + clusterName + "\n" + "Topic name : " + kafkaEntity.getTopicname() + "\n"
						+ "Threshold value  : " + kafkaEntity.getThreshold() + "\n" + "Lag value : " + lag + "\n"
						+ "Consumer group : " + kafkaEntity.getConsumergroup() + "\n" + "Owner : "
						+ kafkaEntity.getOwner());

				String ClusterName = clusterName;
				String TopicName = kafkaEntity.getTopicname();
				int threshold = kafkaEntity.getThreshold();
				long lags = lag;
				String ConsumerGroup = kafkaEntity.getConsumergroup();
				String Owner = kafkaEntity.getOwner();
				
				Example(ClusterName, TopicName, ConsumerGroup, Owner, threshold, lags);
//				 String currentUser = message.getFrom()[0].toString();
//				 String subject1 = message.getSubject();
//				 String text1 = message.getContentID();
				
					
				String template = readTemplateFile("\\kafkalagmonitoring\\src\\main\\resources\\templates\\email-template.html");

			//	       			 template = template.replace("{subject_1}", subject1 );
//	       			 template = template.replace("{current_user}", currentUser);
//				 template = template.replace("{text_1}", text1);
				
				 message.setContent(template,"text/html");
//				try {
//					message.setContent(readTemplateFile("\\kafkalagmonitoring\\src\\main\\resources\\templates\\email-template.html"), "text/html");
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
				Transport.send(message);

				System.out.println("\n" + "<<<<<<<<<<<<<<<<<<<<<<<<<<<Message sent SUCCESSFULLY for " + clusterName
						+ " Cluster And " + kafkaEntity.getTopicname() + " Topic.>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + "\n");

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		String readTemplateFile(String fileName) throws Exception {
			File file = new File(fileName);
			String content = new String(Files.readAllBytes(file.toPath()));
			return content;
		}
	}