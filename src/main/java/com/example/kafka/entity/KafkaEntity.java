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
	
	@Column(name="clusterid")
	int clusterid;
	
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
	public int getClusterid() {
		return clusterid;
	}
	public void setClusterid(int clusterid) {
		this.clusterid = clusterid;
	}
	public KafkaEntity(String emailid, String owner, String description, String topicname,
			String consumergroup, int threshold, int monitoringstatus, int timestamp, int clusterid) {
		super();
		this.emailid = emailid;
		this.owner = owner;
		this.description = description;
		this.topicname = topicname;
		this.consumergroup = consumergroup;
		this.threshold = threshold;
		this.monitoringstatus = monitoringstatus;
		this.timestamp = timestamp;
		this.clusterid = clusterid;
	}

	public KafkaEntity() {
		
	}
	@Override
	public String toString() {
		return "KafkaEntity [groupid=" + groupid + ", emailid=" + emailid + ", owner=" + owner + ", description="
				+ description + ", topicname=" + topicname + ", consumergroup=" + consumergroup + ", threshold="
				+ threshold + ", monitoringstatus=" + monitoringstatus + ", timestamp=" + timestamp + ", clusterid="
				+ clusterid + "]";
	}
	
	
}
