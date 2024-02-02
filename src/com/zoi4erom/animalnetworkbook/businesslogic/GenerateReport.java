package com.zoi4erom.animalnetworkbook.businesslogic;

import com.zoi4erom.animalnetworkbook.businesslogic.exception.SignUpException;
import com.zoi4erom.animalnetworkbook.persistence.entity.Animal;
import com.zoi4erom.animalnetworkbook.persistence.entity.Request;
import com.zoi4erom.animalnetworkbook.persistence.entity.Shelter;
import com.zoi4erom.animalnetworkbook.persistence.entity.User;
import com.zoi4erom.animalnetworkbook.persistence.jsonhandler.JsonConverter;
import com.zoi4erom.animalnetworkbook.persistence.jsonhandler.JsonPaths;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class GenerateReport {
	private static final List<String> ignoredFields = List.of("DEFAULT_ROLE", "Password", "Animal", "User", "Shelter");
	private static final String REPORTS_DIRECTORY = "Data/Reports";
	public static void start(){
		List<User> users = JsonConverter.deserialization(JsonPaths.USER, User.class);
		List<Animal> animals = JsonConverter.deserialization(JsonPaths.ANIMAL, Animal.class);
		List<Shelter> shelters  = JsonConverter.deserialization(JsonPaths.SHELTERS, Shelter.class);
		List<Request> requests = JsonConverter.deserialization(JsonPaths.REQUEST, Request.class);
		if (!users.isEmpty()) {
			generateReport2(users, ignoredFields, "UsersReport");
		} else {
			System.out.println("Список користувачів порожній. Немає даних для звіту.");
		}

		if (!animals.isEmpty()) {
			generateReport2(animals, ignoredFields, "AnimalsReport");
		} else {
			System.out.println("Список тварин порожній. Немає даних для звіту.");
		}

		if (!shelters.isEmpty()) {
			generateReport2(shelters, ignoredFields, "SheltersReport");
		} else {
			System.out.println("Список притулків порожній. Немає даних для звіту.");
		}

		if (!requests.isEmpty()) {
			generateReport2(requests, ignoredFields, "RequestsReport");
		} else {
			System.out.println("Список запитів порожній. Немає даних для звіту.");
		}
	}
	public static void generateReport2(List<?> entities, List<String> ignoredFields, String filename) {
		Class<?> entityClass = entities.get(0).getClass();

		Field[] fields = entityClass.getDeclaredFields();

		String[] headers = new String[fields.length + 1];
		headers[0] = "№";

		for (int i = 0; i < fields.length; i++) {
			headers[i + 1] = capitalize(fields[i].getName());
		}

		generateReport(entities, headers, ignoredFields, filename);
	}
	public static void generateReport(List<?> entities, String[] headers, List<String> ignoredFields, String filename) {
		Class<?> entityClass = entities.get(0).getClass();

		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet(entityClass.getSimpleName() + "s");

		int rowNum = 0;
		Row headerRow = sheet.createRow(rowNum++);
		for (int i = 0, columnIndex = 0; i < headers.length; i++) {
			String fieldName = headers[i].replaceAll("\\s", "_");

			if (ignoredFields.contains(fieldName)) {
				continue;
			}

			Cell cell = headerRow.createCell(columnIndex++);
			cell.setCellValue(headers[i]);
		}

		for (Object entity : entities) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(rowNum - 1);

			int columnIndex = 1;

			for (int i = 1; i < headers.length; i++) {
				try {
					String fieldName = headers[i].replaceAll("\\s", "_");

					if (ignoredFields.contains(fieldName)) {
						continue;
					}

					Method getterMethod = entityClass.getMethod("get" + capitalize(fieldName));

					Object value = getterMethod.invoke(entity);

					row.createCell(columnIndex++).setCellValue(value != null ? value.toString() : "");
				} catch (NoSuchMethodException | IllegalAccessException |
					   InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}

		for (int i = 0; i < headers.length; i++) {
			sheet.autoSizeColumn(i);
		}

		String fileName = "%s[%s].xls".formatted(filename, LocalDateTime.now().toString())
		    .replace(':', '-');

		Path outputPath = Path.of(REPORTS_DIRECTORY, fileName);
		try (FileOutputStream outputStream = new FileOutputStream(outputPath.toFile())) {
			workbook.write(outputStream);
			workbook.close();
			System.out.println("Звіт збережено успішно: " + outputPath);
		} catch (IOException e) {
			throw new SignUpException("Помилка при збереженні звіту користувачів: %s"
			    .formatted(e.getMessage()));
		}
	}
	private static String capitalize(String str) {
		String[] words = str.split("\\s");
		StringBuilder capitalized = new StringBuilder();
		for (String word : words) {
			if (word.length() > 0) {
				capitalized.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
			}
			capitalized.append(" ");
		}
		return capitalized.toString().trim();
	}
}