package com.example.kafka.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class ClusterInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int clusterId;
	String clusterName;
	int monitoringStatus;

	String zookeeperServers;
	String bootstrapServers;
	String zooLogsDir; 
	String brokerLogsDir;
	String kafdropPort;
	

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "clusterid")
	List<TopicInfoEntity> kafkaEntity;

	public int getClusterid() {
		return clusterId;
	}

	public void setClusterid(int clusterid) {
		this.clusterId = clusterid;
	}

	public String getClustername() {
		return clusterName;
	}

	public void setClustername(String clustername) {
		this.clusterName = clustername;
	}

	public int getMonitoringstatus() {
		return monitoringStatus;
	}

	public void setMonitoringstatus(int monitoringstatus) {
		this.monitoringStatus = monitoringstatus;
	}

	public List<TopicInfoEntity> getKafkaEntity() {
		return kafkaEntity;
	}

	public void setKafkaEntity(List<TopicInfoEntity> kafkaEntity) {
		this.kafkaEntity = kafkaEntity;
	}

	
	public ClusterInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getKafdropPort() {
		return kafdropPort;
	}

	public void setKafdropPort(String kafdropPort) {
		this.kafdropPort = kafdropPort;
	}
	
	public String getZookeeper_servers() {
		return zookeeperServers;
	}

	public void setZookeeper_servers(String zookeeper_servers) {
		this.zookeeperServers = zookeeper_servers;
	}

	public String getBootstrap_servers() {
		return bootstrapServers;
	}

	public void setBootstrap_servers(String bootstrap_servers) {
		this.bootstrapServers = bootstrap_servers;
	}

	public String getZoo_logs_dir() {
		return zooLogsDir;
	}

	public void setZoo_logs_dir(String zoo_logs_dir) {
		this.zooLogsDir = zoo_logs_dir;
	}

	public String getBroker_logs_dir() {
		return brokerLogsDir;
	}

	public void setBroker_logs_dir(String broker_logs_dir) {
		this.brokerLogsDir = broker_logs_dir;
	}

	
	public ClusterInfo(int clusterid, String clustername, int monitoringstatus, String zookeeper_servers,
			String bootstrap_servers, String zoo_logs_dir, String broker_logs_dir, String kafdropPort,
			List<TopicInfoEntity> kafkaEntity) {
		super();
		this.clusterId = clusterid;
		this.clusterName = clustername;
		this.monitoringStatus = monitoringstatus;
		this.zookeeperServers = zookeeper_servers;
		this.bootstrapServers = bootstrap_servers;
		this.zooLogsDir = zoo_logs_dir;
		this.brokerLogsDir = broker_logs_dir;
		this.kafdropPort = kafdropPort;
		this.kafkaEntity = kafkaEntity;
	}

	@Override
	public String toString() {
		return "ClusterInfo [clusterid=" + clusterId + ", clustername=" + clusterName + ", monitoringstatus="
				+ monitoringStatus + ", zookeeper_servers=" + zookeeperServers + ", bootstrap_servers="
				+ bootstrapServers + ", zoo_logs_dir=" + zooLogsDir + ", broker_logs_dir=" + brokerLogsDir
				+ ", kafdropPort=" + kafdropPort + ", kafkaEntity=" + kafkaEntity + "]";
	}



}
