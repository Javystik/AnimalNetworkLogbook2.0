package com.zoi4erom.animalnetworkbook.persistence.exception;

/**
 * An exception class for handling serialization-related errors.
 */
public class SerializationException extends RuntimeException {

	/**
	 * Constructs a SerializationException with the specified message and cause.
	 *
	 * @param message The detail message.
	 * @param cause   The cause of the exception.
	 */
	public SerializationException(String message, Throwable cause) {
		super(message, cause);
	}
}
