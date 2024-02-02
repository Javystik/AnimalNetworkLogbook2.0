package com.zoi4erom.animalnetworkbook.businesslogic;

import com.zoi4erom.animalnetworkbook.businesslogic.exception.ExceptionTemplate;
import com.zoi4erom.animalnetworkbook.businesslogic.exception.VerificationException;
import com.zoi4erom.animalnetworkbook.persistence.entity.User;
import com.zoi4erom.animalnetworkbook.persistence.jsonhandler.JsonConverter;
import com.zoi4erom.animalnetworkbook.persistence.jsonhandler.JsonPaths;
import de.codeshelf.consoleui.prompt.ConsolePrompt;
import de.codeshelf.consoleui.prompt.InputResult;
import de.codeshelf.consoleui.prompt.builder.PromptBuilder;
import org.mindrot.bcrypt.BCrypt;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Provides functionality for user registration and verification.
 */
public class RegistrationService {

	private static final List<String> errors = new ArrayList<>();
	private static final int VERIFICATION_CODE_EXPIRATION_MINUTES = 1;
	private static LocalDateTime codeCreationTime;
	private static User user;

	private RegistrationService() {
	}

	/**
	 * Hashes the plain password using BCrypt.
	 *
	 * @param plainPassword the plain password to hash
	 * @return the hashed password
	 */
	private static String hashPassword(String plainPassword) {
		return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
	}

	/**
	 * Validates user registration information and initiates the registration process.
	 *
	 * @param fullName    the full name of the user
	 * @param password    the password of the user
	 * @param email       the email of the user
	 * @param phoneNumber the phone number of the user
	 * @param homeAddress the home address of the user
	 * @param birthdate   the birthdate of the user
	 * @return a list of validation errors or an empty list if validation is successful
	 * @throws IOException if there is an error during IO operations
	 */
	public static List<String> registrationValidation(String fullName, String password,
	    String email, String phoneNumber, String homeAddress, LocalDate birthdate) throws IOException {
		errors.clear();

		isValidFullName(fullName);
		isValidPassword(password);
		isValidPhoneNumber(phoneNumber);
		isValidHomeAddress(homeAddress);
		isValidEmail(email);
		isValidBirthdate(birthdate);

		if (!errors.isEmpty()) {
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

		user = new User(UUID.randomUUID(), fullName, hashPassword(password), phoneNumber, homeAddress, email, LocalDate.of(2006, 10, 20), null);

		if (Boolean.TRUE.equals(verifyCode(verificationCode, inputCode.getInput()))) {
			createUser(user);
		} else {
			System.out.println("Неправильний верифікаційний код!");
		}

		return errors;
	}

	/**
	 * Creates a new user and stores it in the user list.
	 *
	 * @param user the user to create
	 */
	private static void createUser(User user) {
		List<User> userList = JsonConverter.deserialization(JsonPaths.USER, User.class);

		if (userList.isEmpty()) {
			userList = new ArrayList<>();
		}

		userList.add(user);

		JsonConverter.serialization(userList, JsonPaths.USER);
	}

	/**
	 * Generates and sends a verification code to the user's email.
	 *
	 * @param email the user's email
	 * @return the generated verification code
	 */
	private static String generateAndSendVerificationCode(String email) {
		String verificationCode = String.valueOf((int) (Math.random() * 900000 + 100000));

		EmailService.sendVerificationCodeEmail(email, verificationCode);

		codeCreationTime = LocalDateTime.now();

		return verificationCode;
	}

	/**
	 * Verifies the provided verification code against the generated code.
	 *
	 * @param inputCode      the user-input verification code
	 * @param generatedCode  the code generated and sent to the user
	 * @return true if the codes match and are within the expiration time, false otherwise
	 */
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

	/**
	 * Validates the full name of the user.
	 *
	 * @param fullName the full name to validate
	 */
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

	/**
	 * Validates the password of the user.
	 *
	 * @param password the password to validate
	 */
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

	/**
	 * Validates the phone number of the user.
	 *
	 * @param phoneNumber the phone number to validate
	 */
	private static void isValidPhoneNumber(String phoneNumber) {
		final String FIELD_PHONE_NUMBER = "номеру телефона";
		if (Boolean.TRUE.equals(ValidatorServiceUtil.isFieldBlankValidate(phoneNumber))) {
			errors.add(ExceptionTemplate.EMPTY_FIELD_EXCEPTION.getTemplate()
			    .formatted(FIELD_PHONE_NUMBER));
		}
	}

	/**
	 * Validates the home address of the user.
	 *
	 * @param homeAddress the home address to validate
	 */
	private static void isValidHomeAddress(String homeAddress) {
		final String FIELD_HOME_ADDRESS = "домашньої адреси";
		if (Boolean.TRUE.equals(ValidatorServiceUtil.isFieldBlankValidate(homeAddress))) {
			errors.add(ExceptionTemplate.EMPTY_FIELD_EXCEPTION.getTemplate()
			    .formatted(FIELD_HOME_ADDRESS));
		}
	}

	/**
	 * Validates the email of the user.
	 *
	 * @param email the email to validate
	 */
	private static void isValidEmail(String email) {
		final String FIELD_EMAIL = "електронної пошти";
		if (Boolean.TRUE.equals(ValidatorServiceUtil.isFieldBlankValidate(email))) {
			errors.add(ExceptionTemplate.EMPTY_FIELD_EXCEPTION.getTemplate()
			    .formatted(FIELD_EMAIL));
		}
	}

	/**
	 * Validates the birthdate of the user.
	 *
	 * @param birthdate the birthdate to validate
	 */
	private static void isValidBirthdate(LocalDate birthdate) {
		final String FIELD_DATE_OF_BORN = "дати народження";
		if (Boolean.TRUE.equals(ValidatorServiceUtil.isFieldBlankValidate(String.valueOf(birthdate)))) {
			errors.add(ExceptionTemplate.EMPTY_FIELD_EXCEPTION.getTemplate()
			    .formatted(FIELD_DATE_OF_BORN));
		}
		if (Boolean.TRUE.equals(ValidatorServiceUtil.isValidDate(birthdate))) {
			errors.add("Дата не може бути в майбутньому.");
		}
	}
}
