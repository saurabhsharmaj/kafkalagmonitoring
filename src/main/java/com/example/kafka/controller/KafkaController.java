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

import com.example.kafka.csvHelper.CSVHelper;
import com.example.kafka.entity.ClusterInfo;
import com.example.kafka.entity.KafkaEntity;
import com.example.kafka.exception.FillDataException;
import com.example.kafka.exception.ResourceNotFoundException;
import com.example.kafka.kafkaEntityService.KafkaEntityService;
import com.example.kafka.message.ResponseMessage;
import com.example.kafka.repository.ClusterRepo;
import com.example.kafka.repository.KafkaRepository;

@CrossOrigin("http://localhost:3000/")
@RequestMapping("/api")
@RestController
public class KafkaController {
	
	@Autowired
	private KafkaRepository kafkarepository;
	
	@Autowired
	private ClusterRepo clusterRepo;
	
	@Autowired
	private KafkaEntityService kafkaEntityService; 
	
	//getting all clusterdetails
	@GetMapping("/getclusterdetails")
	public List<ClusterInfo> getclusterdetails(){
		return clusterRepo.findAll();
	}
	
	
	//getting all topicdetails
	@GetMapping("/gettopicdetails")
	public List<KafkaEntity> gettopicdetails()
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
	
	//getting information based on clusterid
	@GetMapping("/cluster/{clusterid}")
	public ClusterInfo getClusterById(@PathVariable int clusterid) {
		return clusterRepo.findById(clusterid).get();
	}
	
	@GetMapping("/kafkadetails/{groupid}")
	public KafkaEntity getKafkaById(@PathVariable int groupid) {
		return kafkarepository.findById(groupid).get();
	}
	
	//getting topic details according clusterid
	@GetMapping("/gettopicdata/{clusterid}")
	public List<KafkaEntity> getTopicCluster(@PathVariable int clusterid) {
		List<KafkaEntity> list = new ArrayList<>();
		list = kafkarepository.findByClusterid(clusterid);
		return list;		
	}
	
	@GetMapping("/kafka/clustername")
	public List<String> getKafkaClustername(){
		List<String> list = clusterRepo.getKafkaClusterName();
		return list;
		
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
	
	
	@PostMapping("/uploadfile")
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam MultipartFile file){
		String message = "";
		
		if(CSVHelper.hasCSVFormat(file)) {
			try {
				kafkaEntityService.save(file);
				message = "Uploaded the file successfully: " + file.getOriginalFilename();
				 return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
			}
			catch (Exception e) {
				message = "Could not upload the file: " + file.getOriginalFilename() + "!";
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
			}
		}
		message = "Please upload a csv file!";
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
	  }	
	
	//adding details for cluster
	@PostMapping("/postclusterdetails")
	public String postData(@RequestBody ClusterInfo clusterInfo) {
		clusterRepo.save(clusterInfo);
		return "Data Successfully added";
	}
	
	
	//adding details for topic
	@PostMapping("/post/kafka")
	public ResponseEntity<String> createLagMonitoring(@RequestBody KafkaEntity entities)
	{
		if(entities.getConsumergroup().isEmpty()||entities.getDescription().isEmpty()||entities.getEmailid().isEmpty()||entities.getOwner().isEmpty()
				||entities.getTopicname().isEmpty()) {

			throw new FillDataException();}
		else
		{	
			kafkarepository.save(entities);
			return ResponseEntity.ok("Data saved in DB !!");
		}
	}
	
	//update details for cluster
	@PutMapping("/updateclusterdetails")
	public ResponseEntity<String> updateByid(@RequestBody ClusterInfo clusterinfo) {
		Optional<ClusterInfo> find = clusterRepo.findById(clusterinfo.getClusterid());
		if (find.isPresent()) {
			ClusterInfo update = find.get();

			update.setClustername(clusterinfo.getClustername());
			update.setMonitoringstatus(clusterinfo.getMonitoringstatus());
			update.setZoo_logs_dir(clusterinfo.getZoo_logs_dir());
			update.setBootstrap_servers(clusterinfo.getBootstrap_servers());
			update.setBroker_logs_dir(clusterinfo.getBroker_logs_dir());
			update.setZookeeper_servers(clusterinfo.getZookeeper_servers());

			clusterRepo.save(update);

			return ResponseEntity.ok("Detail Updated");
		} else {
			return ResponseEntity.ok("Detail Not Found");
		}

	}

	@PutMapping("/kafkadetailsupdate")
	public String updateByid(@RequestBody KafkaEntity entities)
	{
		Optional<KafkaEntity> find = kafkarepository.findById(entities.getGroupid());
		if(find.isPresent())
		{
			KafkaEntity update = find.get();
			
			update.setOwner(entities.getOwner());
			update.setEmailid(entities.getEmailid());
			update.setDescription(entities.getDescription());
			update.setTopicname(entities.getTopicname());
			update.setConsumergroup(entities.getConsumergroup());
			update.setThreshold(entities.getThreshold());
			update.setMonitoringstatus(entities.getMonitoringstatus());
			update.setClusterid(entities.getClusterid());
			
			kafkarepository.save(update);
			
			return "Detail Updated";
		}
		else
		{
			throw new ResourceNotFoundException();
		}
						
	}
	
	//delete cluster with related topic
	@DeleteMapping("/clusterdelete/{id}")
	@Transactional
	public String deleteById(@PathVariable int id)
	{
		if(true)
		{
			kafkarepository.deleteByClusterid(id)
;
			clusterRepo.deleteById(id)
;
			return "Data Deleted";
		}
		else
			throw new ResourceNotFoundException();
	}
	
	//delete topic by id
	@DeleteMapping("/deletetopic/{id}")
	@Transactional
	public String deleteByGroupId(@PathVariable int id)
	{
		if(true)
		{
			kafkarepository.deleteById(id)
;
			return "Data Deleted";
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

	
	@GetMapping("/getclustername")
	public List<String> getClusterName() {
		List<String> list1 = clusterRepo.getAllClusterNameReact();
		return list1;
	}
		
}
