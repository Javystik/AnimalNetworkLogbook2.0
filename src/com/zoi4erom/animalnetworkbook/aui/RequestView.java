package com.zoi4erom.animalnetworkbook.aui;

import static com.zoi4erom.animalnetworkbook.aui.RequestView.RequestMenu.*;
import static java.lang.System.out;

import com.zoi4erom.animalnetworkbook.businesslogic.AnimalService;
import com.zoi4erom.animalnetworkbook.businesslogic.RequestService;
import com.zoi4erom.animalnetworkbook.businesslogic.UserService;
import com.zoi4erom.animalnetworkbook.persistence.entity.Animal;
import com.zoi4erom.animalnetworkbook.persistence.entity.Request;
import com.zoi4erom.animalnetworkbook.persistence.entity.Request.RequestStatus;
import com.zoi4erom.animalnetworkbook.persistence.entity.User;
import de.codeshelf.consoleui.prompt.ConsolePrompt;
import de.codeshelf.consoleui.prompt.InputResult;
import de.codeshelf.consoleui.prompt.ListResult;
import de.codeshelf.consoleui.prompt.builder.ListPromptBuilder;
import de.codeshelf.consoleui.prompt.builder.PromptBuilder;
import java.io.IOException;
import java.util.List;
/**
 * The {@code RequestView} class provides a user interface for managing requests related to animals.
 * It allows users to view, add, find by status, edit, and find requests by animal or user.
 * The class implements the {@code Renderable} interface for rendering in a UI context.
 *
 * @author Zoi4erom
 */
public class RequestView implements Renderable{
	/**
	 * Constructs a new {@code UserView} with the specified active user.
	 *
	 * @param activeUser The user interacting with the user view.
	 */
	private static User activeUser;
	/**
	 * Constructs a new {@code RequestView} instance with the specified active user.
	 * The active user is the user currently interacting with the request view.
	 * This constructor is used to initialize the active user when creating a new instance of the {@code RequestView}.
	 *
	 * @param activeUser The user currently interacting with the request view.
	 * @since 1.0
	 */
	public RequestView(User activeUser) {
		this.activeUser = activeUser;
	}
	/**
	 * Prompts the user to enter a name for a new request.
	 *
	 * @return The name entered by the user.
	 * @throws IOException If an I/O error occurs.
	 */
	public static String requestName() throws IOException {
		ConsolePrompt prompt = new ConsolePrompt();
		PromptBuilder promptBuilder = prompt.getPromptBuilder();
		promptBuilder.createInputPrompt()
		    .name("shelterName")
		    .message("Впишіть назву запиту: ")
		    .addPrompt();

		var result = prompt.prompt(promptBuilder.build());
		var productNameInput = (InputResult) result.get("shelterName");
		return productNameInput.getInput();
	}
	/**
	 * Allows the user to select an animal from a list.
	 *
	 * @return The selected {@code Animal} object or {@code null} if no animal is selected.
	 * @throws IOException If an I/O error occurs.
	 */
	public static Animal selectAnimalFromList() throws IOException {
		List<Animal> allAnimals = AnimalService.getAllAnimals();

		if (allAnimals.isEmpty()) {
			System.out.println("Немає доступних тварин.");
			return null;
		}

		try {
			ConsolePrompt prompt = new ConsolePrompt();
			PromptBuilder promptBuilder = prompt.getPromptBuilder();

			ListPromptBuilder listPromptBuilder = promptBuilder.createListPrompt();
			listPromptBuilder
			    .name("animalList")
			    .message("Виберіть тварину:");

			for (Animal animal : allAnimals) {
				String displayText = String.format("%s(%s-%s)", animal.getName(), animal.getSpecies(), animal.getBreed());
				listPromptBuilder.newItem().text(displayText).add();
			}

			listPromptBuilder.addPrompt();

			var result = prompt.prompt(promptBuilder.build());
			ListResult listResult = (ListResult) result.get("animalList");

			if (listResult != null) {
				String selectedAnimalText = listResult.getSelectedId();
				Animal selectedAnimal = getSelectedAnimal(allAnimals, selectedAnimalText);

				if (selectedAnimal != null) {
					return selectedAnimal;
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		System.out.println("Вибір тварини скасовано або виникла помилка.");
		return null;
	}
	/**
	 * Renders the main menu for request management, allowing users to perform various operations.
	 * Overrides the {@code render} method in the {@code Renderable} interface.
	 *
	 * @throws IOException If an I/O error occurs.
	 */
	/**
	 * Retrieves the selected animal from the provided list of animals based on the formatted text.
	 *
	 * @param allAnimals        The list of all available animals.
	 * @param selectedAnimalText The formatted text representing the selected animal.
	 * @return The selected animal, or null if not found.
	 */
	private static Animal getSelectedAnimal(List<Animal> allAnimals, String selectedAnimalText) {
		String[] parts = selectedAnimalText.split("\\(");
		if (parts.length > 0) {
			String selectedAnimalName = parts[0].trim();
			return allAnimals.stream()
			    .filter(animal -> animal.getName().equals(selectedAnimalName))
			    .findFirst()
			    .orElse(null);
		}
		return null;
	}
	/**
	 * Allows the user to select a request from the list of all available requests.
	 *
	 * @return The selected request, or null if no requests are available or the selection is canceled.
	 * @throws IOException If an I/O error occurs during user interaction.
	 */
	private Request selectRequestFromList() throws IOException {
		List<Request> allRequests = RequestService.getAllRequests();

		if (allRequests.isEmpty()) {
			System.out.println("Немає доступних запитів.");
			return null;
		}

		try {
			ConsolePrompt prompt = new ConsolePrompt();
			PromptBuilder promptBuilder = prompt.getPromptBuilder();

			ListPromptBuilder listPromptBuilder = promptBuilder.createListPrompt();
			listPromptBuilder
			    .name("requestList")
			    .message("Виберіть запит за номером:");

			for (Request request : allRequests) {
				out.println("");
				listPromptBuilder.newItem().text(request.toString()).add();
			}

			listPromptBuilder.addPrompt();

			var result = prompt.prompt(promptBuilder.build());
			ListResult listResult = (ListResult) result.get("requestList");

			if (listResult != null) {
				String selectedRequestText = listResult.getSelectedId();
				return getSelectedRequest(allRequests, selectedRequestText);
			} else {
				System.out.println("Вибір запиту скасовано або виникла помилка.");
				return null;
			}
		} catch (Exception e) {
			out.println(e);
		}
		return null;
	}
	/**
	 * Retrieves the selected request from the provided list of requests based on the formatted text.
	 *
	 * @param allRequests          The list of all available requests.
	 * @param selectedRequestText The formatted text representing the selected request.
	 * @return The selected request, or null if not found.
	 */
	private static Request getSelectedRequest(List<Request> allRequests, String selectedRequestText) {
		for (Request request : allRequests) {
			if (request.toString().equals(selectedRequestText)) {
				return request;
			}
		}
		return null;
	}
	/**
	 * Allows the user to edit a selected request by providing options for different modifications.
	 *
	 * @param selectedRequest The request to be edited.
	 * @return The edited request after modifications.
	 * @throws IOException If an I/O error occurs during user interaction.
	 */
	private Request editRequest(Request selectedRequest) throws IOException {
		ConsolePrompt prompt = new ConsolePrompt();
		PromptBuilder promptBuilder = prompt.getPromptBuilder();

		ListPromptBuilder listPromptBuilder = promptBuilder.createListPrompt();
		listPromptBuilder
		    .name("editOptions")
		    .message("Оберіть опцію для редагування:")
		    .newItem("1").text("Змінити назву").add()
		    .newItem("2").text("Змінити тварину").add()
		    .newItem("3").text("Змінити статус").add()
		    .newItem("0").text("Завершити редагування").add()
		    .addPrompt();

		String option;
		do {
			var result = prompt.prompt(promptBuilder.build());
			ListResult listResult = (ListResult) result.get("editOptions");
			option = listResult.getSelectedId();

			switch (option) {
				case "1":
					String newName = promptForNewName();
					selectedRequest.setName(newName);
					break;
				case "2":
					Animal newAnimal = selectAnimalFromList();
					selectedRequest.setAnimal(newAnimal);
					break;
				case "3":
					RequestStatus newStatus = promptForRequestStatus();
					selectedRequest.setStatus(newStatus);
					break;
			}

		} while (!option.equals("0"));

		RequestService.updateRequest(selectedRequest);

		System.out.println("\nЗмінений запит:\n " + selectedRequest);

		return selectedRequest;
	}
	/**
	 * Prompts the user to enter a new name through the console.
	 *
	 * @return The new name entered by the user.
	 * @throws IOException If an I/O error occurs during user interaction.
	 */
	private String promptForNewName() throws IOException {
		ConsolePrompt prompt = new ConsolePrompt();
		PromptBuilder promptBuilder = prompt.getPromptBuilder();

		promptBuilder.createInputPrompt()
		    .name("newName")
		    .message("Введіть нову назву: ")
		    .addPrompt();

		var result = prompt.prompt(promptBuilder.build());
		InputResult inputResult = (InputResult) result.get("newName");

		return inputResult.getInput();
	}
	/**
	 * Allows the user to select a user from the list of all available users.
	 *
	 * @return The selected user, or null if no users are available or the selection is canceled.
	 * @throws IOException If an I/O error occurs during user interaction.
	 */
	public static User selectUserFromList() throws IOException {
		List<User> allUsers = UserService.getAllUsers();

		if (allUsers.isEmpty()) {
			System.out.println("Немає доступних користувачів.");
			return null;
		}

		try {
			ConsolePrompt prompt = new ConsolePrompt();
			PromptBuilder promptBuilder = prompt.getPromptBuilder();

			ListPromptBuilder listPromptBuilder = promptBuilder.createListPrompt();
			listPromptBuilder
			    .name("userList")
			    .message("Виберіть користувача за номером:");

			for (User user : allUsers) {
				listPromptBuilder.newItem().text(user.getFullName()).add();
			}

			listPromptBuilder.addPrompt();

			var result = prompt.prompt(promptBuilder.build());
			ListResult listResult = (ListResult) result.get("userList");

			if (listResult != null) {
				User selectedUser = getSelectedUser(allUsers, listResult.getSelectedId());

				if (selectedUser != null) {
					return selectedUser;
				} else {
					System.out.println("Вибір користувача скасовано або виникла помилка.");
					return null;
				}
			} else {
				System.out.println("Вибір користувача скасовано або виникла помилка.");
				return null;
			}
		} catch (Exception e) {
			out.println(e);
		}
		return null;
	}
	/**
	 * Retrieves the selected user from the provided list of users based on the selected identifier.
	 *
	 * @param allUsers    The list of all available users.
	 * @param selectedId The selected identifier, which can be either a username (String) or a User object.
	 * @return The selected user, or null if not found.
	 */
	private static User getSelectedUser(List<User> allUsers, Object selectedId) {
		if (selectedId instanceof String) {
			String selectedUserName = (String) selectedId;
			return allUsers.stream()
			    .filter(user -> user.getFullName().equals(selectedUserName))
			    .findFirst()
			    .orElse(null);
		} else if (selectedId instanceof User) {
			return (User) selectedId;
		} else {
			return null;
		}
	}
	/**
	 * Prompts the user to select a request status from a list of options.
	 * Uses ConsolePrompt for interaction and returns the selected RequestStatus.
	 *
	 * @return The selected RequestStatus, or RequestStatus.PENDING if no selection is made.
	 * @throws IOException If an I/O error occurs during the prompt.
	 */
	public static RequestStatus promptForRequestStatus() throws IOException {
		ConsolePrompt prompt = new ConsolePrompt();
		PromptBuilder promptBuilder = prompt.getPromptBuilder();

		ListPromptBuilder listPromptBuilder = promptBuilder.createListPrompt();
		listPromptBuilder
		    .name("requestStatus")
		    .message("Виберіть статус запиту:")
		    .newItem(RequestStatus.APPROVED.toString()).text(RequestStatus.APPROVED.getDescription()).add()
		    .newItem(RequestStatus.REJECTED.toString()).text(RequestStatus.REJECTED.getDescription()).add()
		    .newItem(RequestStatus.PENDING.toString()).text(RequestStatus.PENDING.getDescription()).add()
		    .addPrompt();

		var result = prompt.prompt(promptBuilder.build());
		ListResult listResult = (ListResult) result.get("requestStatus");

		if (listResult != null) {
			return RequestStatus.valueOf(listResult.getSelectedId());
		} else {
			return RequestStatus.PENDING;
		}
	}
	/**
	 * Processes the user's selection from the RequestMenu, performing corresponding actions.
	 *
	 * @param selectedItem The selected action from the RequestMenu enum.
	 * @throws IOException If an I/O error occurs during processing.
	 */
	private void process(RequestMenu selectedItem) throws IOException {
		switch (selectedItem) {
			case VIEW_ALL_REQUEST -> {
				List<Request> requests = RequestService.getAllRequests();
				if(!requests.isEmpty()){
					System.out.print("\033[H\033[2J");
					out.println("Знайдені запити: ");
					for (Request request: requests){
						out.println("-" + request);
					}
				}else{
					out.println("В системі не знайдено запитів!");
				}
				render();
			}
			case ADD_REQUEST -> {
				System.out.print("\033[H\033[2J");
				String name = requestName();
				Animal animal = selectAnimalFromList();
				User user = activeUser;
				List<String> errors = RequestService.createRequestValidation(name, animal, user);
				if(!errors.isEmpty()){
					System.out.println("Помилки при створенні запиту:");
					errors.forEach(System.out::println);
				}else{
					System.out.print("\033[H\033[2J");
					out.println("Приют успішно створено!");
				}
				render();
			}
			case FIND_REQUEST_BY_STATUS -> {
				List<Request> requests = RequestService.findRequestsByStatus(promptForRequestStatus());
				System.out.print("\033[H\033[2J");
				out.println("Знайденні запити: ");
				if(!requests.isEmpty()){
					for (Request request: requests){
						out.println("- " + request);
					}
				}else {
					System.out.print("\033[H\033[2J");
					out.println("Запити по цьому статусу не знайдено!");
				}
				render();
			}
			case EDITING_REQUEST -> {
				System.out.print("\033[H\033[2J");
				Request selectedRequest = selectRequestFromList();
				if (selectedRequest != null) {
					editRequest(selectedRequest);
				} else {
					out.println("Вибір запиту скасовано або виникла помилка.");
				}
				render();
			}

			case FIND_BY_ANIMAL -> {
				List<Request> requests = RequestService.findRequestsByAnimal(selectAnimalFromList());
				System.out.print("\033[H\033[2J");
				out.println("Знайденні запити: ");
				if(!requests.isEmpty()){
					for (Request request: requests){
						out.println("- " + request);
					}
				}else {
					System.out.print("\033[H\033[2J");
					out.println("Запити на цю тварину не знайдено! Допоможіть тварині, створивши запит на неї!");
				}
				render();
			}
			case FIND_BY_USER -> {
				List<Request> requests = RequestService.findRequestsByUser(selectUserFromList());
				System.out.print("\033[H\033[2J");
				out.println("Знайденні запити: ");
				if(!requests.isEmpty()){
					for (Request request: requests){
						out.println("- " + request);
					}
				}else {
					System.out.print("\033[H\033[2J");
					out.println("Цей юзер ще не створював запитів!");
				}
				render();
			}
			case BACK -> {
				System.out.print("\033[H\033[2J");
				User activeUser = RequestView.getActiveUser();
				MainMenuView mainMenuView = new MainMenuView(activeUser);
				mainMenuView.render();
			}
		}
	}

	public static User getActiveUser() {
		return activeUser;
	}

	/**
	 * Displays the main menu for monitoring requests, prompts the user for a selection,
	 * and processes the chosen action.
	 *
	 * @throws IOException If an I/O error occurs during rendering or processing.
	 */
	@Override
	public void render() throws IOException {

		ConsolePrompt prompt = new ConsolePrompt();
		PromptBuilder promptBuilder = prompt.getPromptBuilder();

		promptBuilder.createListPrompt()
		    .name("main-menu")
		    .message("Моніторинг запитів")
		    .newItem(VIEW_ALL_REQUEST.toString()).text(VIEW_ALL_REQUEST.getName()).add()
		    .newItem(ADD_REQUEST.toString()).text(ADD_REQUEST.getName()).add()
		    .newItem(FIND_REQUEST_BY_STATUS.toString()).text(FIND_REQUEST_BY_STATUS.getName()).add()
		    .newItem(EDITING_REQUEST.toString()).text(EDITING_REQUEST.getName()).add()
		    .newItem(FIND_BY_ANIMAL.toString()).text(FIND_BY_ANIMAL.getName()).add()
		    .newItem(FIND_BY_USER.toString()).text(FIND_BY_USER.getName()).add()
		    .newItem(BACK.toString()).text(BACK.getName()).add()
		    .addPrompt();

		var result = prompt.prompt(promptBuilder.build());
		ListResult resultItem = (ListResult) result.get("main-menu");

		RequestView.RequestMenu selectedItem = RequestView.RequestMenu.valueOf(resultItem.getSelectedId());

		if ((activeUser.getRole() != User.Role.ADMIN && activeUser.getRole() != User.Role.PERSONAL) &&
		    (selectedItem == RequestView.RequestMenu.FIND_REQUEST_BY_STATUS ||
			  selectedItem == RequestView.RequestMenu.EDITING_REQUEST ||
			  selectedItem == RequestView.RequestMenu.FIND_BY_ANIMAL ||
			  selectedItem == RequestView.RequestMenu.FIND_BY_USER)) {
			System.out.println("У вас немає дозволу на виконання цієї операції.");
			MainMenuView mainMenuView = new MainMenuView(activeUser);
			mainMenuView.render();
		} else {
			process(selectedItem);
		}

		System.out.print("\033[H\033[2J");
	}
	/**
	 * Enum representing various actions in the request menu.
	 */
	enum RequestMenu {
		VIEW_ALL_REQUEST("Перегляд всіх запитів"),
		ADD_REQUEST("Додати запит"),
		FIND_REQUEST_BY_STATUS("Пошук запиту по статусу"),
		EDITING_REQUEST("Редагування запиту"),
		FIND_BY_ANIMAL("Пошук запиту за твариною"),
		FIND_BY_USER("Пошук запиту за юзером"),
		BACK("Повернутись назад");
		private final String name;
		/**
		 * Constructor for RequestMenu enum.
		 *
		 * @param name The display name of the menu item.
		 */
		RequestMenu(String name) {
			this.name = name;
		}
		/**
		 * Returns the display name of the menu item.
		 *
		 * @return The display name of the menu item.
		 */
		public String getName() {
			return name;
		}
	}
}