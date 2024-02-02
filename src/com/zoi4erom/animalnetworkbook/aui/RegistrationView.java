package com.zoi4erom.animalnetworkbook.aui;

import com.zoi4erom.animalnetworkbook.businesslogic.RegistrationService;
import de.codeshelf.consoleui.prompt.ConsolePrompt;
import de.codeshelf.consoleui.prompt.InputResult;
import de.codeshelf.consoleui.prompt.builder.PromptBuilder;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Represents the registration view for user registration in the application.
 */
public class RegistrationView {

	private RegistrationView() {
	}

	/**
	 * Processes the user registration.
	 *
	 * @throws IOException If an I/O error occurs during the registration process.
	 */
	public static void process() throws IOException {
		ConsolePrompt prompt = new ConsolePrompt();
		PromptBuilder promptBuilder = prompt.getPromptBuilder();

		// Build input prompts for user registration
		promptBuilder.createInputPrompt()
		    .name("fullName")
		    .message("Впишіть ваш ПІБ: ")
		    .addPrompt();
		promptBuilder.createInputPrompt()
		    .name("password")
		    .message("Впишіть ваш пароль: ")
		    .mask('*')
		    .addPrompt();
		promptBuilder.createInputPrompt()
		    .name("email")
		    .message("Впишіть електронну адресу: ")
		    .addPrompt();
		promptBuilder.createInputPrompt()
		    .name("phoneNumber")
		    .message("Впишіть номер телефону: ")
		    .addPrompt();
		promptBuilder.createInputPrompt()
		    .name("address")
		    .message("Впишіть домашню адресу: ")
		    .addPrompt();
		promptBuilder.createInputPrompt()
		    .name("birthday")
		    .message("Впишіть дату народження(в форматі YYYY-MM-DD): ")
		    .addPrompt();

		// Prompt the user for registration details
		var userResult = prompt.prompt(promptBuilder.build());

		var fullNameInput = ((InputResult) userResult.get("fullName"));
		var passwordInput = (InputResult) userResult.get("password");
		var emailInput = (InputResult) userResult.get("email");
		var phoneNumberInput = (InputResult) userResult.get("phoneNumber");
		var addressInput = (InputResult) userResult.get("address");
		var birthdayInput = (InputResult) userResult.get("birthday");

		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		try {
			// Parse the birthday input
			LocalDate birthday = LocalDate.parse(birthdayInput.getInput(), dateFormatter);

			// Perform registration validation
			List<String> errors = RegistrationService.registrationValidation(
			    fullNameInput.getInput(),
			    passwordInput.getInput(),
			    emailInput.getInput(),
			    phoneNumberInput.getInput(),
			    addressInput.getInput(),
			    birthday);

			// Display the registration result
			if (errors.isEmpty()) {
				System.out.println("Успішна реєстрація!");
			} else {
				System.out.println("Провалена реєстрація! Помилки: ");
				for (String error : errors) {
					System.err.println("- " + error);
				}
			}
		} catch (DateTimeParseException e) {
			System.out.println("Неправильний формат дати. Будь ласка, введіть дату у форматі 'yyyy-MM-dd'.");
			System.out.println("Повторіть спробу");
			AuthenticationAndRegistrationView authenticationAndRegistrationView
			    = new AuthenticationAndRegistrationView();
			authenticationAndRegistrationView.render();
		} catch (NullPointerException e) {
			System.out.println("Поле з датою народження, не може бути пусте!");
			System.out.println("Повторіть спробу");
			AuthenticationAndRegistrationView authenticationAndRegistrationView
			    = new AuthenticationAndRegistrationView();
			authenticationAndRegistrationView.render();
		}
	}
}