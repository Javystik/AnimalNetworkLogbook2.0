package com.zoi4erom.animalnetworkbook.aui;

import static com.zoi4erom.animalnetworkbook.aui.ShelterView.ShelterMenu.*;
import static java.lang.System.err;
import static java.lang.System.out;

import com.zoi4erom.animalnetworkbook.businesslogic.ShelterService;
import com.zoi4erom.animalnetworkbook.persistence.entity.Shelter;
import com.zoi4erom.animalnetworkbook.persistence.entity.User;
import de.codeshelf.consoleui.prompt.ConsolePrompt;
import de.codeshelf.consoleui.prompt.InputResult;
import de.codeshelf.consoleui.prompt.ListResult;
import de.codeshelf.consoleui.prompt.builder.ListPromptBuilder;
import de.codeshelf.consoleui.prompt.builder.PromptBuilder;
import java.io.IOException;
import java.util.List;
/**
 * The `ShelterView` class provides user interface functionality related to shelters in an animal network book application.
 * It implements the `Renderable` interface to handle rendering tasks.
 *
 * @author Zoi4erom
 * @since 2024-02-02
 */
public class ShelterView implements Renderable{
	/**
	 * Constructs a new {@code UserView} with the specified active user.
	 *
	 * @param activeUser The user interacting with the user view.
	 */
	private static User activeUser;

	/**
	 * Constructs a new `ShelterView` instance with the specified active user.
	 *
	 * @param activeUser The active user interacting with the shelter view.
	 */
	public ShelterView(User activeUser) {
		this.activeUser = activeUser;
	}

	/**
	 * Prompts the user to input the name of a shelter.
	 *
	 * @return The name of the shelter entered by the user.
	 * @throws IOException If an I/O error occurs during the input process.
	 */
	public static String shelterName() throws IOException {
		ConsolePrompt prompt = new ConsolePrompt();
		PromptBuilder promptBuilder = prompt.getPromptBuilder();
		promptBuilder.createInputPrompt()
		    .name("shelterName")
		    .message("Впишіть назву притулку: ")
		    .addPrompt();

		var result = prompt.prompt(promptBuilder.build());
		var productNameInput = (InputResult) result.get("shelterName");
		return productNameInput.getInput();
	}
	/**
	 * Prompts the user to input the phone number of a shelter.
	 *
	 * @return The phone number of the shelter entered by the user.
	 * @throws IOException If an I/O error occurs during the input process.
	 */
	public static String shelterPhoneNumber() throws IOException {
		ConsolePrompt prompt = new ConsolePrompt();
		PromptBuilder promptBuilder = prompt.getPromptBuilder();
		promptBuilder.createInputPrompt()
		    .name("shelterPhoneNumber")
		    .message("Впишіть номер телефону притулку: ")
		    .addPrompt();

		var result = prompt.prompt(promptBuilder.build());
		var phoneNumberInput = (InputResult) result.get("shelterPhoneNumber");
		return phoneNumberInput.getInput();
	}
	/**
	 * Prompts the user to input the maximum capacity of animals in the shelter.
	 * Validates that the input consists only of digits.
	 *
	 * @return The maximum capacity of animals entered by the user.
	 * @throws IOException If an I/O error occurs.
	 */
	public static String shelterCapacityOfAnimals() throws IOException {
		String input;
		do {
			ConsolePrompt prompt = new ConsolePrompt();
			PromptBuilder promptBuilder = prompt.getPromptBuilder();
			promptBuilder.createInputPrompt()
			    .name("сapacityOfAnimals")
			    .message("Впишіть максимальну кількість тварин: ")
			    .addPrompt();

			var result = prompt.prompt(promptBuilder.build());
			var productNameInput = (InputResult) result.get("сapacityOfAnimals");
			input = productNameInput.getInput();

			if (!input.matches("\\d+")) {
				System.out.println("Помилка: Введене значення має містити лише цифри.");
			}
		} while (!input.matches("\\d+"));

		return input;
	}
	/**
	 * Prompts the user to input the address of the shelter.
	 * Validates that the input is not empty.
	 *
	 * @return The address entered by the user.
	 * @throws IOException If an I/O error occurs.
	 */
	public static String shelterAddress() throws IOException {
		String input;
		do {
			ConsolePrompt prompt = new ConsolePrompt();
			PromptBuilder promptBuilder = prompt.getPromptBuilder();
			promptBuilder.createInputPrompt()
			    .name("shelterAddress")
			    .message("Впишіть адресу притулку: ")
			    .addPrompt();

			var result = prompt.prompt(promptBuilder.build());
			var addressInput = (InputResult) result.get("shelterAddress");
			input = addressInput.getInput();

			if (input.isEmpty()) {
				System.out.println("Помилка: Адреса не може бути порожньою.");
			}

		} while (input.isEmpty());

		return input;
	}
	/**
	 * Parses the shelter capacity from the input string.
	 *
	 * @param input The input string to parse.
	 * @return The parsed shelter capacity, or 0 if parsing fails.
	 */

	private static int parseShelterCapacity(String input) {
		try {
			if (input.matches("\\d+")) {
				return Integer.parseInt(input);
			} else {
				throw new NumberFormatException("Введене значення має містити лише цифри.");
			}
		} catch (NumberFormatException e) {
			err.println("Помилка парсингу: " + e.getMessage());
			return 0;
		}
	}
	/**
	 * Prompts the user to select a shelter from the list of available shelters.
	 *
	 * @return The selected shelter, or null if the selection is canceled or an error occurs.
	 * @throws IOException If an I/O error occurs.
	 */
	public static Shelter selectShelterFromList() throws IOException {
		List<Shelter> allShelters = ShelterService.getAllShelters();

		if (allShelters.isEmpty()) {
			System.out.println("Немає доступних притулку.");
			return null;
		}

		try {
			ConsolePrompt prompt = new ConsolePrompt();
			PromptBuilder promptBuilder = prompt.getPromptBuilder();

			ListPromptBuilder listPromptBuilder = promptBuilder.createListPrompt();
			listPromptBuilder
			    .name("shelterList")
			    .message("Виберіть притулок:");

			for (Shelter shelter : allShelters) {
				listPromptBuilder.newItem().text(shelter.getName()).add();
			}

			listPromptBuilder.addPrompt();

			var result = prompt.prompt(promptBuilder.build());
			ListResult listResult = (ListResult) result.get("shelterList");

			if (listResult != null) {
				return getSelectedShelter(allShelters, listResult.getSelectedId());
			} else {
				System.out.println("Вибір притулку скасовано або виникла помилка.");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Gets the selected shelter from the list of shelters based on the selectedId.
	 *
	 * @param allShelters The list of all shelters.
	 * @param selectedId  The selected identifier (shelter name in this case).
	 * @return The selected shelter, or null if not found.
	 */
	private static Shelter getSelectedShelter(List<Shelter> allShelters, Object selectedId) {
		if (selectedId instanceof String) {
			String selectedShelterName = (String) selectedId;
			return allShelters.stream()
			    .filter(shelter -> shelter.getName().equals(selectedShelterName))
			    .findFirst()
			    .orElse(null);
		}
		return null;
	}
	/**
	 * Allows the user to edit the properties of a selected shelter interactively.
	 * The user can choose to change the shelter's name, address, phone number, or capacity.
	 * The editing process continues until the user chooses to finish (option 0).
	 *
	 * @param selectedShelter The shelter to be edited.
	 * @throws IOException If an I/O error occurs.
	 */
	private static void editShelter(Shelter selectedShelter) throws IOException {
		ConsolePrompt prompt = new ConsolePrompt();
		PromptBuilder promptBuilder = prompt.getPromptBuilder();

		ListPromptBuilder listPromptBuilder = promptBuilder.createListPrompt();
		listPromptBuilder
		    .name("editShelterOptions")
		    .message("Оберіть опцію для редагування:")
		    .newItem("1").text("Змінити назву").add()
		    .newItem("2").text("Змінити адресу").add()
		    .newItem("3").text("Змінити телефон").add()
		    .newItem("4").text("Змінити місткість").add()
		    .newItem("0").text("Завершити редагування").add()
		    .addPrompt();

		String option;
		do {
			var result = prompt.prompt(promptBuilder.build());
			ListResult listResult = (ListResult) result.get("editShelterOptions");
			option = listResult.getSelectedId();

			switch (option) {
				case "1":
					String newName = shelterName();
					selectedShelter.setName(newName);
					break;
				case "2":
					String newAddress = shelterAddress();
					selectedShelter.setAddress(newAddress);
					break;
				case "3":
					String newPhone = shelterPhoneNumber();
					selectedShelter.setPhone(newPhone);
					break;
				case "4":
					int newCapacity = Integer.parseInt(shelterCapacityOfAnimals());
					selectedShelter.setCapacityOfAnimals(newCapacity);
					break;
			}

		} while (!option.equals("0"));

		ShelterService.updateShelter(selectedShelter);

		System.out.println("\nЗмінений притулок:\n " + selectedShelter);
	}
	/**
	 * Processes the selected item from the ShelterMenu.
	 *
	 * @param selectedItem The selected item from the ShelterMenu enum.
	 * @throws IOException If an I/O error occurs.
	 */
	private void process(ShelterMenu selectedItem) throws IOException {
		switch (selectedItem) {
			case VIEW_ALL_SHELTER -> {
				System.out.print("\033[H\033[2J");
				List<Shelter> shelters = ShelterService.getAllShelters();
				if(!shelters.isEmpty()){
					System.out.print("\033[H\033[2J");
					out.println("Знайдені притулки: ");
					for (Shelter shelter: shelters){
						out.println("-" + shelter);
					}
				}else{
					out.println("В системі не знайдено притулку!");
				}
				render();
			}
			case FIND_SHELTER -> {
				System.out.print("\033[H\033[2J");
				renderSearch();
			}
			case ADD_SHELTER -> {
				System.out.print("\033[H\033[2J");
				List<String> errors = ShelterService.createShelterValidation(shelterName(), shelterAddress(), shelterPhoneNumber(), parseShelterCapacity(shelterCapacityOfAnimals()));
				if(!errors.isEmpty()){
					System.out.println("Помилки при створенні притулку:");
					errors.forEach(System.out::println);
				}else{
					System.out.print("\033[H\033[2J");
					out.println("Притулок успішно створено!");
				}
				render();
			}
			case EDD_SHELTER -> {
				Shelter selectedShelter = selectShelterFromList();
				if (selectedShelter != null) {
					editShelter(selectedShelter);
				} else {
					out.println("Вибір тварини скасовано або виникла помилка.");
				}
				render();
			}

			case DELETE_SHELTER_BY_NAME -> {
				System.out.print("\033[H\033[2J");

				if (!ShelterService.getAllShelters().isEmpty()) {
					String shelterName = shelterName();

					boolean isDeleted = ShelterService.deleteShelterByName(shelterName);

					if (isDeleted) {
						out.println("Притулок успішно видалено!");
					} else {
						out.println("Не можна видаляти притулку в якому є тварина!");
					}
				} else {
					out.println("В мережі ще немає притулків, які можна було б видалити!");
				}

				render();
			}
			case BACK -> {
				System.out.print("\033[H\033[2J");
				User activeUser = ShelterView.getActiveUser();
				MainMenuView mainMenuView = new MainMenuView(activeUser);
				mainMenuView.render();
			}
		}
	}

	public static User getActiveUser() {
		return activeUser;
	}

	/**
	 * Renders the main menu for monitoring shelters and processes user input.
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
		    .message("Моніторинг притулків")
		    .newItem(VIEW_ALL_SHELTER.toString()).text(VIEW_ALL_SHELTER.getName()).add()
		    .newItem(FIND_SHELTER.toString()).text(FIND_SHELTER.getName()).add()
		    .newItem(ADD_SHELTER.toString()).text(ADD_SHELTER.getName()).add()
		    .newItem(EDD_SHELTER.toString()).text(EDD_SHELTER.getName()).add()
		    .newItem(DELETE_SHELTER_BY_NAME.toString()).text(DELETE_SHELTER_BY_NAME.getName()).add()
		    .newItem(BACK.toString()).text(BACK.getName()).add()
		    .addPrompt();

		var result = prompt.prompt(promptBuilder.build());
		ListResult resultItem = (ListResult) result.get("main-menu");

		ShelterView.ShelterMenu selectedItem = ShelterView.ShelterMenu.valueOf(resultItem.getSelectedId());

		if ((activeUser.getRole() != User.Role.ADMIN && activeUser.getRole() != User.Role.PERSONAL) &&
		    (selectedItem == ShelterView.ShelterMenu.ADD_SHELTER ||
			  selectedItem == ShelterView.ShelterMenu.EDD_SHELTER ||
			  selectedItem == ShelterView.ShelterMenu.DELETE_SHELTER_BY_NAME)) {
			System.out.println("У вас немає дозволу на виконання цієї операції.");
			MainMenuView mainMenuView = new MainMenuView(activeUser);
			mainMenuView.render();
		} else {
			process(selectedItem);
		}

		System.out.print("\033[H\033[2J");
	}
	/**
	 * Renders the search menu for monitoring shelters and processes user input.
	 * This method uses a console prompt for user interaction.
	 *
	 * @throws IOException if an I/O error occurs.
	 */
	public void renderSearch() throws IOException {
		ConsolePrompt prompt = new ConsolePrompt();
		PromptBuilder promptBuilder = prompt.getPromptBuilder();

		promptBuilder.createListPrompt()
		    .name("search-menu")
		    .message("Моніторинг пошуку притулків")
		    .newItem(FIND_BY_SHELTER_BY_NAME.toString()).text(FIND_BY_SHELTER_BY_NAME.getName())
		    .add()
		    .newItem(FIND_BY_CAPACITY_OF_ANIMALS.toString()).text(FIND_BY_CAPACITY_OF_ANIMALS.getName())
		    .add()
		    .newItem(FIND_BY_PHONE_NUMBER.toString()).text(FIND_BY_PHONE_NUMBER.getName())
		    .add()
		    .newItem(FIND_BY_ADDRESS.toString()).text(FIND_BY_ADDRESS.getName())
		    .add()
		    .newItem(BACK.toString()).text(BACK.getName()).add()
		    .addPrompt();

		var result = prompt.prompt(promptBuilder.build());
		ListResult resultItem = (ListResult) result.get("search-menu");

		ShelterView.ShelterMenu selectedItem = ShelterView.ShelterMenu.valueOf(
		    resultItem.getSelectedId());
		processSearch(selectedItem);

		System.out.print("\033[H\033[2J");
	}
	/**
	 * Processes user input related to shelter search operations based on the selected menu item.
	 *
	 * @param selectedItem The selected menu item from the ShelterMenu enum.
	 * @throws IOException if an I/O error occurs.
	 */
	private void processSearch(ShelterMenu selectedItem) throws IOException {
		switch (selectedItem) {
			case FIND_BY_SHELTER_BY_NAME -> {
				String shelterName = shelterName();
				Shelter shelter = ShelterService.findShelterByName(shelterName);

				if (shelter != null) {
					out.println("Притулок знайдено: " + shelter);
				} else {
					System.out.print("\033[H\033[2J");
					out.println("Притулок по імені: '" + shelterName + "' не знайдено.");
				}
				renderSearch();
			}
			case FIND_BY_CAPACITY_OF_ANIMALS -> {
				int shelterCapacity = parseShelterCapacity(shelterCapacityOfAnimals());
				if (shelterCapacity == 0) {
					out.println("Максимальний розмір вмістимості не може бути 0!");
				} else {
					Shelter shelter = ShelterService.findShelterByMaxAnimals(shelterCapacity);

					if (shelter != null) {
						System.out.print("\033[H\033[2J");
						out.println("Притулок знайдено: " + shelter);
					} else {
						System.out.print("\033[H\033[2J");
						out.println("Притулок з даною вмістимостю тварин не знайдено.");
					}
				}
				renderSearch();
			}
			case FIND_BY_PHONE_NUMBER -> {
				Shelter shelter = ShelterService.findShelterByPhoneNumber(shelterPhoneNumber());

				if (shelter != null) {
					System.out.print("\033[H\033[2J");
					out.println("Притулок знайдено: " + shelter);
				} else {
					System.out.print("\033[H\033[2J");
					out.println("Притулку по данному номеру телефону не знайдено.");
				}
				renderSearch();
			}
			case FIND_BY_ADDRESS -> {
				Shelter shelter = ShelterService.findShelterByAddress(shelterAddress());

				if (shelter != null) {
					System.out.print("\033[H\033[2J");
					out.println("Притулок знайдено: " + shelter);
				} else {
					System.out.print("\033[H\033[2J");
					out.println("Притулок за вказаною адресою не знайдено.");
				}
				renderSearch();
			}
			case BACK -> {
				System.out.print("\033[H\033[2J");
				render();
			}
		}
	}
	/**
	 * The {@code ShelterMenu} enum represents the different options in the shelter menu.
	 * It includes options for viewing, adding, editing, and searching shelters.
	 */
	enum ShelterMenu {
		VIEW_ALL_SHELTER("Перегляд всіх притулків"),
		FIND_SHELTER("Меню пошуку притулків"),
		ADD_SHELTER("Додати притулків"),
		EDD_SHELTER("Редагувати притулок"),
		DELETE_SHELTER_BY_NAME("Видалити притулок за ім'ям"),
		BACK("Повернутись назад"),
		FIND_BY_SHELTER_BY_NAME("Пошук за назвою"),
		FIND_BY_CAPACITY_OF_ANIMALS("Пошук за максимальною кількістю тварин"),
		FIND_BY_PHONE_NUMBER("Пошук за номером телефону"),
		FIND_BY_ADDRESS("Пошук за адресою");
		private final String name;
		ShelterMenu(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
	}
}