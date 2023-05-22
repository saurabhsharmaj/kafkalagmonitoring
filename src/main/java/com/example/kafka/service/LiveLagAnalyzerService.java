package com.example.kafka.service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.kafka.repository.KafkaRepository;

@Service
public class LiveLagAnalyzerService {

	private final LagAnalyzerService lagAnalyzerService;

	@Autowired
	private KafkaRepository kafkarepository;

	@Autowired
	public LiveLagAnalyzerService(LagAnalyzerService lagAnalyzerService)
	{
		this.lagAnalyzerService = lagAnalyzerService;
	}

	@Scheduled(fixedDelay = 5000L)
	public void liveLagAnalysis() throws ExecutionException, InterruptedException {
		List<String> groupIds = kafkarepository.getAllGroupName();
				for(int i = 0; i<groupIds.size();i++)
					lagAnalyzerService.analyzeLag(groupIds.get(i));
	}

	@Scheduled(fixedDelay = 1000L)
	public void produceData() throws ExecutionException, InterruptedException {
		List<String> list = kafkarepository.getAllTopicName();
		List<String> list1 = kafkarepository.getAllClusterName();
		for (int i = 0; i < list.size(); i++)
			lagAnalyzerService.pData(list.get(i),list1.get(i));
	}
	
	@Scheduled(fixedDelay = 1000L)
	public void consumerData() throws ExecutionException, InterruptedException {
		List<String> topics = kafkarepository.getAllTopicName();
		List<String> groupIds = kafkarepository.getAllGroupName();
		List<String> list = kafkarepository.getAllClusterName();
		for(int i = 0; i<topics.size(); i++)
			lagAnalyzerService.cData(Arrays.asList(topics.get(i)),groupIds.get(i),list.get(i));
	}
}