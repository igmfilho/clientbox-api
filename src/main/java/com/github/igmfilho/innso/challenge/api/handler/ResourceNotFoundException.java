package com.github.igmfilho.innso.challenge.api.handler;

public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String messageIndex = "error.resource.notfound";
	
	public ResourceNotFoundException() {
		super();
	}
	
	public ResourceNotFoundException(String message){
        super(message);
    }
	
	public String getMessageIndex() {
		return messageIndex;
	}
}