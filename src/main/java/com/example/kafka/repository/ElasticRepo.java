package com.example.kafka.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.example.kafka.entity.ElasticDocumentEntity;


//@EnableElasticsearchRepositories
public interface ElasticRepo extends ElasticsearchRepository<ElasticDocumentEntity, Integer>{

}
