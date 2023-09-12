package com.example.kafka.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "topic_info")
public class TopicInfoEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "groupid")
	private int groupId;

	@Column(name = "emailid")
	String emailId;

	@Column(name = "owner")
	String owner;

	@Column(name = "description")
	String description;

	@Column(name = "topicname")
	String topicName;

	@Column(name = "consumergroup")
	String consumerGroup;

	@Column(name = "threshold")
	int threshold;

	@Column(name = "monitoringstatus")
	int monitoringStatus;

	@Column(name = "timestamp")
	int timestamp;

	@Column(name = "clusterid")
	int clusterId;

	public int getGroupid() {
		return groupId;
	}

	public void setGroupid(int groupid) {
		this.groupId = groupid;
	}

	public String getEmailid() {
		return emailId;
	}

	public void setEmailid(String emailid) {
		this.emailId = emailid;
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
		return topicName;
	}

	public void setTopicname(String topicname) {
		this.topicName = topicname;
	}

	public String getConsumergroup() {
		return consumerGroup;
	}

	public void setConsumergroup(String consumergroup) {
		this.consumerGroup = consumergroup;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public int getMonitoringstatus() {
		return monitoringStatus;
	}

	public void setMonitoringstatus(int monitoringstatus) {
		this.monitoringStatus = monitoringstatus;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	public int getClusterid() {
		return clusterId;
	}

	public void setClusterid(int clusterid) {
		this.clusterId = clusterid;
	}

	public TopicInfoEntity(String emailid, String owner, String description, String topicname, String consumergroup,
			int threshold, int monitoringstatus, int timestamp, int clusterid) {
		super();
		this.emailId = emailid;
		this.owner = owner;
		this.description = description;
		this.topicName = topicname;
		this.consumerGroup = consumergroup;
		this.threshold = threshold;
		this.monitoringStatus = monitoringstatus;
		this.timestamp = timestamp;
		this.clusterId = clusterid;
	}

	public TopicInfoEntity() {

	}

	@Override
	public String toString() {
		return "KafkaEntity [groupid=" + groupId + ", emailid=" + emailId + ", owner=" + owner + ", description="
				+ description + ", topicname=" + topicName + ", consumergroup=" + consumerGroup + ", threshold="
				+ threshold + ", monitoringstatus=" + monitoringStatus + ", timestamp=" + timestamp + ", clusterid="
				+ clusterId + "]";
	}

}
