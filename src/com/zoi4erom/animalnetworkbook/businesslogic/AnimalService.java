package com.zoi4erom.animalnetworkbook.businesslogic;

import static com.zoi4erom.animalnetworkbook.businesslogic.ShelterService.shelters;

import com.zoi4erom.animalnetworkbook.businesslogic.exception.ExceptionTemplate;
import com.zoi4erom.animalnetworkbook.persistence.entity.Animal;
import com.zoi4erom.animalnetworkbook.persistence.entity.Shelter;
import com.zoi4erom.animalnetworkbook.persistence.jsonhandler.JsonConverter;
import com.zoi4erom.animalnetworkbook.persistence.jsonhandler.JsonPaths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AnimalService {
	private static final List<String> errors = new ArrayList<>();
	private AnimalService() {
	}
	public static List<String> addAnimalValidation(String name, int age, String species, String breed, Shelter shelter) {
		errors.clear();

		LocalDate localDate = LocalDate.now(ZoneId.systemDefault());

		isValidName(name);
		isValidAge(age);
		isValidSpecies(species);
		isValidBreed(breed);
		isValidDateOfDelivery(String.valueOf(localDate));
		isValidShelter(shelter);

		if (!errors.isEmpty()) {
			return errors;
		}

		addAnimal(name, age, species, breed, localDate, shelter);

		return errors;
	}
	private static void addAnimal(String name, int age, String species, String breed, LocalDate dateOfDelivery, Shelter shelter) {
		List<Animal> animals = animals();
		List<Shelter> shelters = shelters();

		Shelter selectedShelter = null;
		for (Shelter s : shelters) {
			if (s.equals(shelter)) {
				selectedShelter = s;
				break;
			}
		}

		if (selectedShelter == null) {
			System.out.println("Помилка: Вказаний притулок не існує.");
			return;
		}

		if (selectedShelter.getNumbersOfAnimals() >= selectedShelter.getCapacityOfAnimals()) {
			System.out.println("Помилка: В приюті немає вільних місць для тварини.");
			return;
		}

		Animal animal = new Animal(UUID.randomUUID(), name, age, species, breed, dateOfDelivery, selectedShelter);
		animals.add(animal);

		selectedShelter.setNumbersOfAnimals(selectedShelter.getNumbersOfAnimals() + 1);

		JsonConverter.serialization(animals, JsonPaths.ANIMAL);
		JsonConverter.serialization(shelters, JsonPaths.SHELTERS);
	}
	public static Animal updateAnimal(Animal updatedAnimal) {
		List<Animal> allAnimals = AnimalService.getAllAnimals();

		allAnimals.removeIf(animal -> animal.getId().equals(updatedAnimal.getId()));

		allAnimals.add(updatedAnimal);

		JsonConverter.serialization(allAnimals, JsonPaths.ANIMAL);

		ShelterService.updateShelter(updatedAnimal.getShelter());

		return updatedAnimal;
	}
	public static void decrementNumbersOfAnimals(Shelter shelter) {
		if (shelter != null) {
			int currentNumbersOfAnimals = shelter.getNumbersOfAnimals();
			if (currentNumbersOfAnimals > 0) {
				shelter.setNumbersOfAnimals(currentNumbersOfAnimals - 1);
				ShelterService.updateShelter(shelter);
			}
		}
	}
	public static void decrementNumbersOfAnimalsInShelter(Shelter shelter) {
		List<Animal> animalsInShelter = searchByShelter(shelter);

		for (Animal animal : animalsInShelter) {
			decrementNumbersOfAnimals(animal.getShelter());
		}
	}
	public static List<Animal> searchBySpecies(String species) {
		return animals().stream()
		    .filter(animal -> animal.getSpecies().equalsIgnoreCase(species))
		    .collect(Collectors.toList());
	}
	public static Animal findAnimalByName(String name) {
		return animals().stream()
		    .filter(animal -> animal.getName().equalsIgnoreCase(name))
		    .findFirst()
		    .orElse(null);
	}
	public static boolean deleteAnimalById(UUID animalId) {
		List<Animal> animalList = animals();

		animalList.removeIf(animal -> animal.getId().equals(animalId));

		JsonConverter.serialization(animalList, JsonPaths.ANIMAL);
		return false;
	}
	public static List<Animal> findAnimalByYear(int birthYear) {
		return animals().stream()
		    .filter(animal -> animal.getDateOfDelivery().getYear() == birthYear)
		    .collect(Collectors.toList());
	}
	public static List<Animal> getAllAnimals() {
		List<Animal> animalsList = animals();
		return animalsList != null ? animalsList : new ArrayList<>();
	}
	public static List<Animal> searchByBreed(String breed) {
		return animals().stream()
		    .filter(animal -> animal.getBreed().equalsIgnoreCase(breed))
		    .collect(Collectors.toList());
	}
	public static List<Animal> searchByShelter(Shelter shelter) {
		return animals().stream()
		    .filter(animal -> animal.getShelter().equals(shelter))
		    .collect(Collectors.toList());
	}
	private static List<Animal> animals(){
		return JsonConverter.deserialization(JsonPaths.ANIMAL, Animal.class);
	}
	private static void isValidName(String name) {
		final String FIELD_NAME = "ім'я тварини";
		final int MIN_SIZE = 2;
		final int MAX_SIZE = 50;

		if (ValidatorServiceUtil.isFieldBlankValidate(name)) {
			errors.add(ExceptionTemplate.EMPTY_FIELD_EXCEPTION.getTemplate().formatted(FIELD_NAME));
		}
		if (ValidatorServiceUtil.isFieldSizeValidate(name, MIN_SIZE, MAX_SIZE)) {
			errors.add(ExceptionTemplate.TOO_SHORT_LONG_EXCEPTION.getTemplate()
			    .formatted(FIELD_NAME, MIN_SIZE, MAX_SIZE));
		}
	}
	private static void isValidAge(int age) {
		final String FIELD_AGE = "вік тварини";

		if (age <= 0) {
			errors.add("Invalid field: " + FIELD_AGE);
		}
	}
	private static void isValidSpecies(String species) {
		final String FIELD_SPECIES = "вид тварини";
		final int MIN_SIZE = 2;
		final int MAX_SIZE = 50;

		if (ValidatorServiceUtil.isFieldBlankValidate(species)) {
			errors.add(ExceptionTemplate.EMPTY_FIELD_EXCEPTION.getTemplate().formatted(FIELD_SPECIES));
		}
		if (ValidatorServiceUtil.isFieldSizeValidate(species, MIN_SIZE, MAX_SIZE)) {
			errors.add(ExceptionTemplate.TOO_SHORT_LONG_EXCEPTION.getTemplate()
			    .formatted(FIELD_SPECIES, MIN_SIZE, MAX_SIZE));
		}
	}
	private static void isValidBreed(String breed) {
		final String FIELD_BREED = "порода тварини";
		final int MIN_SIZE = 2;
		final int MAX_SIZE = 50;

		if (ValidatorServiceUtil.isFieldBlankValidate(breed)) {
			errors.add(ExceptionTemplate.EMPTY_FIELD_EXCEPTION.getTemplate().formatted(FIELD_BREED));
		}
		if (ValidatorServiceUtil.isFieldSizeValidate(breed, MIN_SIZE, MAX_SIZE)) {
			errors.add(ExceptionTemplate.TOO_SHORT_LONG_EXCEPTION.getTemplate()
			    .formatted(FIELD_BREED, MIN_SIZE, MAX_SIZE));
		}
	}
	private static void isValidDateOfDelivery(String dateOfDelivery) {
		final String FIELD_DATE_OF_DELIVERY = "дата доставки тварини";

		if (ValidatorServiceUtil.isFieldBlankValidate(dateOfDelivery)) {
			errors.add(ExceptionTemplate.EMPTY_FIELD_EXCEPTION.getTemplate().formatted(FIELD_DATE_OF_DELIVERY));
		}
	}
	private static void isValidShelter(Shelter shelter) {
		final String FIELD_SHELTER = "притулок для тварини";

		if (shelter == null) {
			errors.add("Invalid field: " + FIELD_SHELTER);
		}
	}
}