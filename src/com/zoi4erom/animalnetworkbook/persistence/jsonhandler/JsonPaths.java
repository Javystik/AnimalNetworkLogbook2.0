package com.zoi4erom.animalnetworkbook.persistence.jsonhandler;

public enum JsonPaths {
	USER("users.json"),
	ANIMAL("animal.json"),
	SHELTERS("shelters.json"),
	REQUEST("request.json");
	private final String path;
	JsonPaths(String path) {
		this.path = path;
	}
	public String getPath() {
		return "Data/" + path;
	}
}
