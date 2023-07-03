package com.example.kafka;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
	
//	@Bean(initMethod="runAfterObjectCreated")
//    public LagAnalyzerService getFunnyBean() {
//        return new LagAnalyzerService();
//    }
	
	@Controller 
	public class HomeContoller {
		 // inject via application.properties
	    @Value("${welcome.message}")
	    private String message;

	    private List<String> tasks = Arrays.asList("a", "b", "c", "d", "e", "f", "g");

	    @GetMapping("/")
	    public String main(Model model) {
	        model.addAttribute("message", message);
	        model.addAttribute("tasks", tasks);

	        return "index.html"; //view
	    }
	}
	
	
}
