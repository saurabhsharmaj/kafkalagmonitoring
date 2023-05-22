package com.example.kafka.entity;

import javax.persistence.Id;

import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "laganalyzer")
public class ElasticEntity {

	@Id
	private int id;
	
	private String topic;
	
	private int partition;
	
	private long lags;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public int getPartition() {
		return partition;
	}

	public void setPartition(int partition) {
		this.partition = partition;
	}

	public long getLags() {
		return lags;
	}

	public void setLags(long lags) {
		this.lags = lags;
	}

	public ElasticEntity(int id, String topic, int partition, long lags) {
		super();
		this.id = id;
		this.topic = topic;
		this.partition = partition;
		this.lags = lags;
	}

	@Override
	public String toString() {
		return "ElasticEntity [id=" + id + ", topic=" + topic + ", partition=" + partition + ", lags=" + lags + "]";
	}
		
	
}
