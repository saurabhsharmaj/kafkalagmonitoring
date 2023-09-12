package com.example.kafka.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kafka.entity.ClusterInfo;
import com.example.kafka.exception.ResourceNotFoundException;
import com.example.kafka.repository.ClusterRepo;
import com.example.kafka.repository.TopicInfoEntityRepository;

@CrossOrigin("http://localhost:3000/")
@RestController
@RequestMapping("/api")
public class ClusterInfoController {

	@Autowired
	private TopicInfoEntityRepository topicInfoEntityRepository;

	@Autowired
	private ClusterRepo clusterRepo;

	// getting all clusterdetails
	@GetMapping("/getclusterdetails")
	public List<ClusterInfo> getclusterdetails() {
		return clusterRepo.findAll();
	}

	// getting information based on clusterid
	@GetMapping("/cluster/{clusterid}")
	public ClusterInfo getClusterById(@PathVariable int clusterid) {
		return clusterRepo.findById(clusterid).get();
	}

	@GetMapping("/kafka/clustername")
	public List<String> getKafkaClustername() {
		List<String> list = clusterRepo.getKafkaClusterName();
		return list;

	}

	// adding details for cluster
	@PostMapping("/postclusterdetails")
	public String postData(@RequestBody ClusterInfo clusterInfo) {
		clusterRepo.save(clusterInfo);
		return "Data Successfully added";
	}

	// update details for cluster
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

	// delete cluster with related topic
	@SuppressWarnings("unused")
	@DeleteMapping("/clusterdelete/{id}")
	@Transactional
	public String deleteById(@PathVariable int id) {
		if (true) {
			topicInfoEntityRepository.deleteByClusterid(id);
			clusterRepo.deleteById(id);
			return "Data Deleted";
		} else
			throw new ResourceNotFoundException();
	}

	@GetMapping("/getclustername")
	public List<String> getClusterName() {
		List<ClusterInfo> clusterData = clusterRepo.findAll();
		List<String> list1 = clusterData.stream().map(ClusterInfo::getClustername).collect(Collectors.toList());
		return list1;
	}
}
