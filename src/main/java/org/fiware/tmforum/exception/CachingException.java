package org.fiware.tmforum.exception;

public class CachingException extends RuntimeException {

	public CachingException(String message) {
		super(message);
	}

	public CachingException(String message, Throwable cause) {
		super(message, cause);
	}
}
