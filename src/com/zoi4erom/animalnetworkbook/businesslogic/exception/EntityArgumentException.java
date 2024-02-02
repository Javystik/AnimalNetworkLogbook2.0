package com.zoi4erom.animalnetworkbook.businesslogic.exception;

import java.util.List;

public class EntityArgumentException extends IllegalArgumentException{
	private final List<String> errors;
	public EntityArgumentException(List<String> errors) {
		this.errors = errors;
	}
	public List<String> getErrors() {
		return errors;
	}
}