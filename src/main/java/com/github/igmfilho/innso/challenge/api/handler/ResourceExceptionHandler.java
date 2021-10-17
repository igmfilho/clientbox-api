package com.github.igmfilho.innso.challenge.api.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ResourceExceptionHandler {

	@Autowired
	private MessageSource messageSource;
	
	@ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundExceptionHandler(HttpServletRequest req, final ResourceNotFoundException ex){
		String message = getMessage(ex.getMessageIndex());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

	@ExceptionHandler(ResourceInvalidException.class)
    public ResponseEntity<?> resourceInvalidExceptionHandler(HttpServletRequest req, final ResourceInvalidException ex){
		String message = getMessage(ex.getMessageIndex());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

	@ExceptionHandler(Exception.class)
    public ResponseEntity<?> exceptionHandler(){
        String message = getMessage("error.unexpected");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }

	private String getMessage(final String key) {
       return messageSource.getMessage(key, null, null);
    }
}