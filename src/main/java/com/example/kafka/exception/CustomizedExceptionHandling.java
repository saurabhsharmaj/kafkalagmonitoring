package com.example.kafka.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomizedExceptionHandling extends ResponseEntityExceptionHandler
{
	 @ExceptionHandler(FillDataException.class)
	 public ResponseEntity<Object> handleExceptions( FillDataException exception, WebRequest webRequest) 
		{
	        ExceptionResponse response = new ExceptionResponse();
	        response.setDateTime(LocalDateTime.now());
	        response.setMessage("Please provide correct data in Request Body.");
	        response.setError(HttpStatus.BAD_REQUEST);
	        response.setStatus(HttpStatus.BAD_REQUEST.value());
	        ResponseEntity<Object> entity = new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	        return entity;
	    }
	 
	 @ExceptionHandler(ResourceNotFoundException.class)
	 public ResponseEntity<Object> handleExceptions(ResourceNotFoundException exception,WebRequest webRequest)
	 {
		 ExceptionResponse response = new ExceptionResponse();
	        response.setDateTime(LocalDateTime.now());
	        response.setMessage("Kafka Entity not found for this id !!");
	        response.setError(HttpStatus.NOT_FOUND);
	        response.setStatus(HttpStatus.NOT_FOUND.value());
	        ResponseEntity<Object> entity = new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
	        return entity;
	 }
}
