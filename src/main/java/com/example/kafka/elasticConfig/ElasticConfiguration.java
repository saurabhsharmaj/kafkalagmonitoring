package com.example.kafka.elasticConfig;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.example.kafka.repo.ElasticRepo")
@ComponentScan(basePackages = { "io.pratik.elasticsearch" })
public class ElasticConfiguration {

	@SuppressWarnings("deprecation")
	@Bean
	public RestHighLevelClient elasticsearchClient() {

		final ClientConfiguration clientConfiguration = ClientConfiguration.builder().connectedTo("localhost:9200")
				.build();

		return RestClients.create(clientConfiguration).rest();
	}
}
