package com.zoi4erom.animalnetworkbook.aui;

import com.zoi4erom.animalnetworkbook.businesslogic.AuthorizationService;
import com.zoi4erom.animalnetworkbook.persistence.entity.User;
import de.codeshelf.consoleui.prompt.ConsolePrompt;
import de.codeshelf.consoleui.prompt.InputResult;
import de.codeshelf.consoleui.prompt.builder.PromptBuilder;
import java.io.IOException;
/**
 * A view responsible for handling user authentication.
 */
public class AuthenticationView {
	/**
	 * The currently authenticated user.
	 */
	private static User activeUser;
	/**
	 * Gets the currently authenticated user.
	 *
	 * @return The currently authenticated user.
	 */
	public static User getActiveUser() {
		return activeUser;
	}
	/**
	 * Private constructor to prevent instantiation as this class provides only static methods.
	 */
	private AuthenticationView() {
	}
	/**
	 * Performs the user authentication process.
	 *
	 * @return The authenticated user or null if authentication fails.
	 * @throws IOException If an I/O error occurs during the input process.
	 */
	public static User process() throws IOException {
		ConsolePrompt prompt = new ConsolePrompt();
		PromptBuilder promptBuilder = prompt.getPromptBuilder();

		promptBuilder.createInputPrompt()
		    .name("fullName")
		    .message("Впишіть ваш логін: ")
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

		var userResult = prompt.prompt(promptBuilder.build());
		var fullNameInput = ((InputResult) userResult.get("fullName"));
		var passwordInput = (InputResult) userResult.get("password");
		var emailInput = (InputResult) userResult.get("email");

		User authenticatedUser = AuthorizationService.authorization(fullNameInput.getInput()
		    , passwordInput.getInput(), emailInput.getInput());

		if (authenticatedUser == null) {
			System.out.print("\033[H\033[2J");
			System.out.println("Користувача не знайдено! Повторіть спробу");
		} else {
			System.out.print("\033[H\033[2J");
			System.out.println("Успішна авторизація. Вітаємо вас: " + fullNameInput.getInput());

			MainMenuView mainMenuView = new MainMenuView(authenticatedUser);
			mainMenuView.render();
		}

		return authenticatedUser;
	}
}
