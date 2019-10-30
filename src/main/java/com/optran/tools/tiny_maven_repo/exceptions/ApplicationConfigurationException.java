package com.optran.tools.tiny_maven_repo.exceptions;

public class ApplicationConfigurationException extends Exception {
	private static final long serialVersionUID = -6331880735128614231L;

	public ApplicationConfigurationException() {
		super("Invalid Application configuration!");
	}

	public ApplicationConfigurationException(String message) {
		super(message);
	}
	
	public ApplicationConfigurationException(String message, Exception e) {
		super(message, e);
	}
}
