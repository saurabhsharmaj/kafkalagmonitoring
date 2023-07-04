package com.example.kafka.kafkaEntityService;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.kafka.csvHelper.CSVHelper;
import com.example.kafka.entity.KafkaEntity;
import com.example.kafka.repository.KafkaRepository;

@Service
public class KafkaEntityService {

	@Autowired
	private KafkaRepository kafkaRepo;

	
	public void save(MultipartFile file) {
		try {
			List<KafkaEntity> kafkaEntity = CSVHelper.csvToKafkaEntities(file.getInputStream());
			kafkaRepo.saveAll(kafkaEntity);
		}catch(IOException e) {
			throw new RuntimeException("fail to store csv data: " + e.getMessage());
		}
	}
	
	public List<KafkaEntity> getAllCustomer(){
		return kafkaRepo.findAll();
	}
}