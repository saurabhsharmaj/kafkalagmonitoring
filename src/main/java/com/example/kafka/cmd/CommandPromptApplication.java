package com.example.kafka.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.kafka.entity.HeapEntity;
import com.example.kafka.repository.HeapElasticRepo;
import com.example.kafka.service.MonitoringUtil;

@Service
public class CommandPromptApplication {

	@Autowired
	private HeapElasticRepo heapElasticRepo;

	int id = 1;
	String command = "command";

	@Scheduled(fixedRate = 30000L)
	private void executeCommandPeriodically() throws IOException {

		long pid = ProcessHandle.current().pid();
		ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", "\"jcmd " + pid + " GC.heap_info\"");

		Process process = processBuilder.start();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String output;
		while ((output = reader.readLine()) != null) {
			HeapEntity heapEntity = new HeapEntity(id, MonitoringUtil.time(), output, command);
			heapElasticRepo.save(heapEntity);
			id++;
		}

		process.destroy();
	}
}
