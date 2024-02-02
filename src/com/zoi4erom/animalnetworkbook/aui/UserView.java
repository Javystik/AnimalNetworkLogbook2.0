package com.zoi4erom.animalnetworkbook.aui;

import static com.zoi4erom.animalnetworkbook.aui.UserView.UserMenu.*;
import static java.lang.System.out;

import com.zoi4erom.animalnetworkbook.businesslogic.UserService;
import com.zoi4erom.animalnetworkbook.persistence.entity.User;
import de.codeshelf.consoleui.prompt.ConsolePrompt;
import de.codeshelf.consoleui.prompt.InputResult;
import de.codeshelf.consoleui.prompt.ListResult;
import de.codeshelf.consoleui.prompt.builder.ListPromptBuilder;
import de.codeshelf.consoleui.prompt.builder.PromptBuilder;
import java.io.IOException;
import java.util.List;

/**
 * The {@code UserView} class represents the view for managing user-related operations.
 * It provides methods for rendering user-related tasks in the console.
 * This class implements the {@code Renderable} interface.
 */
public class UserView implements Renderable{
	/**
	 * Constructs a new {@code UserView} with the specified active user.
	 *
	 * @param activeUser The user interacting with the user view.
	 */
	private User activeUser;

	public UserView(User activeUser) {
		this.activeUser = activeUser;
	}
	/**
	 * Gets the full name of the user through a console prompt.
	 *
	 * @return The full name entered by the user.
	 * @throws IOException if an I/O error occurs.
	 */
	public static String userFullName() throws IOException {
		ConsolePrompt prompt = new ConsolePrompt();
		PromptBuilder promptBuilder = prompt.getPromptBuilder();
		promptBuilder.createInputPrompt()
		    .name("userName")
		    .message("Введіть ім'я користувача: ")
		    .addPrompt();

		var result = prompt.prompt(promptBuilder.build());
		var userNameInput = (InputResult) result.get("userName");
		return userNameInput.getInput();
	}
	/**
	 * Gets the UUID of the user through a console prompt.
	 *
	 * @return The UUID entered by the user.
	 * @throws IOException if an I/O error occurs.
	 */
	public static String userUUID() throws IOException {
		ConsolePrompt prompt = new ConsolePrompt();
		PromptBuilder promptBuilder = prompt.getPromptBuilder();
		promptBuilder.createInputPrompt()
		    .name("userUUID")
		    .message("Enter the UUID: ")
		    .addPrompt();

		var result = prompt.prompt(promptBuilder.build());
		var userUUIDInput = (InputResult) result.get("userUUID");
		return userUUIDInput.getInput();
	}
	/**
	 * Gets the user role through a console prompt, allowing the user to select from predefined roles.
	 *
	 * @return The selected user role.
	 * @throws IOException if an I/O error occurs.
	 */
	public static String userRole() throws IOException {
		ConsolePrompt prompt = new ConsolePrompt();
		PromptBuilder promptBuilder = prompt.getPromptBuilder();
		ListPromptBuilder listPromptBuilder = promptBuilder.createListPrompt();

		listPromptBuilder
		    .name("userRole")
		    .message("Виберіть роль:")
		    .newItem(User.Role.ADMIN.name()).text(User.Role.ADMIN.name()).add()
		    .newItem(User.Role.PERSONAL.name()).text(User.Role.PERSONAL.name()).add()
		    .newItem(User.Role.USER.name()).text(User.Role.USER.name()).add()
		    .addPrompt();

		var result = prompt.prompt(promptBuilder.build());
		ListResult listResult = (ListResult) result.get("userRole");
		return listResult.getSelectedId();
	}
	/**
	 * Gets the user email through a console prompt.
	 *
	 * @return The entered user email.
	 * @throws IOException if an I/O error occurs.
	 */
	public static String userEmail() throws IOException {
		ConsolePrompt prompt = new ConsolePrompt();
		PromptBuilder promptBuilder = prompt.getPromptBuilder();
		promptBuilder.createInputPrompt()
		    .name("userEmail")
		    .message("Введіть новий Email: ")
		    .addPrompt();

		var result = prompt.prompt(promptBuilder.build());
		InputResult emailInput = (InputResult) result.get("userEmail");
		return emailInput.getInput();
	}
	/**
	 * Edits the details of the given user based on user input.
	 *
	 * @param selectedUser The user to be edited.
	 * @throws IOException if an I/O error occurs.
	 */
	private void editUser(User selectedUser) throws IOException {
		ConsolePrompt prompt = new ConsolePrompt();
		PromptBuilder promptBuilder = prompt.getPromptBuilder();

		ListPromptBuilder listPromptBuilder = promptBuilder.createListPrompt();
		listPromptBuilder
		    .name("editOptions")
		    .message("Оберіть опцію для редагування:")
		    .newItem("1").text("Змінити Повне Ім'я").add()
		    .newItem("2").text("Змінити Роль").add()
		    .newItem("3").text("Змінити Email").add()
		    .newItem("0").text("Завершити редагування").add()
		    .addPrompt();

		String option;
		do {
			var result = prompt.prompt(promptBuilder.build());
			ListResult listResult = (ListResult) result.get("editOptions");
			option = listResult.getSelectedId();

			switch (option) {
				case "1":
					String newFullName = userFullName();
					selectedUser.setFullName(newFullName);
					break;
				case "2":
					String newRoleString = userRole();
					User.Role newRole = User.Role.valueOf(newRoleString);
					selectedUser.setRole(newRole);
					break;
				case "3":
					String newEmail = userEmail();
					selectedUser.setEmail(newEmail);
					break;
			}

		} while (!option.equals("0"));

		UserService.updateUser(selectedUser);

		System.out.println("\nОновлений користувач:\n " + selectedUser);
	}
	/**
	 * Processes the selected option from the user menu.
	 *
	 * @param selectedItem The selected option from the user menu.
	 * @throws IOException if an I/O error occurs.
	 */
	private void process(UserMenu selectedItem) throws IOException {
		switch (selectedItem) {
			case VIEW_ALL_USER -> {
				System.out.print("\033[H\033[2J");
				List<User> users = UserService.getAllUsers();
				if(!users.isEmpty()){
					out.println("Знайдені користувачі: ");
					for (User user: users){
						out.println(user);
					}
				}else {
					out.println("Юзерів не знайдено!");
				}
				render();
			}
			case FIND_USER_BY_NAME -> {
				System.out.print("\033[H\033[2J");
				String fullName = userFullName();
				List<User> users = UserService.findUserByName(fullName);

				if (users != null) {
					for (User user: users){
						out.println("Знайдені користувачі: ");
						out.println(user);
					}
				} else {
					out.println("Юзера по данному імені не знайдено!");
				}
				render();
			}

			case FIND_USER_BY_ID -> {
				System.out.print("\033[H\033[2J");
				String userIdString = userUUID();
				User foundUser = UserService.findUserByUUID(userIdString);

				if (foundUser != null) {
					out.println("Користувача знайдено: " + foundUser);
				} else {
					out.println("Користувач по заданому айді не знайдено");
				}
				render();
			}

			case EDIT_USER -> {
				System.out.print("\033[H\033[2J");
				String userIdString = userUUID();
				User foundUser = UserService.findUserByUUID(userIdString);

				if (foundUser != null) {
					if (!foundUser.getId().equals(activeUser.getId())) {
						editUser(foundUser);
					} else {
						out.println("Ви не можете редагувати власний профіль.");
					}
				} else {
					out.println("Користувача з ID '" + userIdString + "' не знайдено.");
				}
				render();
			}

			case BACK -> {
				System.out.print("\033[H\033[2J");
				User activeUser = AuthenticationView.getActiveUser();
				MainMenuView mainMenuView = new MainMenuView(activeUser);
				mainMenuView.render();
			}
		}
	}
	/**
	 * Renders the main menu for user-related operations and processes user input.
	 * This method uses a console prompt for user interaction.
	 *
	 * @throws IOException if an I/O error occurs.
	 */
	@Override
	public void render() throws IOException {
		ConsolePrompt prompt = new ConsolePrompt();
		PromptBuilder promptBuilder = prompt.getPromptBuilder();

		promptBuilder.createListPrompt()
		    .name("main-menu")
		    .message("Моніторинг користувачів")
		    .newItem(VIEW_ALL_USER.toString()).text(VIEW_ALL_USER.getName()).add()
		    .newItem(FIND_USER_BY_NAME.toString()).text(FIND_USER_BY_NAME.getName()).add()
		    .newItem(FIND_USER_BY_ID.toString()).text(FIND_USER_BY_ID.getName()).add()
		    .newItem(EDIT_USER.toString()).text(EDIT_USER.getName()).add()
		    .newItem(BACK.toString()).text(BACK.getName()).add()
		    .addPrompt();

		var result = prompt.prompt(promptBuilder.build());
		ListResult resultItem = (ListResult) result.get("main-menu");

		UserView.UserMenu selectedItem = UserView.UserMenu.valueOf(
		    resultItem.getSelectedId());
		process(selectedItem);

		System.out.print("\033[H\033[2J");
	}

	/**
	 * The {@code UserMenu} enum represents the different options in the user menu.
	 * It includes options for viewing, adding, editing, and searching users.
	 */
	enum UserMenu {
		VIEW_ALL_USER("Перегляд всіх користувачів"),
		FIND_USER_BY_NAME("Пошук користувача по ніку"),
		FIND_USER_BY_ID("Пошук користувача по айді"),
		EDIT_USER("Редагування користувача"),
		BACK("Повернутись назад");
		private final String name;
		UserMenu(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
	}
}