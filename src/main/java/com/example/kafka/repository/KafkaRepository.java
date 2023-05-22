package com.example.kafka.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.kafka.entity.KafkaEntity;

@Repository
public interface KafkaRepository extends JpaRepository<KafkaEntity, Integer> {

	KafkaEntity findByTopicname(String topicname);

	@Query(value = "select k.topicname FROM kafkalagmonitoring k left join cluster_info c on k.clusterid = c.clusterid where k.monitoringstatus = 1 & c.monitoringstatus =1;", nativeQuery = true)
	List<String> getAllTopicName();
	
	@Query(value = "select topicname  FROM kafkalagmonitoring;", nativeQuery = true)
	List<String> getAllTopicNames();
	
	@Query(value = "select k.consumergroup FROM kafkalagmonitoring k left join cluster_info c on k.clusterid = c.clusterid where k.monitoringstatus = 1 & c.monitoringstatus =1;", nativeQuery = true)
	List<String> getAllGroupName();
	
	void save(KafkaRepository kafkarepository);
	
	@Query(value = "select c.clustername FROM kafkalagmonitoring k left join cluster_info c on k.clusterid = c.clusterid where k.monitoringstatus = 1 & c.monitoringstatus =1;", nativeQuery = true)
	List<String> getAllClusterName();
}
