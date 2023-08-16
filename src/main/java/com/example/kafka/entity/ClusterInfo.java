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
	int clusterid;
	String clustername;
	int monitoringstatus;

	String zookeeper_servers;
	String bootstrap_servers;
	String zoo_logs_dir; 
	String broker_logs_dir;
	String kafdropPort;
	

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "clusterid")
	List<KafkaEntity> kafkaEntity;

	public int getClusterid() {
		return clusterid;
	}

	public void setClusterid(int clusterid) {
		this.clusterid = clusterid;
	}

	public String getClustername() {
		return clustername;
	}

	public void setClustername(String clustername) {
		this.clustername = clustername;
	}

	public int getMonitoringstatus() {
		return monitoringstatus;
	}

	public void setMonitoringstatus(int monitoringstatus) {
		this.monitoringstatus = monitoringstatus;
	}

	public List<KafkaEntity> getKafkaEntity() {
		return kafkaEntity;
	}

	public void setKafkaEntity(List<KafkaEntity> kafkaEntity) {
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
		return zookeeper_servers;
	}

	public void setZookeeper_servers(String zookeeper_servers) {
		this.zookeeper_servers = zookeeper_servers;
	}

	public String getBootstrap_servers() {
		return bootstrap_servers;
	}

	public void setBootstrap_servers(String bootstrap_servers) {
		this.bootstrap_servers = bootstrap_servers;
	}

	public String getZoo_logs_dir() {
		return zoo_logs_dir;
	}

	public void setZoo_logs_dir(String zoo_logs_dir) {
		this.zoo_logs_dir = zoo_logs_dir;
	}

	public String getBroker_logs_dir() {
		return broker_logs_dir;
	}

	public void setBroker_logs_dir(String broker_logs_dir) {
		this.broker_logs_dir = broker_logs_dir;
	}

	
	public ClusterInfo(int clusterid, String clustername, int monitoringstatus, String zookeeper_servers,
			String bootstrap_servers, String zoo_logs_dir, String broker_logs_dir, String kafdropPort,
			List<KafkaEntity> kafkaEntity) {
		super();
		this.clusterid = clusterid;
		this.clustername = clustername;
		this.monitoringstatus = monitoringstatus;
		this.zookeeper_servers = zookeeper_servers;
		this.bootstrap_servers = bootstrap_servers;
		this.zoo_logs_dir = zoo_logs_dir;
		this.broker_logs_dir = broker_logs_dir;
		this.kafdropPort = kafdropPort;
		this.kafkaEntity = kafkaEntity;
	}

	@Override
	public String toString() {
		return "ClusterInfo [clusterid=" + clusterid + ", clustername=" + clustername + ", monitoringstatus="
				+ monitoringstatus + ", zookeeper_servers=" + zookeeper_servers + ", bootstrap_servers="
				+ bootstrap_servers + ", zoo_logs_dir=" + zoo_logs_dir + ", broker_logs_dir=" + broker_logs_dir
				+ ", kafdropPort=" + kafdropPort + ", kafkaEntity=" + kafkaEntity + "]";
	}



}
