package com.github.igmfilho.innso.challenge.api.handler;

public class ResourceInvalidException extends RuntimeException {

private static final long serialVersionUID = 1L;
	
	private String messageIndex = "error.resource.invalid";
	
	public ResourceInvalidException() {
		super();
	}
	
	public ResourceInvalidException(String message){
        super(message);
    }
	
	public String getMessageIndex() {
		return messageIndex;
	}
}