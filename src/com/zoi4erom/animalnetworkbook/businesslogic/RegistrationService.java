package com.zoi4erom.animalnetworkbook.businesslogic;

import com.zoi4erom.animalnetworkbook.businesslogic.exception.ExceptionTemplate;
import com.zoi4erom.animalnetworkbook.businesslogic.exception.VerificationException;
import com.zoi4erom.animalnetworkbook.persistence.entity.User;
import com.zoi4erom.animalnetworkbook.persistence.jsonhandler.JsonConverter;
import com.zoi4erom.animalnetworkbook.persistence.jsonhandler.JsonPaths;
import de.codeshelf.consoleui.prompt.ConsolePrompt;
import de.codeshelf.consoleui.prompt.InputResult;
import de.codeshelf.consoleui.prompt.builder.PromptBuilder;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.mindrot.bcrypt.BCrypt;

public class RegistrationService {
	private static final List<String> errors = new ArrayList<>();
	private static final int VERIFICATION_CODE_EXPIRATION_MINUTES = 1;
	private static LocalDateTime codeCreationTime;
	private static User user;
	private RegistrationService() {
	}
	private static String hashPassword(String plainPassword) {
		return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
	}
	public static List<String> registrationValidation(String fullName, String password,
	    String email, String phoneNumber, String homeAddress, LocalDate birthdate) throws IOException {
		errors.clear();

		isValidFullName(fullName);
		isValidPassword(password);
		isValidPhoneNumber(phoneNumber);
		isValidHomeAddress(homeAddress);
		isValidEmail(email);
		isValidBirthdate(birthdate);

		if(!errors.isEmpty()){
			return errors;
		}

		String verificationCode = generateAndSendVerificationCode(email);

		ConsolePrompt prompt = new ConsolePrompt();
		PromptBuilder promptBuilder = prompt.getPromptBuilder();
		promptBuilder.createInputPrompt()
		    .name("inputCode")
		    .message("Введіть верифікаційний код: ")
		    .addPrompt();

		var userResult = prompt.prompt(promptBuilder.build());
		var inputCode = (InputResult) userResult.get("inputCode");

		user = new User(UUID.randomUUID(), fullName, hashPassword(password), phoneNumber, homeAddress, email, LocalDate.of(2006,10,20), null);

		if(Boolean.TRUE.equals(verifyCode(verificationCode, inputCode.getInput()))){
			createUser(user);
		}else{
			System.out.println("Неправильний верифікаційний код!");
		}

		return errors;
	}
	private static void createUser(User user){
		List<User> userList = JsonConverter.deserialization(JsonPaths.USER, User.class);

		if(userList.isEmpty()){
			userList = new ArrayList<>();
		}

		userList.add(user);

		JsonConverter.serialization(userList, JsonPaths.USER);
	}
	private static String generateAndSendVerificationCode(String email) {
		String verificationCode = String.valueOf((int) (Math.random() * 900000 + 100000));

		EmailService.sendVerificationCodeEmail(email, verificationCode);

		codeCreationTime = LocalDateTime.now();

		return verificationCode;
	}
	private static Boolean verifyCode(String inputCode, String generatedCode) {
		try {
			if (codeCreationTime == null) {
				throw new VerificationException("Час створення коду не визначено.");
			}

			LocalDateTime currentTime = LocalDateTime.now();
			long minutesElapsed = codeCreationTime.until(currentTime, ChronoUnit.MINUTES);

			if (minutesElapsed > VERIFICATION_CODE_EXPIRATION_MINUTES) {
				throw new VerificationException("Час верифікації вийшов. Спробуйте ще раз.");
			}

			if (!inputCode.equals(generatedCode)) {
				System.out.println("Невірний код підтвердження.");
			}

			codeCreationTime = null;
			return true;

		} catch (VerificationException e) {
			throw new VerificationException("Помилка верифікації: " + e.getMessage());
		} catch (Exception e) {
			throw new VerificationException("Помилка під час верифікації: " + e.getMessage());
		}
	}
	private static void isValidFullName(String fullName) {
		final String FIELD_FULL_NAME = "ПІБ";
		final int MIN_SIZE = 4;
		final int MAX_SIZE = 48;

		if (Boolean.TRUE.equals(ValidatorServiceUtil.isFieldBlankValidate(fullName))) {
			errors.add(ExceptionTemplate.EMPTY_FIELD_EXCEPTION.getTemplate().formatted(FIELD_FULL_NAME));
		}
		if (Boolean.TRUE.equals(ValidatorServiceUtil.isFieldSizeValidate(fullName, MIN_SIZE, MAX_SIZE))) {
			errors.add(ExceptionTemplate.TOO_SHORT_LONG_EXCEPTION.getTemplate()
			    .formatted(FIELD_FULL_NAME, MIN_SIZE, MAX_SIZE));
		}
	}
	private static void isValidPassword(String password) {
		final String FIELD_PASSWORD = "паролю";
		final int MIN_SIZE = 2;
		final int MAX_SIZE = 30;

		if (Boolean.TRUE.equals(ValidatorServiceUtil.isFieldBlankValidate(password))) {
			errors.add(ExceptionTemplate.EMPTY_FIELD_EXCEPTION.getTemplate()
			    .formatted(FIELD_PASSWORD));
		}
		if (Boolean.TRUE.equals(ValidatorServiceUtil.isFieldSizeValidate(password, MIN_SIZE, MAX_SIZE))) {
			errors.add(ExceptionTemplate.TOO_SHORT_LONG_EXCEPTION.getTemplate()
			    .formatted(FIELD_PASSWORD, MIN_SIZE, MAX_SIZE));
		}
	}
	private static void isValidPhoneNumber(String phoneNumber) {
		final String FIELD_PHONE_NUMBER = "номеру телефона";
		if (Boolean.TRUE.equals(ValidatorServiceUtil.isFieldBlankValidate(phoneNumber))) {
			errors.add(ExceptionTemplate.EMPTY_FIELD_EXCEPTION.getTemplate()
			    .formatted(FIELD_PHONE_NUMBER));
		}
	}
	private static void isValidHomeAddress(String homeAddress) {
		final String FIELD_HOME_ADDRESS = "домашньої адреси";
		if (Boolean.TRUE.equals(ValidatorServiceUtil.isFieldBlankValidate(homeAddress))) {
			errors.add(ExceptionTemplate.EMPTY_FIELD_EXCEPTION.getTemplate()
			    .formatted(FIELD_HOME_ADDRESS));
		}
	}
	private static void isValidEmail(String email) {
		final String FIELD_EMAIL = "електронної пошти";
		if (Boolean.TRUE.equals(ValidatorServiceUtil.isFieldBlankValidate(email))) {
			errors.add(ExceptionTemplate.EMPTY_FIELD_EXCEPTION.getTemplate()
			    .formatted(FIELD_EMAIL));
		}
	}
	private static void isValidBirthdate(LocalDate dateOfBorn) 	{
		final String FIELD_DATE_OF_BORN = "дати народження";
		if (Boolean.TRUE.equals(ValidatorServiceUtil.isFieldBlankValidate(String.valueOf(dateOfBorn)))) {
			errors.add(ExceptionTemplate.EMPTY_FIELD_EXCEPTION.getTemplate()
			    .formatted(FIELD_DATE_OF_BORN));
		}
		if(Boolean.TRUE.equals(ValidatorServiceUtil.isValidDate(dateOfBorn))){
			errors.add("Дата не може бути в майбутньому.");
		}
	}
}