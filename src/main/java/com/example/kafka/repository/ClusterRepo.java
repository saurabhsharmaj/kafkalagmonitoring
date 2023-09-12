package com.example.kafka.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.kafka.entity.ClusterInfo;

@Repository
public interface ClusterRepo extends JpaRepository<ClusterInfo, Integer> {

	@Transactional
	@Query(value="SELECT c.cluster_name FROM cluster_info c LEFT JOIN topic_info k ON c.cluster_id = k.clusterid WHERE k.topicname = :topic ",nativeQuery = true)	
	String clusterElastic(@Param("topic") String topic);
		
	@Query(value = "SELECT c.cluster_name FROM cluster_info c left join topic_info k on c.cluster_id = k.clusterid WHERE c.cluster_id = k.clusterid;",nativeQuery=true)
	List<String> getKafkaClusterName();
}
