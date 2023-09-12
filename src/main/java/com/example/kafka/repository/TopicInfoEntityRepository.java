package com.example.kafka.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.kafka.entity.TopicInfoEntity;



@Repository
public interface TopicInfoEntityRepository extends JpaRepository<TopicInfoEntity, Integer> {

	@Query(value = "select k.* FROM topic_info k left join cluster_info c  on k.clusterid=c.cluster_id where k.topicname = :topic  AND c.bootstrap_servers = :bootstrapServer;", nativeQuery = true)
	TopicInfoEntity FindTopicDetailsByTopicNameAndBootStrapServer(@Param("topic") String topic, @Param("bootstrapServer") String bootstrapServer);

	@Query(value = "select k.topicname FROM topic_info k left join cluster_info c on k.clusterid = c.cluster_id where k.monitoringstatus = 1 AND c.monitoring_status =1 ;", nativeQuery = true)
	List<String> getAllTopicNamesByMonitoringStatus();
	
	@Query(value = "select k.consumergroup FROM topic_info k left join cluster_info c on k.clusterid = c.cluster_id where k.monitoringstatus = 1 AND c.monitoring_status =1 ;", nativeQuery = true)
	List<String> getAllGroupNamesByMonitoringStatus();
	
	void save(TopicInfoEntityRepository kafkarepository);
	
	@Query(value = "select c.cluster_name FROM topic_info k left join cluster_info c on k.clusterid = c.cluster_id where k.monitoringstatus = 1 AND c.monitoring_status =1 ;", nativeQuery = true)
	List<String> getAllClusterNamesByMonitoringStatus();
	
	@Query(value = "SELECT ci.bootstrap_servers FROM cluster_info ci LEFT JOIN topic_info klm ON ci.cluster_id = klm.clusterid where klm.monitoringstatus = 1 AND ci.monitoring_status =1 order by groupid ;", nativeQuery = true)
	List<String> getAllBootstrapServersByMonitoringStatus();
	
	@Query(value = "SELECT ci.bootstrap_servers FROM topic_info klm LEFT JOIN cluster_info ci ON klm.clusterid = ci.cluster_id order by groupid;", nativeQuery = true)
	List<String> getAllBootstrapServers();
	
	List<TopicInfoEntity> findByClusterId(int clusterid);

	@Modifying
	@Query(value="delete from topic_info k where k.clusterid = :groupid ;",nativeQuery = true)
	 void deleteByClusterid(@Param("groupid") int groupid);

//	TopicInfoEntity findByTopicname(String topicname);

	@Query(value="SELECT groupid from topic_info k where k.topicname = :topic;",nativeQuery = true)
	 int findId(@Param("topic") String topic);

//	String findByTopicname(String topicname);

}
