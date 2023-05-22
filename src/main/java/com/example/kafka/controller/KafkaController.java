package com.example.kafka.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.kafka.entity.ClusterInfo;
import com.example.kafka.entity.KafkaEntity;
import com.example.kafka.exception.FillDataException;
import com.example.kafka.exception.ResourceNotFoundException;
import com.example.kafka.repository.ClusterRepo;
import com.example.kafka.repository.KafkaRepository;

@RestController
public class KafkaController {
	
	@Autowired
	private KafkaRepository kafkarepository;
	
	@Autowired
	private ClusterRepo clusterRepo;
		
	@PostMapping("/kafka")
	public ResponseEntity<String> createLagMonitoring(@RequestBody KafkaEntity entities)
	{
		if(entities.getConsumergroup().isEmpty()||entities.getDescription().isEmpty()||entities.getEmailid().isEmpty()||entities.getOwner().isEmpty()
				||entities.getTopicname().isEmpty())
			throw new FillDataException();
		else
		{	
			kafkarepository.save(entities);
			return ResponseEntity.ok("Data saved in DB !!");
		}
	}
	
	@GetMapping("/kafka")
	public List<KafkaEntity> getLagMonitoring()
	{
		List<KafkaEntity> list = new ArrayList<>();
		if(kafkarepository.findAll().isEmpty())
			throw new ResourceNotFoundException();
		else
		{
			kafkarepository.findAll().forEach(list::add);
			return list;
		}
	}
	@GetMapping("/kafka/{groupid}")
	public ResponseEntity<KafkaEntity> getByGroupid(@PathVariable Integer groupid)
	{
		Optional<KafkaEntity> ent = kafkarepository.findById(groupid);
		if(ent.isPresent())
		{
			return new ResponseEntity<KafkaEntity>(ent.get(),HttpStatus.FOUND);
		}
		else
		{
			throw new ResourceNotFoundException();
		}
	}
	
	@PutMapping("/kafka/{groupid}")
	public String updateByid(@PathVariable Integer groupid,@RequestBody KafkaEntity entities)
	{
		Optional<KafkaEntity> find = kafkarepository.findById(groupid);
		if(find.isPresent())
		{
			KafkaEntity update = find.get();
			
			update.setGroupid(entities.getGroupid());
			update.setOwner(entities.getOwner());
			update.setEmailid(entities.getEmailid());
			update.setDescription(entities.getDescription());
			update.setTopicname(entities.getTopicname());
			update.setConsumergroup(entities.getConsumergroup());
			update.setThreshold(entities.getThreshold());
			update.setMonitoringstatus(entities.getMonitoringstatus());
			
			kafkarepository.save(update);
			
			return "Detail Updated";
		}
		else
		{
			throw new ResourceNotFoundException();
		}
		
				
	}
	
	@DeleteMapping("/kafka/{groupid}")
	public ResponseEntity<String> deleteById(@PathVariable Integer groupid )
	{
		if(kafkarepository.findById(groupid).isPresent())
		{
			kafkarepository.deleteById(groupid);
			return ResponseEntity.ok("Data Deleted !!");
		}
		else
			throw new ResourceNotFoundException();
	}
	
	@PutMapping("/kafka/updateonlymonitoringstatus")
	public ResponseEntity<String> updateStatus(@RequestParam String topicname, @RequestParam int status)
	{
		KafkaEntity kafkaEntity = kafkarepository.findByTopicname(topicname);
		kafkaEntity.setMonitoringstatus(status);
		kafkarepository.save(kafkaEntity);
		return ResponseEntity.ok("Data updated !!");
	}
	
	@PutMapping("/kafka/updateonlytimestamp")
	public ResponseEntity<String> updateTimeStamp()
	{
		List<String> topic = kafkarepository.getAllTopicNames();
		for(int i=0;i<topic.size();i++)
		{
			KafkaEntity kafkaEntity = kafkarepository.findByTopicname(topic.get(i));
			kafkaEntity.setTimestamp(-1);
			kafkarepository.save(kafkaEntity);
		}
		return ResponseEntity.ok("Data updated !!");
	}

	// cluster apis

		@PostMapping("/clusterinfo")
		public String createCluster(@RequestBody List<ClusterInfo> cluster) {

			clusterRepo.saveAll(cluster);
			return "Data Created Successfully";
		}

		@GetMapping("/clusterinfo")
		public List<ClusterInfo> getClusterInfo() {
			List<ClusterInfo> list = new ArrayList<>();
			list = clusterRepo.findAll();
			return list;
		}

//		get cluster by id
		@GetMapping("/clusterinfo/{clusterid}")
		public ResponseEntity<ClusterInfo> getClusterInfoByClusterId(@PathVariable int clusterid) {
			Optional<ClusterInfo> ent = clusterRepo.findById(clusterid);
			if (ent.isPresent()) {
				return new ResponseEntity<ClusterInfo>(ent.get(), HttpStatus.FOUND);
			} else {
				return new ResponseEntity<ClusterInfo>(HttpStatus.NOT_FOUND);
			}
		}

//		update cluster by id
		@PutMapping("/updatecluster/{clusterid}")
		public ResponseEntity<String> updateByid(@PathVariable Integer clusterid, @RequestBody ClusterInfo clusterinfo) {
			Optional<ClusterInfo> find = clusterRepo.findById(clusterid);
			if (find.isPresent()) {
				ClusterInfo update = find.get();

				update.setClustername(clusterinfo.getClustername());
				update.setMonitoringstatus(clusterinfo.getMonitoringstatus());

				clusterRepo.save(update);

				return ResponseEntity.ok("Detail Updated");
			} else {
				return ResponseEntity.ok("Detail Not Found");
			}

		}

//		Delete by cluster id
		@DeleteMapping("/deletecluster/{clusterid}")
		public ResponseEntity<String> deleteById1(@PathVariable Integer clusterid) {
			clusterRepo.deleteById(clusterid);

			return ResponseEntity.ok("Deleted");
		}
}
