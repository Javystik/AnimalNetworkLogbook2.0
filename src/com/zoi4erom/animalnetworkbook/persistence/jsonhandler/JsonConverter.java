package com.zoi4erom.animalnetworkbook.persistence.jsonhandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.zoi4erom.animalnetworkbook.persistence.entity.Entity;
import com.zoi4erom.animalnetworkbook.persistence.exception.DeserializationException;
import com.zoi4erom.animalnetworkbook.persistence.exception.SerializationException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public final class JsonConverter {
	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
	public static void serialization(List<? extends Entity> entities, JsonPaths jsonPaths) {
		Type entityType = new TypeToken<List<Entity>>() {}.getType();

		try {
			Path directoryPath = Paths.get(jsonPaths.getPath()).getParent();
			Files.createDirectories(directoryPath);

			Gson gson = new GsonBuilder()
			    .setPrettyPrinting()
			    .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
				  new JsonPrimitive(dateFormatter.format(src)))
			    .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context) ->
				  LocalDate.parse(json.getAsJsonPrimitive().getAsString(), dateFormatter))
			    .create();

			try (Writer writer = new FileWriter(jsonPaths.getPath())) {
				gson.toJson(entities, entityType, writer);
			}
		} catch (IOException e) {
			throw new SerializationException("Помилка при записі в файл: " + e.getMessage(), e);
		}
	}
	public static <T extends Entity> List<T> deserialization(JsonPaths jsonPaths, Class<T> clazz) {
		try {
			Path filePath = Paths.get(jsonPaths.getPath());

			if (!Files.exists(filePath)) {
				Files.createDirectories(filePath.getParent());
				Files.createFile(filePath);
				return new ArrayList<>();
			}

			String jsonContent = Files.readString(filePath);

			Gson gson = new GsonBuilder()
			    .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
				  new JsonPrimitive(dateFormatter.format(src)))
			    .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context) ->
				  LocalDate.parse(json.getAsJsonPrimitive().getAsString(), dateFormatter))
			    .create();

			Type entityType = TypeToken.getParameterized(List.class, clazz).getType();
			return gson.fromJson(jsonContent, entityType);
		} catch (IOException e) {
			throw new DeserializationException("Помилка при читанні з файлу: " + e.getMessage(), e);
		}
	}
}