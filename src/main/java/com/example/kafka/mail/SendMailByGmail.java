package com.example.kafka.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import com.example.kafka.entity.KafkaEntity;

@Service
public class  SendMailByGmail 
{	
	public void sendMail(KafkaEntity kafkaEntity,Long lag) { 
		  
		  String host="smtp.gmail.com";
		  String port ="587";
		  final String user="mkroks21my15@gmail.com";//change accordingly  
		  final String password="dckrxarlhrybpqui";//change accordingly  
		    
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
			     message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
			     message.setSubject("Lag Exceeds");  
			     message.setText("Topic name         : "+kafkaEntity.getTopicname()+"\n"+"Threshold value  : "+kafkaEntity.getThreshold()+"\n"+"Lag value            : "+lag+"\n"+
			     "Consumer group : "+kafkaEntity.getConsumergroup()+"\n"+"Owner                 : "+ kafkaEntity.getOwner());  
			       
			    //send the message  
			     Transport.send(message);  
			  
			     System.out.println("message sent successfully...");  
			   
			     } catch (MessagingException e) {e.printStackTrace();}  	
	}
}