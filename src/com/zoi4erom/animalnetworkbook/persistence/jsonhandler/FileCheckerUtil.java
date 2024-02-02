package com.zoi4erom.animalnetworkbook.persistence.jsonhandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for checking and creating directories and files needed for the application.
 */
public class FileCheckerUtil {

	/**
	 * Checks and creates necessary directories and files for the application.
	 */
	public static void checkAndCreateDirectoriesAndFiles() {
		try {
			// Check and create directories
			checkAndCreateDirectory(JsonPaths.USER.getPath());
			checkAndCreateDirectory("Data/Reports");

			// Check and create files
			for (JsonPaths jsonPath : JsonPaths.values()) {
				checkAndCreateFile(jsonPath.getPath());
			}
		} catch (IOException e) {
			e.printStackTrace(); // It's usually better to log exceptions rather than printing the stack trace
		}
	}

	/**
	 * Checks and creates a directory if it doesn't exist.
	 *
	 * @param directoryPath The path of the directory to check and create.
	 * @throws IOException If an I/O error occurs during directory creation.
	 */
	private static void checkAndCreateDirectory(String directoryPath) throws IOException {
		Path path = Paths.get(directoryPath);
		Path parentDir = path.getParent();

		if (parentDir != null && !Files.exists(parentDir)) {
			try {
				Files.createDirectories(parentDir);
			} catch (IOException e) {
				throw new IOException("Error creating directory: " + parentDir, e);
			}
		}
	}

	/**
	 * Checks and creates a file if it doesn't exist, initializing it with an empty array.
	 *
	 * @param filePath The path of the file to check and create.
	 * @throws IOException If an I/O error occurs during file creation.
	 */
	private static void checkAndCreateFile(String filePath) throws IOException {
		Path path = Paths.get(filePath);
		if (!Files.exists(path)) {
			try {
				Files.createFile(path);
				Files.write(path, "[]".getBytes()); // Initializing with an empty array
			} catch (IOException e) {
				throw new IOException("Error creating file: " + path, e);
			}
		}
	}
}
