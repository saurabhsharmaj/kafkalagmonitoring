package com.example.kafka.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "kafkalagmonitoring")
public class KafkaEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "groupid")
	int groupid;
	@Column(name = "emailid")
	String emailid;
	@Column(name = "owner")
	String owner;
	@Column(name = "description")
	String description;
	@Column(name = "topicname")
	String topicname;
	@Column(name = "consumergroup")
	String consumergroup;
	@Column(name = "threshold")
	int threshold;
	@Column(name = "monitoringstatus")
	int monitoringstatus;
	@Column(name = "timestamp")
	int timestamp;

	// Generating Getter Setter
	public int getGroupid() {
		return groupid;
	}

	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}

	

	public String getEmailid() {
		return emailid;
	}

	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTopicname() {
		return topicname;
	}

	public void setTopicname(String topicname) {
		this.topicname = topicname;
	}

	public String getConsumergroup() {
		return consumergroup;
	}

	public void setConsumergroup(String consumergroup) {
		this.consumergroup = consumergroup;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public int getMonitoringstatus() {
		return monitoringstatus;
	}

	public void setMonitoringstatus(int monitoringstatus) {
		this.monitoringstatus = monitoringstatus;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	// Constructor Using Fields

	public KafkaEntity(int groupid, String emailid, String owner, String description, String topicname,
			String consumergroup, int threshold, int monitoringstatus) {
		super();
		this.groupid = groupid;
		this.emailid = emailid;
		this.owner = owner;
		this.description = description;
		this.topicname = topicname;
		this.consumergroup = consumergroup;
		this.threshold = threshold;
		this.monitoringstatus = monitoringstatus;
	}
	

	// Constructor Using Superclass

	public KafkaEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	// To String

	@Override
	public String toString() {
		return "Entities [groupid=" + groupid + ", emailid=" + emailid + ", owner=" + owner + ", description="
				+ description + ", topicname=" + topicname + ", consumergroup=" + consumergroup + ", threshold="
				+ threshold + ", monitoringstatus=" + monitoringstatus + "]";
	}


}
