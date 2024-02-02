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

/**
 * The {@code AnimalService} class provides business logic for managing and interacting with animals in a shelter.
 * It includes methods for adding, updating, searching, and deleting animals, as well as validation checks for
 * various fields related to animals.
 * <p>
 * This class utilizes a {@code JsonConverter} for serialization and deserialization of animal data. The data is
 * stored in JSON format, and interactions with shelters are coordinated with the {@code ShelterService}.
 * <p>
 * The class follows a singleton pattern, and instances cannot be created as the constructor is private.
 *
 * @author Zoi4erom
 * @version 1.0
 * @since 2024-02-02
 */
public class AnimalService {

	private static final List<String> errors = new ArrayList<>();

	private AnimalService() {
	}

	/**
	 * Validates the provided information and adds a new animal to the system if validation passes.
	 *
	 * @param name     The name of the animal.
	 * @param age      The age of the animal.
	 * @param species  The species of the animal.
	 * @param breed    The breed of the animal.
	 * @param shelter  The shelter where the animal is located.
	 * @return A list of validation errors if any, or an empty list if the animal is added successfully.
	 */
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

	/**
	 * Private method to add a new animal to the system.
	 *
	 * @param name           The name of the animal.
	 * @param age            The age of the animal.
	 * @param species        The species of the animal.
	 * @param breed          The breed of the animal.
	 * @param dateOfDelivery The date of delivery for the animal.
	 * @param shelter        The shelter where the animal is located.
	 */
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
			System.out.println("Error: The specified shelter does not exist.");
			return;
		}

		if (selectedShelter.getNumbersOfAnimals() >= selectedShelter.getCapacityOfAnimals()) {
			System.out.println("Error: The shelter has no available space for the animal.");
			return;
		}

		Animal animal = new Animal(UUID.randomUUID(), name, age, species, breed, dateOfDelivery, selectedShelter);
		animals.add(animal);

		selectedShelter.setNumbersOfAnimals(selectedShelter.getNumbersOfAnimals() + 1);

		JsonConverter.serialization(animals, JsonPaths.ANIMAL);
		JsonConverter.serialization(shelters, JsonPaths.SHELTERS);
	}

	/**
	 * Updates information about an existing animal in the system.
	 *
	 * @param updatedAnimal The updated information for the animal.
	 * @return The updated animal object.
	 */
	public static Animal updateAnimal(Animal updatedAnimal) {
		List<Animal> allAnimals = AnimalService.getAllAnimals();

		allAnimals.removeIf(animal -> animal.getId().equals(updatedAnimal.getId()));

		allAnimals.add(updatedAnimal);

		JsonConverter.serialization(allAnimals, JsonPaths.ANIMAL);

		ShelterService.updateShelter(updatedAnimal.getShelter());

		return updatedAnimal;
	}

	/**
	 * Decrements the number of animals in the specified shelter if there are animals present.
	 *
	 * @param shelter The shelter from which to decrement the number of animals.
	 */
	public static void decrementNumbersOfAnimals(Shelter shelter) {
		if (shelter != null) {
			int currentNumbersOfAnimals = shelter.getNumbersOfAnimals();
			if (currentNumbersOfAnimals > 0) {
				shelter.setNumbersOfAnimals(currentNumbersOfAnimals - 1);
				ShelterService.updateShelter(shelter);
			}
		}
	}

	/**
	 * Decrements the number of animals in the specified shelter by iterating through all animals in the shelter.
	 *
	 * @param shelter The shelter from which to decrement the number of animals.
	 */
	public static void decrementNumbersOfAnimalsInShelter(Shelter shelter) {
		List<Animal> animalsInShelter = searchByShelter(shelter);

		for (Animal animal : animalsInShelter) {
			decrementNumbersOfAnimals(animal.getShelter());
		}
	}

	/**
	 * Searches for animals based on the specified species.
	 *
	 * @param species The species to search for.
	 * @return A list of animals matching the specified species.
	 */
	public static List<Animal> searchBySpecies(String species) {
		return animals().stream()
		    .filter(animal -> animal.getSpecies().equalsIgnoreCase(species))
		    .collect(Collectors.toList());
	}

	/**
	 * Finds an animal by its name.
	 *
	 * @param name The name of the animal to find.
	 * @return The animal with the specified name or null if not found.
	 */
	public static Animal findAnimalByName(String name) {
		return animals().stream()
		    .filter(animal -> animal.getName().equalsIgnoreCase(name))
		    .findFirst()
		    .orElse(null);
	}

	/**
	 * Deletes an animal with the specified ID from the system.
	 *
	 * @param animalId The ID of the animal to delete.
	 * @return True if the animal is deleted successfully, false otherwise.
	 */
	public static boolean deleteAnimalById(UUID animalId) {
		List<Animal> animalList = animals();

		animalList.removeIf(animal -> animal.getId().equals(animalId));

		JsonConverter.serialization(animalList, JsonPaths.ANIMAL);
		return false;
	}

	/**
	 * Finds animals born in a specific year.
	 *
	 * @param birthYear The birth year to search for.
	 * @return A list of animals born in the specified year.
	 */
	public static List<Animal> findAnimalByYear(int birthYear) {
		return animals().stream()
		    .filter(animal -> animal.getDateOfDelivery().getYear() == birthYear)
		    .collect(Collectors.toList());
	}

	/**
	 * Retrieves a list of all animals in the system.
	 *
	 * @return A list of all animals in the system.
	 */
	public static List<Animal> getAllAnimals() {
		List<Animal> animalsList = animals();
		return animalsList != null ? animalsList : new ArrayList<>();
	}

	/**
	 * Searches for animals based on the specified breed.
	 *
	 * @param breed The breed to search for.
	 * @return A list of animals matching the specified breed.
	 */
	public static List<Animal> searchByBreed(String breed) {
		return animals().stream()
		    .filter(animal -> animal.getBreed().equalsIgnoreCase(breed))
		    .collect(Collectors.toList());
	}

	/**
	 * Searches for animals in a specific shelter.
	 *
	 * @param shelter The shelter to search for.
	 * @return A list of animals located in the specified shelter.
	 */
	public static List<Animal> searchByShelter(Shelter shelter) {
		return animals().stream()
		    .filter(animal -> animal.getShelter().equals(shelter))
		    .collect(Collectors.toList());
	}

	/**
	 * Deserializes and retrieves a list of animals from the JSON file.
	 *
	 * @return A list of animals.
	 */
	private static List<Animal> animals() {
		return JsonConverter.deserialization(JsonPaths.ANIMAL, Animal.class);
	}

	/**
	 * Validates the name of the animal.
	 *
	 * @param name The name to validate.
	 */
	private static void isValidName(String name) {
		final String FIELD_NAME = "animal name";
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

	/**
	 * Validates the age of the animal.
	 *
	 * @param age The age to validate.
	 */
	private static void isValidAge(int age) {
		final String FIELD_AGE = "animal age";

		if (age <= 0) {
			errors.add("Invalid field: " + FIELD_AGE);
		}
	}

	/**
	 * Validates the species of the animal.
	 *
	 * @param species The species to validate.
	 */
	private static void isValidSpecies(String species) {
		final String FIELD_SPECIES = "animal species";
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

	/**
	 * Validates the breed of the animal.
	 *
	 * @param breed The breed to validate.
	 */
	private static void isValidBreed(String breed) {
		final String FIELD_BREED = "animal breed";
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

	/**
	 * Validates the date of delivery for the animal.
	 *
	 * @param dateOfDelivery The date of delivery to validate.
	 */
	private static void isValidDateOfDelivery(String dateOfDelivery) {
		final String FIELD_DATE_OF_DELIVERY = "animal delivery date";

		if (ValidatorServiceUtil.isFieldBlankValidate(dateOfDelivery)) {
			errors.add(ExceptionTemplate.EMPTY_FIELD_EXCEPTION.getTemplate().formatted(FIELD_DATE_OF_DELIVERY));
		}
	}

	/**
	 * Validates the shelter for the animal.
	 *
	 * @param shelter The shelter to validate.
	 */
	private static void isValidShelter(Shelter shelter) {
		final String FIELD_SHELTER = "animal shelter";

		if (shelter == null) {
			errors.add("Invalid field: " + FIELD_SHELTER);
		}
	}
}