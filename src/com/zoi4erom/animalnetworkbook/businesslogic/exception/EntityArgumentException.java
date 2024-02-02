package com.zoi4erom.animalnetworkbook.businesslogic.exception;

import java.util.List;

/**
 * Exception thrown when an entity receives invalid arguments.
 */
public class EntityArgumentException extends IllegalArgumentException {
	private final List<String> errors;

	/**
	 * Constructs an EntityArgumentException with a list of errors.
	 *
	 * @param errors The list of error messages.
	 */
	public EntityArgumentException(List<String> errors) {
		this.errors = errors;
	}

	/**
	 * Gets the list of error messages.
	 *
	 * @return The list of error messages.
	 */
	public List<String> getErrors() {
		return errors;
	}
}
