package org.fiware.tmforum.exception;

public class NonExistentReferenceException extends RuntimeException {

	public NonExistentReferenceException(String message) {
		super(message);
	}

	public NonExistentReferenceException(String message, Throwable cause) {
		super(message, cause);
	}
}
