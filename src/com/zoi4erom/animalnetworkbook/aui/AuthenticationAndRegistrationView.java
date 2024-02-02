package com.zoi4erom.animalnetworkbook.aui;

import com.zoi4erom.animalnetworkbook.persistence.entity.User;
import de.codeshelf.consoleui.prompt.ConsolePrompt;
import de.codeshelf.consoleui.prompt.ListResult;
import de.codeshelf.consoleui.prompt.builder.PromptBuilder;
import java.io.IOException;
/**
 * A view responsible for handling user authentication and registration.
 */
public class AuthenticationAndRegistrationView implements Renderable{
	/**
	 * Processes the selected item from the authentication and registration menu.
	 *
	 * @param selectedItem The selected item from the menu.
	 * @throws IOException If an I/O error occurs during the input process.
	 */
	private static void process(AuthenticationAndRegistrationView.AuthAndRegMenu selectedItem) throws IOException {
		switch (selectedItem) {
			case SIGN_IN -> {
				AuthenticationView.process();
			}
			case SIGN_UP -> {
				RegistrationView.process();
			}

			case EXIT -> {
				System.exit(0);
			}
		}
	}
	/**
	 * Renders the authentication and registration menu, prompting the user to choose an option.
	 *
	 * @throws IOException If an I/O error occurs during the input process.
	 */
	@Override
	public void render() throws IOException {
		ConsolePrompt prompt = new ConsolePrompt();
		PromptBuilder promptBuilder = prompt.getPromptBuilder();

		promptBuilder.createListPrompt()
		    .name("authAndRegMenu-menu")
		    .message("Оберіть пункт меню")
		    .newItem(AuthAndRegMenu.SIGN_IN.toString()).text(AuthAndRegMenu.SIGN_IN.getName()).add()
		    .newItem(AuthAndRegMenu.SIGN_UP.toString()).text(AuthAndRegMenu.SIGN_UP.getName()).add()
		    .newItem(AuthAndRegMenu.EXIT.toString()).text(AuthAndRegMenu.EXIT.getName()).add()
		    .addPrompt();

		var result = prompt.prompt(promptBuilder.build());
		ListResult resultItem = (ListResult) result.get("authAndRegMenu-menu");

		AuthenticationAndRegistrationView.AuthAndRegMenu selectedItem
		    = AuthenticationAndRegistrationView.AuthAndRegMenu.valueOf(resultItem.getSelectedId());

		process(selectedItem);
	}
	/**
	 * Enum representing items in the authentication and registration menu.
	 */
	enum AuthAndRegMenu {
		SIGN_IN("Авторизація"),
		SIGN_UP("Реєстрація"),
		EXIT("Вихід");
		private final String name;
		AuthAndRegMenu(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
	}
}