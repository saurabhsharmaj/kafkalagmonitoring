package com.example.kafka.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.kafka.common.CSVHelper;
import com.example.kafka.entity.TopicInfoEntity;
import com.example.kafka.repository.TopicInfoEntityRepository;

@Service
public class KafkaEntityService {

	@Autowired
	private TopicInfoEntityRepository topicInfoEntityRepository;

	public void save(MultipartFile file) {
		try {
			List<TopicInfoEntity> kafkaEntity = CSVHelper.csvToKafkaEntities(file.getInputStream());
			topicInfoEntityRepository.saveAll(kafkaEntity);
		} catch (IOException e) {
			throw new RuntimeException("fail to store csv data: " + e.getMessage());
		}
	}

	public List<TopicInfoEntity> getAllCustomer() {
		return topicInfoEntityRepository.findAll();
	}
}