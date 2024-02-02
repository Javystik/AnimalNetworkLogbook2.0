package com.zoi4erom.animalnetworkbook.businesslogic.exception;

/**
 * Enum representing templates for error messages in the application.
 */
public enum ExceptionTemplate {
	EMPTY_FIELD_EXCEPTION("Поле '%s' обов'язкове для заповнення!"),
	TOO_SHORT_LONG_EXCEPTION("Поле '%s' повинно бути довше '%d' і коротше '%d'!"),
	NON_LATIN_CHARACTERS_EXCEPTION("Поле '%s' не може містити латинських символів!"),
	WEAK_PASSWORD_EXCEPTION("Поле '%s' не може містити спеціальних символів або латинських символів!"),
	WRONG_ENDING_EXCEPTION("Поле '%s' повинно закінчуватися на '%s'!");
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
