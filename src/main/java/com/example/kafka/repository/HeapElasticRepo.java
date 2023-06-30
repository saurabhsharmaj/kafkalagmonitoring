package com.example.kafka.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.example.kafka.entity.HeapEntity;

//@EnableElasticsearchRepositories
public interface HeapElasticRepo extends ElasticsearchRepository<HeapEntity, Integer>{

}
