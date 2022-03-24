package org.fiware.tmforum.exception;

public class NonExistentReferenceException extends RuntimeException {

	private final String referencedId;

	public NonExistentReferenceException(String message, String referencedId) {
		super(message);
		this.referencedId = referencedId;
	}

	public NonExistentReferenceException(String message, Throwable cause, String referencedId) {
		super(message, cause);
		this.referencedId = referencedId;
	}
}
