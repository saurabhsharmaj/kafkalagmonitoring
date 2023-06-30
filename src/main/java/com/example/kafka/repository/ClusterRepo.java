package com.example.kafka.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.kafka.entity.ClusterInfo;

@Repository
public interface ClusterRepo extends JpaRepository<ClusterInfo, Integer> {

	@Transactional
	@Query(value="SELECT c.clustername FROM cluster_info c LEFT JOIN kafkalagmonitoring k ON c.clusterid = k.clusterid WHERE k.topicname = :topic ",nativeQuery = true)	
	String clusterElastic(@Param("topic") String topic);
}
