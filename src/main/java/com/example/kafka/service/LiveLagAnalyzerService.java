package com.example.kafka.service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.kafka.repository.KafkaRepository;

@Service
public class LiveLagAnalyzerService {

	@Autowired
	private LagAnalyzerService lagAnalyzerService;

	@Autowired
	private KafkaRepository kafkarepository;

	@Scheduled(fixedDelay = 5000L)
	public void liveLagAnalysis() throws ExecutionException, InterruptedException {
		List<String> groupIds = kafkarepository.getAllGroupNamesByMonitoringStatus();
		List<String> servers = kafkarepository.getAllBootstrapServersByMonitoringStatus();
		List<String> clusters = kafkarepository.getAllClusterNamesByMonitoringStatus();
				for(int i = 0; i<groupIds.size();i++)
					lagAnalyzerService.analyzeLag(groupIds.get(i),servers.get(i),clusters.get(i));
	}

	@Scheduled(fixedDelay = 5000L)
	public void produceData() throws ExecutionException, InterruptedException {
		List<String> topics = kafkarepository.getAllTopicNamesByMonitoringStatus();
		List<String> clusters = kafkarepository.getAllClusterNamesByMonitoringStatus();
		List<String> servers = kafkarepository.getAllBootstrapServersByMonitoringStatus();
		for (int i = 0; i < topics.size(); i++)
			lagAnalyzerService.pData(topics.get(i),clusters.get(i),servers.get(i));
	}
	
//	@Scheduled(fixedDelay = 5000L)
//	public void consumerData() throws ExecutionException, InterruptedException {
//		List<String> topics = kafkarepository.getAllTopicNamesByMonitoringStatus();
//		List<String> groupIds = kafkarepository.getAllGroupNamesByMonitoringStatus();
//		List<String> clusters = kafkarepository.getAllClusterNamesByMonitoringStatus();
//		List<String> servers = kafkarepository.getAllBootstrapServersByMonitoringStatus();
//		for(int i = 0; i<topics.size(); i++)
//			lagAnalyzerService.cData(Arrays.asList(topics.get(i)),groupIds.get(i),clusters.get(i),servers.get(i));
//	}
}