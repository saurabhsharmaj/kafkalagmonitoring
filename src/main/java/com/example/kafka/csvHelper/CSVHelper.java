package com.example.kafka.csvHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import com.example.kafka.entity.KafkaEntity;

public class CSVHelper {
	public static String TYPE = "text/csv";
	static String[] header = {"consumergroup", "description", "emailid", "owner", "topicname", "threshold", "monitoringstatus", "timestamp", "clusterid"};
	
	public static boolean hasCSVFormat(MultipartFile file) {
		
		if(!TYPE.equals(file.getContentType())) {
			return false;
		}
		return true;
	}
	
	  public static List<KafkaEntity> csvToKafkaEntities(InputStream is) {
	    try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	        CSVParser csvParser = new CSVParser(fileReader,
	            CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

	      List<KafkaEntity> entities = new ArrayList<KafkaEntity>();

	      Iterable<CSVRecord> csvRecords = csvParser.getRecords();

	      for (CSVRecord csvRecord : csvRecords) {
	    	  KafkaEntity kafkaEntity = new KafkaEntity(
	    			  csvRecord.get("emailid"),
	    			  csvRecord.get("owner"),
	    			  csvRecord.get("description"),
	    			  csvRecord.get("topicname"),
	    			  csvRecord.get("consumergroup"),
	    			  Integer.parseInt(csvRecord.get("threshold")),
	    			  Integer.parseInt(csvRecord.get("monitoringstatus")),
	    			  Integer.parseInt(csvRecord.get("timestamp")),
	    			  Integer.parseInt(csvRecord.get("clusterid"))	    			  
	            );

	    	  entities.add(kafkaEntity);
	      }

	      return entities;
	    } 
	    catch (IOException e) {
	      throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
	    }
	  }
}