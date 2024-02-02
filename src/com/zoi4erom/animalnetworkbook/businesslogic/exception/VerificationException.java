package com.zoi4erom.animalnetworkbook.businesslogic.exception;

/**
 * Exception thrown when an error occurs during the verification process.
 */
public class VerificationException extends RuntimeException {

	/**
	 * Constructs a VerificationException with a specific error message.
	 *
	 * @param message The error message.
	 */
	public VerificationException(String message) {
		super(message);
	}
}
