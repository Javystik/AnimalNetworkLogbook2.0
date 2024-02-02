package com.zoi4erom.animalnetworkbook.persistence.jsonhandler;

/**
 * Enum representing paths to various JSON files used in the application.
 */
public enum JsonPaths {
	USER("users.json"),
	ANIMAL("animal.json"),
	SHELTERS("shelters.json"),
	REQUEST("request.json");

	private final String path;

	/**
	 * Constructor for the JsonPaths enum.
	 *
	 * @param path The relative path to the JSON file.
	 */
	JsonPaths(String path) {
		this.path = path;
	}

	/**
	 * Gets the full path to the JSON file, including the "Data/" prefix.
	 *
	 * @return The full path to the JSON file.
	 */
	public String getPath() {
		return "Data/" + path;
	}
}
