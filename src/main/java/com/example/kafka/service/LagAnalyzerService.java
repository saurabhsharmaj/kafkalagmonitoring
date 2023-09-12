package com.example.kafka.service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ListConsumerGroupOffsetsResult;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.kafka.common.MonitoringUtil;
import com.example.kafka.common.SendMailByGmail;
import com.example.kafka.entity.ElasticDocumentEntity;
import com.example.kafka.entity.TopicInfoEntity;
import com.example.kafka.repository.ClusterRepo;
import com.example.kafka.repository.ElasticRepo;
import com.example.kafka.repository.TopicInfoEntityRepository;

@Service
@Transactional
public class LagAnalyzerService {
	Logger LOGGER = LoggerFactory.getLogger(LagAnalyzerService.class);

	@Autowired
	TopicInfoEntityRepository kafkarepository;
	
	@Autowired
	ClusterRepo clusterRepo;
	
	@Autowired
	SendMailByGmail sendmailbygmail;
	
	@Autowired
    private ElasticRepo elasticRepo;
	
	int id = 1;
    
    public LagAnalyzerService(){}
    
    private void runAfterObjectCreated()
    {
    	List<TopicInfoEntity> topicData = kafkarepository.findAll();
    	
    	List<String> topic = topicData.stream().map(TopicInfoEntity::getTopicname).collect(Collectors.toList());
    	List<String> servers = kafkarepository.getAllBootstrapServers();
		for(int i=0;i<topic.size();i++)
		{
			TopicInfoEntity kafkaEntity = kafkarepository.FindTopicDetailsByTopicNameAndBootStrapServer(topic.get(i), servers.get(i));
			kafkaEntity.setTimestamp(-1);
			kafkarepository.save(kafkaEntity);
		}
    }
    
	private AdminClient getAdminClient(String bootstrapServer) {
			Properties config = new Properties();
			config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
			return AdminClient.create(config);
	}
	
	private KafkaConsumer<String, String> getKafkaConsumer(String consumerGroup,String bootstrapServer) {
			Properties properties = new Properties();
			properties.setProperty("group.id",consumerGroup);
			properties.setProperty("bootstrap.servers", bootstrapServer);
			properties.setProperty("enable.auto.commit", "true");
			properties.setProperty("auto.commit.interval.ms", "1000");
			properties.setProperty("session.timeout.ms", "30000");
			properties.setProperty("max.poll.interval.ms", "3000");
			properties.setProperty("max.poll.records", "500");
			properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
			properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
			return new KafkaConsumer<>(properties);
	}

	public void analyzeLag(String groupId,String bootstrapServer,String clusterName) throws ExecutionException, InterruptedException {
		Map<TopicPartition, Long> consumerGrpOffsets = getConsumerGrpOffsets(groupId,bootstrapServer);
		Map<TopicPartition, Long> producerOffsets = getProducerOffsets(consumerGrpOffsets,groupId,bootstrapServer);
		Map<TopicPartition, Long> lags = computeLags(consumerGrpOffsets, producerOffsets);
		for (Map.Entry<TopicPartition, Long> lagEntry : lags.entrySet()) {
			String topic = lagEntry.getKey().topic();
			int partition = lagEntry.getKey().partition();
			Long lag = lagEntry.getValue();
			String clustername = clusterRepo.clusterElastic(topic);
			ElasticDocumentEntity elasticEntity = new ElasticDocumentEntity(id, topic, partition, lag, clustername);
			elasticRepo.save(elasticEntity);
			
			TopicInfoEntity kafkaEntity = kafkarepository.FindTopicDetailsByTopicNameAndBootStrapServer(topic, bootstrapServer);
			System.out.println(kafkaEntity.getTopicname()+" "+kafkaEntity.getTimestamp());
			int minutes = Integer.parseInt(DateTimeFormatter.ofPattern("mm").format(LocalTime.now()));

			int noOfMinutes = (minutes - kafkaEntity.getTimestamp());
			
			if(lag>=kafkaEntity.getThreshold() && (noOfMinutes == 3 || noOfMinutes != 0 || (kafkaEntity.getTimestamp() == -1)))
 			{
 				sendmailbygmail.sendMail(kafkaEntity,lag,clusterName);
 				int timestamp = Integer.parseInt(DateTimeFormatter.ofPattern("mm").format(LocalTime.now()));
 				kafkaEntity.setTimestamp(timestamp);
 				kafkarepository.save(kafkaEntity);
 			}			
			++id;		
			clustername = "";
			System.err.println("Time="+ MonitoringUtil.time()+" | Lag for topic = "+topic+", partition = "+partition+", groupId = "+groupId+" is "+lag );
		}
	}

	private Map<TopicPartition, Long> computeLags(Map<TopicPartition, Long> consumerGrpOffsets,
			Map<TopicPartition, Long> producerOffsets) {
		Map<TopicPartition, Long> lags = new HashMap<>();
		for (Map.Entry<TopicPartition, Long> entry : consumerGrpOffsets.entrySet()) {
			Long producerOffset = producerOffsets.get(entry.getKey());
			Long consumerOffset = consumerGrpOffsets.get(entry.getKey());
			long lag = Math.abs(producerOffset - consumerOffset);
			lags.putIfAbsent(entry.getKey(), lag);
		}
		return lags;
	}

	private Map<TopicPartition, Long> getProducerOffsets(Map<TopicPartition, Long> consumerGrpOffset,String groupId,String bootstrapServer) {
		List<TopicPartition> topicPartitions = new LinkedList<>();
		for (Map.Entry<TopicPartition, Long> entry : consumerGrpOffset.entrySet()) {
			TopicPartition key = entry.getKey();
			topicPartitions.add(new TopicPartition(key.topic(), key.partition()));
		}
		KafkaConsumer<String, String> kafkaConsumer = getKafkaConsumer(groupId,bootstrapServer);
		return kafkaConsumer.endOffsets(topicPartitions);
	}

	private Map<TopicPartition, Long> getConsumerGrpOffsets(String groupId,String bootstrapServer)
			throws ExecutionException, InterruptedException {
		AdminClient adminClient = getAdminClient(bootstrapServer);
		ListConsumerGroupOffsetsResult info = adminClient.listConsumerGroupOffsets(groupId);
		Map<TopicPartition, OffsetAndMetadata> topicPartitionOffsetAndMetadataMap = info.partitionsToOffsetAndMetadata()
				.get();

		Map<TopicPartition, Long> groupOffset = new HashMap<>();
		for (Map.Entry<TopicPartition, OffsetAndMetadata> entry : topicPartitionOffsetAndMetadataMap.entrySet()) {
			TopicPartition key = entry.getKey();
			OffsetAndMetadata metadata = entry.getValue();
			groupOffset.putIfAbsent(new TopicPartition(key.topic(), key.partition()), metadata.offset());
		}
		return groupOffset;
	}
	
	int count=0;
	
	public void pData(String topicName,String clusterName,String bootstrapServer) throws InterruptedException
	{
		Producer<String,String> producer;
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
	    props.put("acks", "all"); 
	    props.put("retries", 0);	      
	    props.put("batch.size", 16384);		      
	    props.put("linger.ms", 1);	      
	    props.put("buffer.memory", 33554432);      
	    props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
	    props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
	    producer = new KafkaProducer<String, String>(props);
		producer.send(new ProducerRecord<String, String>( topicName,
	            Integer.toString(count++), Integer.toString(count++)));
	               System.out.println("Message sent successfully for " + topicName + " from " + clusterName + " cluster ");
	               Thread.sleep(1000);
	}
	
	public void cData(List<String> topics,String consumerName,String clusterName,String bootstrapServer)
	{
		KafkaConsumer<String, String> kafkaConsumer = getKafkaConsumer(consumerName,bootstrapServer);
		kafkaConsumer.subscribe(topics);	
		System.out.println("Subscribed to topic " + topics + " from " + clusterName + " cluster for " + consumerName + " consumer ");
	    try
	    {
	    	ConsumerRecords<String, String> records = kafkaConsumer.poll(5000);
	    	if(!records.isEmpty())
	    	{
	    		for (ConsumerRecord<String, String> record : records)
	    			System.err.printf("offset = %d, key = %s, value = %s\n",record.offset(), record.key(), record.value());
	    	}
	    	kafkaConsumer.commitSync();
	    }catch (Exception e) {
	    	LOGGER.error("Kafka Consumer thread {} Exception while polling Kafka.", hashCode(), e);
        }
	}
}
