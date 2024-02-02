package com.zoi4erom.animalnetworkbook.persistence.jsonhandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
public class FileCheckerUtil {
	public static void checkAndCreateDirectoriesAndFiles() {
		try {
			checkAndCreateDirectory(JsonPaths.USER.getPath());

			checkAndCreateDirectory("Data/Reports");

			for (JsonPaths jsonPath : JsonPaths.values()) {
				checkAndCreateFile(jsonPath.getPath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static void checkAndCreateDirectory(String directoryPath) throws IOException {
		Path path = Paths.get(directoryPath);
		Path parentDir = path.getParent();

		if (parentDir != null && !Files.exists(parentDir)) {
			try {
				Files.createDirectories(parentDir);
			} catch (IOException e) {
				throw new IOException("Помилка при створенні директорії: " + parentDir, e);
			}
		}
	}
	private static void checkAndCreateFile(String filePath) throws IOException {
		Path path = Paths.get(filePath);
		if (!Files.exists(path)) {
			try {
				Files.createFile(path);
				Files.write(path, "[]".getBytes());
			} catch (IOException e) {
				throw new IOException("Помилка при створенні файлу: " + path, e);
			}
		}
	}
}
