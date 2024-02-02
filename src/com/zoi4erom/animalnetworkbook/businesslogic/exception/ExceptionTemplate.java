package com.zoi4erom.animalnetworkbook.businesslogic.exception;

public enum ExceptionTemplate {
	EMPTY_FIELD_EXCEPTION("Поле '%s' обов'язкове до заповнення!"),
	TOO_SHORT_LONG_EXCEPTION("Поле '%s' має бути більше за розмір '%d' і менше за розмір '%d'!"),
	NON_LATIN_CHARACTERS_EXCEPTION("Поле '%s' не може містити латинські літери!"),
	WEAK_PASSWORD_EXCEPTION("Поле '%s' не може містити спецсимволи чи не латинські літери!"),
	WRONG_ENDING_EXCEPTION("Поле '%s' повине закінчуватись на '%s' !");
	private final String template;
	ExceptionTemplate(String template) {
		this.template = template;
	}
	public String getTemplate() {
		return template;
	}
}