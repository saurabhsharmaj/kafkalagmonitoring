package com.example.kafka.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.kafka.common.CSVHelper;
import com.example.kafka.common.ResponseMessage;
import com.example.kafka.entity.TopicInfoEntity;
import com.example.kafka.exception.FillDataException;
import com.example.kafka.exception.ResourceNotFoundException;
import com.example.kafka.repository.TopicInfoEntityRepository;
import com.example.kafka.service.KafkaEntityService;

@CrossOrigin("http://localhost:3000/")
@RestController
@RequestMapping("/api")
public class KafkaController {

	@Autowired
	private TopicInfoEntityRepository topicInfoEntityRepository;

	@Autowired
	private KafkaEntityService kafkaEntityService;

	// getting all topicdetails
	@GetMapping("/gettopicdetails")
	public List<TopicInfoEntity> gettopicdetails() {
		List<TopicInfoEntity> list = new ArrayList<>();
		if (topicInfoEntityRepository.findAll().isEmpty())
			throw new ResourceNotFoundException();
		else

		{
			topicInfoEntityRepository.findAll().forEach(list::add);
			return list;
		}
	}

	@GetMapping("/kafkadetails/{groupid}")
	public TopicInfoEntity getKafkaById(@PathVariable int groupid) {
		return topicInfoEntityRepository.findById(groupid).get();
	}

	// getting topic details according clusterid
	@GetMapping("/gettopicdata/{clusterid}")
	public List<TopicInfoEntity> getTopicCluster(@PathVariable int clusterid) {
		List<TopicInfoEntity> list = new ArrayList<>();
		list = topicInfoEntityRepository.findByClusterId(clusterid);
		return list;
	}

	@GetMapping("/kafka/{groupid}")
	public ResponseEntity<TopicInfoEntity> getByGroupid(@PathVariable Integer groupid) {
		Optional<TopicInfoEntity> ent = topicInfoEntityRepository.findById(groupid);
		if (ent.isPresent()) {
			return new ResponseEntity<TopicInfoEntity>(ent.get(), HttpStatus.FOUND);
		} else {
			throw new ResourceNotFoundException();
		}
	}

	@PostMapping("/uploadfile")
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam MultipartFile file) {
		String message = "";

		if (CSVHelper.hasCSVFormat(file)) {
			try {
				kafkaEntityService.save(file);
				message = "Uploaded the file successfully: " + file.getOriginalFilename();
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
			} catch (Exception e) {
				message = "Could not upload the file: " + file.getOriginalFilename() + "!";
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
			}
		}
		message = "Please upload a csv file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
	}

	// adding details for topic
	@PostMapping("/post/kafka")
	public ResponseEntity<String> createLagMonitoring(@RequestBody TopicInfoEntity entities) {
		if (entities.getConsumergroup().isEmpty() || entities.getDescription().isEmpty()
				|| entities.getEmailid().isEmpty() || entities.getOwner().isEmpty()
				|| entities.getTopicname().isEmpty()) {

			throw new FillDataException();
		} else {
			topicInfoEntityRepository.save(entities);
			return ResponseEntity.ok("Data saved in DB !!");
		}
	}

	@PutMapping("/kafkadetailsupdate")
	public String updateByid(@RequestBody TopicInfoEntity entities) {
		Optional<TopicInfoEntity> find = topicInfoEntityRepository.findById(entities.getGroupid());
		if (find.isPresent()) {
			TopicInfoEntity update = find.get();

			update.setOwner(entities.getOwner());
			update.setEmailid(entities.getEmailid());
			update.setDescription(entities.getDescription());
			update.setTopicname(entities.getTopicname());
			update.setConsumergroup(entities.getConsumergroup());
			update.setThreshold(entities.getThreshold());
			update.setMonitoringstatus(entities.getMonitoringstatus());
			update.setClusterid(entities.getClusterid());

			topicInfoEntityRepository.save(update);

			return "Detail Updated";
		} else {
			throw new ResourceNotFoundException();
		}

	}

	// delete topic by id
	@SuppressWarnings("unused")
	@DeleteMapping("/deletetopic/{id}")
	@Transactional
	public String deleteByGroupId(@PathVariable int id) {
		if (true) {
			topicInfoEntityRepository.deleteById(id);
			return "Data Deleted";
		} else
			throw new ResourceNotFoundException();
	}

}
