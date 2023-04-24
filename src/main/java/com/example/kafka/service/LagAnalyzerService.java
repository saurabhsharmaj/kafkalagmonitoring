package com.example.kafka.service;

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
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LagAnalyzerService {
	Logger LOGGER = LoggerFactory.getLogger(LagAnalyzerService.class);

	

	private final AdminClient adminClient;
    private final KafkaConsumer<String, String> kafkaConsumer;

    @Autowired
    public LagAnalyzerService(@Value("${monitor.kafka.bootstrap.config}") String bootstrapServerConfig) {
        adminClient = getAdminClient(bootstrapServerConfig);
        kafkaConsumer = getKafkaConsumer(bootstrapServerConfig);
    }
    

	private AdminClient getAdminClient(String bootstrapServerConfig) {
		if (adminClient == null) {
			Properties config = new Properties();
			config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServerConfig);
			return AdminClient.create(config);
		} else
			return adminClient;
	}

	public void analyzeLag(String groupId) throws ExecutionException, InterruptedException {
		Map<TopicPartition, Long> consumerGrpOffsets = getConsumerGrpOffsets(groupId);
		Map<TopicPartition, Long> producerOffsets = getProducerOffsets(consumerGrpOffsets);
		Map<TopicPartition, Long> lags = computeLags(consumerGrpOffsets, producerOffsets);
		for (Map.Entry<TopicPartition, Long> lagEntry : lags.entrySet()) {
			String topic = lagEntry.getKey().topic();
			int partition = lagEntry.getKey().partition();
			Long lag = lagEntry.getValue();
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
}
