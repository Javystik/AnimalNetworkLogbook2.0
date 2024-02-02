package com.zoi4erom.animalnetworkbook.persistence.exception;

/**
 * An exception class for handling deserialization-related errors.
 */
public class DeserializationException extends RuntimeException {

	/**
	 * Constructs a DeserializationException with the specified message and cause.
	 *
	 * @param message The detail message.
	 * @param cause   The cause of the exception.
	 */
	public DeserializationException(String message, Throwable cause) {
		super(message, cause);
	}
}
