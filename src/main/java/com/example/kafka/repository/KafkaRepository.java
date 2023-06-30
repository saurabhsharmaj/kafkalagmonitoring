package com.example.kafka.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.kafka.entity.KafkaEntity;

@Repository
public interface KafkaRepository extends JpaRepository<KafkaEntity, Integer> {

	@Query(value = "select k.* FROM kafkalagmonitoring k left join cluster_info c  on k.clusterid=c.clusterid where k.topicname = :topic  AND c.bootstrap_servers = :bootstrapServer ;", nativeQuery = true)
	KafkaEntity FindTopicDetailsByTopicNameAndBootStrapServer(@Param("topic") String topic, @Param("bootstrapServer") String bootstrapServer);

	@Query(value = "select k.topicname FROM kafkalagmonitoring k left join cluster_info c on k.clusterid = c.clusterid where k.monitoringstatus = 1 AND c.monitoringstatus =1 ;", nativeQuery = true)
	List<String> getAllTopicNamesByMonitoringStatus();
	
	@Query(value = "select topicname  FROM kafkalagmonitoring ;", nativeQuery = true)
	List<String> getAllTopicNames();
	
	@Query(value = "select k.consumergroup FROM kafkalagmonitoring k left join cluster_info c on k.clusterid = c.clusterid where k.monitoringstatus = 1 AND c.monitoringstatus =1 ;", nativeQuery = true)
	List<String> getAllGroupNamesByMonitoringStatus();
	
	void save(KafkaRepository kafkarepository);
	
	@Query(value = "select c.clustername FROM kafkalagmonitoring k left join cluster_info c on k.clusterid = c.clusterid where k.monitoringstatus = 1 AND c.monitoringstatus =1 ;", nativeQuery = true)
	List<String> getAllClusterNamesByMonitoringStatus();
	
	@Query(value = "SELECT ci.bootstrap_servers FROM cluster_info ci LEFT JOIN kafkalagmonitoring klm ON ci.clusterid = klm.clusterid where klm.monitoringstatus = 1 AND ci.monitoringstatus =1 order by groupid ;", nativeQuery = true)
	List<String> getAllBootstrapServersByMonitoringStatus();
	
	@Query(value = "SELECT ci.bootstrap_servers FROM kafkalagmonitoring klm LEFT JOIN cluster_info ci ON klm.clusterid = ci.clusterid order by groupid ;", nativeQuery = true)
	List<String> getAllBootstrapServers();
	
	@Query(value="SELECT k.* FROM kafkalagmonitoring k ;",nativeQuery=true)
	List<KafkaEntity> getAllData();

	List<KafkaEntity> findByClusterid(int clusterid);

	@Modifying
	@Query(value="delete from kafkalagmonitoring k where k.clusterid = :groupid ;",nativeQuery = true)
	 void deleteByClusterid(@Param("groupid") int groupid);

	KafkaEntity findByTopicname(String topicname);

//	@Query(value="SELECT groupid from kafkalagmonitoring k where k.topicname = :topic;",nativeQuery = true)
//	 int findId(@Param("topic") String topic);

}
