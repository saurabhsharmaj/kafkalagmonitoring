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

	public ClusterInfo(int clusterid, String clustername, int monitoringstatus, List<KafkaEntity> kafkaEntity) {
		super();
		this.clusterid = clusterid;
		this.clustername = clustername;
		this.monitoringstatus = monitoringstatus;
		this.kafkaEntity = kafkaEntity;
	}

	public ClusterInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "ClusterInfo [clusterid=" + clusterid + ", clustername=" + clustername + ", monitoringstatus="
				+ monitoringstatus + ", kafkaEntity=" + kafkaEntity + "]";
	}

}
