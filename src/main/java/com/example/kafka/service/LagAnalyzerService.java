package com.example.kafka.service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.kafka.entity.KafkaEntity;
import com.example.kafka.mail.SendMailByGmail;
import com.example.kafka.repository.ElasticRepo;
import com.example.kafka.entity.ElasticEntity;
import com.example.kafka.repository.KafkaRepository;

@Service
public class LagAnalyzerService {
	Logger LOGGER = LoggerFactory.getLogger(LagAnalyzerService.class);

	@Autowired
	KafkaRepository kafkarepository;
	
	@Autowired
	SendMailByGmail sendmailbygmail;
	
	@Autowired
    private ElasticRepo elasticRepo;

	int id = 1;
	private final AdminClient adminClient;
	private final Producer<String,String> producer;
    private KafkaConsumer<String, String> kafkaConsumer;

    @Autowired
    public LagAnalyzerService(@Value("${monitor.kafka.bootstrap.config}") String bootstrapServerConfig) {
        adminClient = getAdminClient(bootstrapServerConfig);
        producer = getProducer(bootstrapServerConfig);
        kafkaConsumer = getKafkaConsumer(bootstrapServerConfig);
    }
    
    public LagAnalyzerService()
    {
    	adminClient = null;
    	producer = null;
    	kafkaConsumer = null;
    }
    
    private void runAfterObjectCreated()
    {
    	List<String> topic = kafkarepository.getAllTopicNames();
		for(int i=0;i<topic.size();i++)
		{
			KafkaEntity kafkaEntity = kafkarepository.findByTopicname(topic.get(i));
			kafkaEntity.setTimestamp(-1);
			kafkarepository.save(kafkaEntity);
		}
    }
    
	private AdminClient getAdminClient(String bootstrapServerConfig) {
		if (adminClient == null) {
			Properties config = new Properties();
			config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServerConfig);
			return AdminClient.create(config);
		} else
			return adminClient;
	}
	
	private Producer<String,String> getProducer(String bootstrapServerConfig)
	{
		if(producer == null)
		{
			Properties props = new Properties();
			  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServerConfig);
		      props.put("acks", "all"); 
		      props.put("retries", 0);	      
		      props.put("batch.size", 16384);		      
		      props.put("linger.ms", 1);	      
		      props.put("buffer.memory", 33554432);      
		      props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
		      props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
		      Producer<String, String> producer = new KafkaProducer
		         <String, String>(props);
		      return producer;
		}
		else
			return producer;
	}

	public void analyzeLag(String groupId) throws ExecutionException, InterruptedException {
		Map<TopicPartition, Long> consumerGrpOffsets = getConsumerGrpOffsets(groupId);
		Map<TopicPartition, Long> producerOffsets = getProducerOffsets(consumerGrpOffsets);
		Map<TopicPartition, Long> lags = computeLags(consumerGrpOffsets, producerOffsets);
		for (Map.Entry<TopicPartition, Long> lagEntry : lags.entrySet()) {
			String topic = lagEntry.getKey().topic();
			int partition = lagEntry.getKey().partition();
			Long lag = lagEntry.getValue();
			
			ElasticEntity elasticEntity = new ElasticEntity(id, topic, partition, lag);
			elasticRepo.save(elasticEntity);
			
			KafkaEntity kafkaEntity = kafkarepository.findByTopicname(topic);
			int minutes = Integer.parseInt(DateTimeFormatter.ofPattern("mm").format(LocalTime.now()));
			int noOfMinutes = (minutes - kafkaEntity.getTimestamp());
			if(lag>=kafkaEntity.getThreshold() && ( noOfMinutes == 30 || noOfMinutes == -30 || (kafkaEntity.getTimestamp() == -1)))
 			{
 				sendmailbygmail.sendMail(kafkaEntity,lag);
 				int timestamp = Integer.parseInt(DateTimeFormatter.ofPattern("mm").format(LocalTime.now()));
 				kafkaEntity.setTimestamp(timestamp);
 				kafkarepository.save(kafkaEntity);
 			}
			
			++id;
			
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

	private Map<TopicPartition, Long> getProducerOffsets(Map<TopicPartition, Long> consumerGrpOffset) {
		List<TopicPartition> topicPartitions = new LinkedList<>();
		for (Map.Entry<TopicPartition, Long> entry : consumerGrpOffset.entrySet()) {
			TopicPartition key = entry.getKey();
			topicPartitions.add(new TopicPartition(key.topic(), key.partition()));
		}
		return kafkaConsumer.endOffsets(topicPartitions);
	}

	private KafkaConsumer<String, String> getKafkaConsumer(String bootstrapServerConfig) {
		if (kafkaConsumer == null) {
			Properties properties = new Properties();
			properties.setProperty("group.id", "Apple");
			properties.setProperty("enable.auto.commit", "true");
			properties.setProperty("auto.commit.interval.ms", "1000");
			properties.setProperty("session.timeout.ms", "30000");
			properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServerConfig);
			properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
			properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
			return new KafkaConsumer<>(properties);
		} else
			return kafkaConsumer;

	}

	private Map<TopicPartition, Long> getConsumerGrpOffsets(String groupId)
			throws ExecutionException, InterruptedException {
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
	
	public void pData(String topicName,String clusterName) throws InterruptedException
	{
		producer.send(new ProducerRecord<String, String>( topicName,
	            Integer.toString(count++), Integer.toString(count++)));
	               System.out.println("Message sent successfully for " + topicName + " from " + clusterName + " cluster ");
	               Thread.sleep(1000);
	}
	
	public void cData(List<String> topics,String consumerName,String clusterName)
	{
		Properties properties = new Properties();
		properties.setProperty("group.id",consumerName);
		properties.setProperty("bootstrap.servers", "localhost:9092");
		properties.setProperty("enable.auto.commit", "true");
		properties.setProperty("auto.commit.interval.ms", "1000");
		properties.setProperty("session.timeout.ms", "30000");
		properties.setProperty("max.poll.interval.ms", "3000");
		properties.setProperty("max.poll.records", "500");
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		kafkaConsumer = new KafkaConsumer<>(properties);
		
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
