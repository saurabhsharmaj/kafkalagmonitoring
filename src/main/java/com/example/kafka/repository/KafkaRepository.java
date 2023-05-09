package com.example.kafka.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kafka.entity.KafkaEntity;

public interface KafkaRepository extends JpaRepository<KafkaEntity, Integer>{

}