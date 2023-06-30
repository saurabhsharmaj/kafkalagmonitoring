package com.example.kafka.entity;

import javax.persistence.Id;

import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "heapinfo")
public class HeapEntity {
	
	@Id
	private int id;
	
	private String timestamp;

	private String output;
	
	private String command;

	public HeapEntity(int id, String timestamp, String output,String command) {
		super();
		this.id = id;
		this.timestamp = timestamp;
		this.output = output;
		this.command = command;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}
	
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	@Override
	public String toString() {
		return "HeapEntity [id=" + id + ", timestamp=" + timestamp + ", output=" + output + ",command=" + command +"]";
	}
}
