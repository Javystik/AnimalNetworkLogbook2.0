package com.zoi4erom.animalnetworkbook.businesslogic.exception;

/**
 * Exception thrown when an error occurs during the sign-up process.
 */
public class SignUpException extends RuntimeException {

	/**
	 * Constructs a SignUpException with a specific error message.
	 *
	 * @param message The error message.
	 */
	public SignUpException(String message) {
		super(message);
	}
}
