package com.zoi4erom.animalnetworkbook.aui;

import static com.zoi4erom.animalnetworkbook.aui.AnimalView.AnimalMenu.*;
import static java.lang.System.out;

import com.zoi4erom.animalnetworkbook.businesslogic.AnimalService;
import com.zoi4erom.animalnetworkbook.businesslogic.ShelterService;
import com.zoi4erom.animalnetworkbook.persistence.entity.Animal;
import com.zoi4erom.animalnetworkbook.persistence.entity.Shelter;
import com.zoi4erom.animalnetworkbook.persistence.entity.User;
import de.codeshelf.consoleui.prompt.ConsolePrompt;
import de.codeshelf.consoleui.prompt.InputResult;
import de.codeshelf.consoleui.prompt.ListResult;
import de.codeshelf.consoleui.prompt.builder.ListPromptBuilder;
import de.codeshelf.consoleui.prompt.builder.PromptBuilder;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class AnimalView implements Renderable{
	private User activeUser;
	public AnimalView(User activeUser) {
		this.activeUser = activeUser;
	}
	public static String animalName() throws IOException {
		ConsolePrompt prompt = new ConsolePrompt();
		PromptBuilder promptBuilder = prompt.getPromptBuilder();
		promptBuilder.createInputPrompt()
		    .name("animalName")
		    .message("Впишіть ім'я тварини: ")
		    .addPrompt();

		var result = prompt.prompt(promptBuilder.build());
		var animalNameInput = (InputResult) result.get("animalName");
		return animalNameInput.getInput();
	}
	public static String animalSpecies() throws IOException {
		ConsolePrompt prompt = new ConsolePrompt();
		PromptBuilder promptBuilder = prompt.getPromptBuilder();
		promptBuilder.createInputPrompt()
		    .name("animalSpecies")
		    .message("Впишіть вид тварини: ")
		    .addPrompt();

		var result = prompt.prompt(promptBuilder.build());
		var speciesInput = (InputResult) result.get("animalSpecies");
		return speciesInput.getInput();
	}
	public static String animalBreed() throws IOException {
		ConsolePrompt prompt = new ConsolePrompt();
		PromptBuilder promptBuilder = prompt.getPromptBuilder();
		promptBuilder.createInputPrompt()
		    .name("animalBreed")
		    .message("Впишіть породу тварини: ")
		    .addPrompt();

		var result = prompt.prompt(promptBuilder.build());
		var breedInput = (InputResult) result.get("animalBreed");
		return breedInput.getInput();
	}
	public static int animalAge() throws IOException {
		ConsolePrompt prompt = new ConsolePrompt();
		PromptBuilder promptBuilder = prompt.getPromptBuilder();
		promptBuilder.createInputPrompt()
		    .name("animalAge")
		    .message("Впишіть вік тварини: ")
		    .addPrompt();

		var result = prompt.prompt(promptBuilder.build());
		var ageInput = (InputResult) result.get("animalAge");
		return Integer.parseInt(ageInput.getInput());
	}
	public static Shelter selectShelterFromList() throws IOException {
		List<Shelter> allShelters = ShelterService.getAllShelters();

		if (allShelters.isEmpty()) {
			System.out.println("Немає доступних приютів.");
			return null;
		}

		try {
			ConsolePrompt prompt = new ConsolePrompt();
			PromptBuilder promptBuilder = prompt.getPromptBuilder();

			ListPromptBuilder listPromptBuilder = promptBuilder.createListPrompt();
			listPromptBuilder
			    .name("shelterList")
			    .message("Виберіть приют за номером:");

			for (Shelter shelter : allShelters) {
				listPromptBuilder.newItem().text(shelter.getName()).add();
			}

			listPromptBuilder.addPrompt();

			var result = prompt.prompt(promptBuilder.build());
			ListResult listResult = (ListResult) result.get("shelterList");

			if (listResult != null) {
				Shelter selectedShelter = getSelectedShelter(allShelters,
				    listResult.getSelectedId());

				if (selectedShelter != null) {
					return selectedShelter;
				} else {
					System.out.println("Вибір приюту скасовано або виникла помилка.");
					return null;
				}
			} else {
				System.out.println("Вибір приюту скасовано або виникла помилка.");
				return null;
			}
		}catch (Exception e){
			out.println(e);
		}
		return null;
	}
	private static Shelter getSelectedShelter(List<Shelter> allShelters, Object selectedId) {
		if (selectedId instanceof String) {
			String selectedShelterName = (String) selectedId;
			return allShelters.stream()
			    .filter(shelter -> shelter.getName().equals(selectedShelterName))
			    .findFirst()
			    .orElse(null);
		} else if (selectedId instanceof Shelter) {
			return (Shelter) selectedId;
		} else {
			return null;
		}
	}
	public static UUID animalId() throws IOException {
		ConsolePrompt prompt = new ConsolePrompt();
		PromptBuilder promptBuilder = prompt.getPromptBuilder();
		promptBuilder.createInputPrompt()
		    .name("animalId")
		    .message("Впишіть унікальний ідентифікатор тварини: ")
		    .addPrompt();

		var result = prompt.prompt(promptBuilder.build());
		var idInput = (InputResult) result.get("animalId");
		return UUID.fromString(idInput.getInput());
	}
	private void editAnimal(Animal selectedAnimal) throws IOException {
		ConsolePrompt prompt = new ConsolePrompt();
		PromptBuilder promptBuilder = prompt.getPromptBuilder();

		ListPromptBuilder listPromptBuilder = promptBuilder.createListPrompt();
		listPromptBuilder
		    .name("editOptions")
		    .message("Оберіть опцію для редагування:")
		    .newItem("1").text("Змінити ім'я").add()
		    .newItem("2").text("Змінити вид").add()
		    .newItem("3").text("Змінити породу").add()
		    .newItem("4").text("Змінити приют").add()
		    .newItem("5").text("Змінити вік").add()
		    .newItem("0").text("Завершити редагування").add()
		    .addPrompt();

		String option;
		do {
			var result = prompt.prompt(promptBuilder.build());
			ListResult listResult = (ListResult) result.get("editOptions");
			option = listResult.getSelectedId();

			switch (option) {
				case "1":
					String newName = animalName();
					selectedAnimal.setName(newName);
					break;
				case "2":
					String newSpecies = animalSpecies();
					selectedAnimal.setSpecies(newSpecies);
					break;
				case "3":
					String newBreed = animalBreed();
					selectedAnimal.setBreed(newBreed);
					break;
				case "4":
					Shelter oldShelter = selectedAnimal.getShelter();
					Shelter newShelter = selectShelterFromList();
					if (newShelter != null && !newShelter.equals(oldShelter)) {

						AnimalService.decrementNumbersOfAnimalsInShelter(oldShelter);

						selectedAnimal.setShelter(newShelter);

						newShelter.setNumbersOfAnimals(newShelter.getNumbersOfAnimals() + 1);
					} else {
						System.out.println("Помилка: Новий приют не обраний або вже заповнений.");
					}
					break;

				case "5":
					int newAge = animalAge();
					selectedAnimal.setAge(newAge);
					break;
			}

		} while (!option.equals("0"));

		AnimalService.updateAnimal(selectedAnimal);

		System.out.println("\nЗмінена тварина:\n " + selectedAnimal);
	}
	private Animal selectAnimalFromList() throws IOException {
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
			    .message("Виберіть тварину за номером:");

			for (Animal animal : allAnimals) {
				String displayText = String.format("%s(%s-%s, Вік: %d років)", animal.getName(), animal.getSpecies(), animal.getBreed(), animal.getAge());
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
				} else {
					System.out.println("Вибір тварини скасовано або виникла помилка.");
					return null;
				}
			} else {
				System.out.println("Вибір тварини скасовано або виникла помилка.");
				return null;
			}
		} catch (Exception e) {
			out.println(e);
		}
		return null;
	}
	private static Animal getSelectedAnimal(List<Animal> allAnimals, String selectedAnimalText) {
		if (selectedAnimalText != null) {
			for (Animal animal : allAnimals) {
				String displayText = String.format("%s(%s-%s, Вік: %d років)",
				    animal.getName(), animal.getSpecies(), animal.getBreed(), animal.getAge());
				if (displayText.equals(selectedAnimalText.trim())) {
					return animal;
				}
			}
		}
		return null;
	}
	private void process(AnimalMenu selectedItem) throws IOException {
		switch (selectedItem) {
			case VIEW_ALL_ANIMAL -> {
				List<Animal> animals = AnimalService.getAllAnimals();

				if (animals != null && !animals.isEmpty()) {
					System.out.print("\033[H\033[2J");
					out.println("Знайдені тварини: ");
					for (Animal animal : animals) {
						out.println(animal);
					}
				} else {
					System.out.print("\033[H\033[2J");
					out.println("В системі ще немає тварин.");
				}
				render();
			}
			case FIND_ANIMAL -> {
				System.out.print("\033[H\033[2J");
				renderSearch();
			}
			case ADD_ANIMAL -> {
				System.out.print("\033[H\033[2J");
				List<String> errors = AnimalService.addAnimalValidation(animalName(), animalAge(), animalSpecies(), animalBreed(), selectShelterFromList());
				if (!errors.isEmpty()) {
					System.out.println("Помилки при додаванні тварини:");
					errors.forEach(System.out::println);
				} else {
					System.out.print("\033[H\033[2J");
					out.println("Тварина успішно додана!");
				}
				render();
			}
			case EDIT_ANIMAL -> {
				System.out.print("\033[H\033[2J");
				Animal selectedAnimal = selectAnimalFromList();
				if (selectedAnimal != null) {
					editAnimal(selectedAnimal);
				} else {
					out.println("Вибір тварини скасовано або виникла помилка.");
				}
				render();
			}
			case DELETE_ANIMAL_BY_NAME -> {
				System.out.print("\033[H\033[2J");

				String animalId = String.valueOf(animalId());
				boolean deletionSuccessful = AnimalService.deleteAnimalById(
				    UUID.fromString(animalId));

				if (deletionSuccessful) {
					out.println("Тварину з ID " + animalId + " успішно видалено.");
				} else {
					out.println("Не вдалося видалити тварину з ID " + animalId + ". Можливо, тварина з таким ID не існує.");
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
	private void processSearch(AnimalMenu selectedItem) throws IOException {
		switch (selectedItem) {
			case FIND_BY_ANIMAL_BY_NAME -> {
				System.out.print("\033[H\033[2J");
				Animal animal = AnimalService.findAnimalByName(animalName());
				if(animal != null){
					out.println(animal);
				}else{
					out.println("Тварин по цьому імені не знайдено");
				}
				renderSearch();
			}
			case FIND_BY_SPECIES -> {
				System.out.print("\033[H\033[2J");
				List<Animal> animals = AnimalService.searchBySpecies(animalSpecies());
				if(!animals.isEmpty()){
					out.println("Знайдені тварини: ");
					for (Animal animal: animals){
						out.println(animal);
					}
				}else {
					System.out.print("\033[H\033[2J");
					out.println("Тварин по цьому виду не знайдено!");
				}
				renderSearch();
			}
			case FIND_BY_BREED -> {
				System.out.print("\033[H\033[2J");
				List<Animal> animals = AnimalService.searchByBreed(animalBreed());
				if(!animals.isEmpty()){
					out.println("Знайдені тварини: ");
					for (Animal animal: animals){
						out.println(animal);
					}
				}else {
					System.out.print("\033[H\033[2J");
					out.println("Тварин по цій породі не знайдено!");
				}
				renderSearch();
			}
			case FIND_BY_AGE -> {
				System.out.print("\033[H\033[2J");
				List<Animal> animals = AnimalService.findAnimalByYear(animalAge());
				if(!animals.isEmpty()){
					out.println("Знайдені тварини: ");
					for (Animal animal: animals){
						out.println(animal);
					}
				}else {
					System.out.print("\033[H\033[2J");
					out.println("Тварин по цьому віку не знайдено!");
				}
				renderSearch();
			}
			case FIND_BY_SHELTER -> {
				System.out.print("\033[H\033[2J");
				List<Animal> animals = AnimalService.searchByShelter(selectShelterFromList());
				if(!animals.isEmpty()){
					out.println("Знайдені тварини: ");
					for (Animal animal: animals){
						out.println(animal);
					}
				}else {
					System.out.print("\033[H\033[2J");
					out.println("Тварин по даному приюту не знайдено! Можливо в приюті немає тварин!");
				}
				renderSearch();
			}
			case BACK -> {
				System.out.print("\033[H\033[2J");
				render();
			}
		}
	}
	@Override
	public void render() throws IOException {
		ConsolePrompt prompt = new ConsolePrompt();
		PromptBuilder promptBuilder = prompt.getPromptBuilder();

		promptBuilder.createListPrompt()
		    .name("main-menu")
		    .message("Моніторинг тварин")
		    .newItem(VIEW_ALL_ANIMAL.toString()).text(VIEW_ALL_ANIMAL.getName()).add()
		    .newItem(FIND_ANIMAL.toString()).text(FIND_ANIMAL.getName()).add()
		    .newItem(ADD_ANIMAL.toString()).text(ADD_ANIMAL.getName()).add()
		    .newItem(EDIT_ANIMAL.toString()).text(EDIT_ANIMAL.getName()).add()
		    .newItem(DELETE_ANIMAL_BY_NAME.toString()).text(DELETE_ANIMAL_BY_NAME.getName()).add()
		    .newItem(BACK.toString()).text(BACK.getName()).add()
		    .addPrompt();

		var result = prompt.prompt(promptBuilder.build());
		ListResult resultItem = (ListResult) result.get("main-menu");

		AnimalView.AnimalMenu selectedItem = AnimalView.AnimalMenu.valueOf(
		    resultItem.getSelectedId());

		if ((activeUser.getRole() != User.Role.ADMIN && activeUser.getRole() != User.Role.PERSONAL) &&
		    (selectedItem == AnimalView.AnimalMenu.ADD_ANIMAL ||
			  selectedItem == AnimalView.AnimalMenu.EDIT_ANIMAL ||
			  selectedItem == AnimalView.AnimalMenu.DELETE_ANIMAL_BY_NAME)) {
			System.out.println("У вас немає дозволу на виконання цієї операції.");
			MainMenuView mainMenuView = new MainMenuView(activeUser);
			mainMenuView.render();
		} else {
			process(selectedItem);
		}

		System.out.print("\033[H\033[2J");
	}
	public void renderSearch() throws IOException {
		ConsolePrompt prompt = new ConsolePrompt();
		PromptBuilder promptBuilder = prompt.getPromptBuilder();

		promptBuilder.createListPrompt()
		    .name("search-menu")
		    .message("Моніторинг пошуку тварин")
		    .newItem(FIND_BY_ANIMAL_BY_NAME.toString()).text(FIND_BY_ANIMAL_BY_NAME.getName())
		    .add()
		    .newItem(FIND_BY_SPECIES.toString()).text(FIND_BY_SPECIES.getName())
		    .add()
		    .newItem(FIND_BY_BREED.toString()).text(FIND_BY_BREED.getName())
		    .add()
		    .newItem(FIND_BY_AGE.toString()).text(FIND_BY_AGE.getName())
		    .add()
		    .newItem(FIND_BY_SHELTER.toString()).text(FIND_BY_SHELTER.getName())
		    .add()
		    .newItem(BACK.toString()).text(BACK.getName()).add()
		    .addPrompt();

		var result = prompt.prompt(promptBuilder.build());
		ListResult resultItem = (ListResult) result.get("search-menu");

		AnimalView.AnimalMenu selectedItem = AnimalView.AnimalMenu.valueOf(
		    resultItem.getSelectedId());
		processSearch(selectedItem);

		System.out.print("\033[H\033[2J");
	}
	enum AnimalMenu {
		VIEW_ALL_ANIMAL("Перегляд всіх тварин"),
		FIND_ANIMAL("Меню пошуку тварин"),
		ADD_ANIMAL("Додати тварину"),
		EDIT_ANIMAL("Редагування тварин"),
		DELETE_ANIMAL_BY_NAME("Видалити тварину за унікальним айді"),
		BACK("Повернутись назад"),
		FIND_BY_ANIMAL_BY_NAME("Пошук за назвою"),
		FIND_BY_SPECIES("Пошук за видом тварини"),
		FIND_BY_BREED("Пошук за породою"),
		FIND_BY_AGE("Пошук за роками"),
		FIND_BY_SHELTER("Пошук за приютом");
		private final String name;
		AnimalMenu(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
	}
}