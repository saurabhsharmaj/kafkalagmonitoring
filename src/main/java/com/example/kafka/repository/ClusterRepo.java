package com.example.kafka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.kafka.entity.ClusterInfo;

@Repository
public interface ClusterRepo extends JpaRepository<ClusterInfo, Integer> {

}
