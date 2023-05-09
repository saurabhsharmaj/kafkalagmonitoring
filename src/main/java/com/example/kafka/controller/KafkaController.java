package com.example.kafka.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kafka.entity.KafkaEntity;
import com.example.kafka.repository.KafkaRepository;

@RestController
@RequestMapping("/restservice/v1.0/")
public class KafkaController {
	@Autowired
	KafkaRepository kafkarepository;

	@GetMapping("/kafka")
	public List<KafkaEntity> getAllTopic() {
		return kafkarepository.findAll();
	}

	@GetMapping("/kafka/{id}")
	public ResponseEntity<KafkaEntity> getTopicById(@PathVariable Integer id) {
		Optional<KafkaEntity> ent = kafkarepository.findById(id);
		if (ent.isPresent()) {
			return new ResponseEntity<KafkaEntity>(ent.get(), HttpStatus.FOUND);
		} else {
			return new ResponseEntity<KafkaEntity>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/kafka")
	public ResponseEntity<KafkaEntity> createTopic(@RequestBody KafkaEntity entities) {
		Optional<KafkaEntity> find = kafkarepository.findById(entities.getId());
		if (find.isPresent()) {
			KafkaEntity update = find.get();
			update.setId(entities.getId());
			update.setOwner(entities.getOwner());
			update.setEmailid(entities.getEmailid());
			update.setDescription(entities.getDescription());
			update.setTopicname(entities.getTopicname());
			update.setConsumergroup(entities.getConsumergroup());
			update.setThreshold(entities.getThreshold());
			update.setMonitoringstatus(entities.getMonitoringstatus());
			kafkarepository.save(update);
		} else {
			kafkarepository.save(entities);
		}

		return new ResponseEntity<KafkaEntity>(entities, HttpStatus.OK);
	}

	@PutMapping("/kafka")
	public ResponseEntity<KafkaEntity> updateTopic(@RequestBody KafkaEntity entities) {
		return createTopic(entities);
	}

	@DeleteMapping("/kafka/{id}")
	public ResponseEntity<KafkaEntity> deleteById(@PathVariable Integer id) {
		Optional<KafkaEntity> find = kafkarepository.findById(id);
		if (find.isPresent()) {
			kafkarepository.deleteById(id);
			return new ResponseEntity<KafkaEntity>(find.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<KafkaEntity>(new KafkaEntity(), HttpStatus.OK);
		}
	}

}
