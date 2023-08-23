package com.example.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.example.kafka.cmd.CommandPromptApplication;
@SpringBootApplication
@EnableScheduling
public class KafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaApplication.class, args);
	}

	@Bean(initMethod="executeCommandPeriodically")
	public CommandPromptApplication getFunnyBean1() {
        return new CommandPromptApplication();
    }
	
}
