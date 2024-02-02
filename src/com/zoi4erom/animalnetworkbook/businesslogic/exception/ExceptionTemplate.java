package com.zoi4erom.animalnetworkbook.businesslogic.exception;

/**
 * Enum representing templates for error messages in the application.
 */
public enum ExceptionTemplate {
	EMPTY_FIELD_EXCEPTION("Field '%s' is required to be filled in!"),
	TOO_SHORT_LONG_EXCEPTION("Field '%s' must be longer than '%d' and shorter than '%d'!"),
	NON_LATIN_CHARACTERS_EXCEPTION("Field '%s' cannot contain Latin characters!"),
	WEAK_PASSWORD_EXCEPTION("Field '%s' cannot contain special characters or non-Latin characters!"),
	WRONG_ENDING_EXCEPTION("Field '%s' must end with '%s'!");

	private final String template;

	/**
	 * Constructor for the ExceptionTemplate enum.
	 *
	 * @param template The template for the error message.
	 */
	ExceptionTemplate(String template) {
		this.template = template;
	}

	/**
	 * Gets the template for the error message.
	 *
	 * @return The template for the error message.
	 */
	public String getTemplate() {
		return template;
	}
}
