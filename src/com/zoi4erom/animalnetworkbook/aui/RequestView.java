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

public class RequestView implements Renderable{
	private User activeUser;
	public RequestView(User activeUser) {
		this.activeUser = activeUser;
	}
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
	private static Request getSelectedRequest(List<Request> allRequests, String selectedRequestText) {
		for (Request request : allRequests) {
			if (request.toString().equals(selectedRequestText)) {
				return request;
			}
		}
		return null;
	}
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
				User activeUser = AuthenticationView.getActiveUser();
				MainMenuView mainMenuView = new MainMenuView(activeUser);
				mainMenuView.render();
			}
		}
	}
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
	enum RequestMenu {
		VIEW_ALL_REQUEST("Перегляд всіх запитів"),
		ADD_REQUEST("Додати запит"),
		FIND_REQUEST_BY_STATUS("Пошук запиту по статусу"),
		EDITING_REQUEST("Редагування запиту"),
		FIND_BY_ANIMAL("Пошук запиту за твариною"),
		FIND_BY_USER("Пошук запиту за юзером"),
		BACK("Повернутись назад");
		private final String name;
		RequestMenu(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
	}
}