package com.zoi4erom.animalnetworkbook.businesslogic;

import com.zoi4erom.animalnetworkbook.businesslogic.exception.ExceptionTemplate;
import com.zoi4erom.animalnetworkbook.persistence.entity.Animal;
import com.zoi4erom.animalnetworkbook.persistence.entity.Shelter;
import com.zoi4erom.animalnetworkbook.persistence.jsonhandler.JsonConverter;
import com.zoi4erom.animalnetworkbook.persistence.jsonhandler.JsonPaths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Provides functionality related to animal shelters.
 */
public class ShelterService {

	private static final List<String> errors = new ArrayList<>();

	private ShelterService() {
	}

	/**
	 * Validates and creates a new animal shelter.
	 *
	 * @param name              the name of the shelter
	 * @param address           the address of the shelter
	 * @param phone             the phone number of the shelter
	 * @param capacityOfAnimals the capacity of animals that the shelter can accommodate
	 * @return a list of validation errors or an empty list if validation is successful
	 */
	public static List<String> createShelterValidation(String name, String address, String phone, int capacityOfAnimals) {
		errors.clear();

		isValidName(name);
		isValidAddress(address);
		isValidPhone(phone);
		isValidCapacityOfAnimals(capacityOfAnimals);

		if (!errors.isEmpty()) {
			return errors;
		}

		createShelter(name, address, phone, capacityOfAnimals);

		return errors;
	}

	/**
	 * Creates a new animal shelter and stores it in the shelter list.
	 *
	 * @param name              the name of the shelter
	 * @param address           the address of the shelter
	 * @param phone             the phone number of the shelter
	 * @param capacityOfAnimals the capacity of animals that the shelter can accommodate
	 */
	private static void createShelter(String name, String address, String phone, int capacityOfAnimals) {
		List<Shelter> shelters = shelters();

		Shelter shelter = new Shelter(UUID.randomUUID(), name, address, phone, 0, capacityOfAnimals);

		shelters.add(shelter);
		JsonConverter.serialization(shelters, JsonPaths.SHELTERS);
	}

	/**
	 * Deletes an animal shelter by its name.
	 *
	 * @param name the name of the shelter to delete
	 * @return true if the shelter is successfully deleted, false otherwise
	 */
	public static Boolean deleteShelterByName(String name) {
		List<Shelter> shelters = shelters();
		List<Animal> animals = AnimalService.getAllAnimals();

		for (Shelter shelter : shelters) {
			if (Objects.equals(shelter.getName(), name)) {
				for (Animal animal : animals) {
					if (Objects.equals(animal.getShelter().getName(), name)) {
						return false;
					}
				}
			}
		}

		shelters.removeIf(shelter -> shelter.getName().equals(name));

		JsonConverter.serialization(shelters, JsonPaths.SHELTERS);

		return true;
	}

	/**
	 * Updates an existing animal shelter.
	 *
	 * @param updatedShelter the updated shelter
	 */
	public static void updateShelter(Shelter updatedShelter) {
		List<Shelter> allShelters = shelters();

		allShelters.removeIf(shelter -> shelter.getId().equals(updatedShelter.getId()));

		allShelters.add(updatedShelter);

		JsonConverter.serialization(allShelters, JsonPaths.SHELTERS);
	}

	/**
	 * Retrieves an animal shelter by its ID.
	 *
	 * @param shelterId the ID of the shelter to retrieve
	 * @return the shelter with the specified ID, or null if not found
	 */
	public static Shelter getShelterById(UUID shelterId) {
		return shelters().stream()
		    .filter(shelter -> shelter.getId().equals(shelterId))
		    .findFirst()
		    .orElse(null);
	}

	/**
	 * Finds an animal shelter by its name.
	 *
	 * @param name the name of the shelter to find
	 * @return the shelter with the specified name, or null if not found
	 */
	public static Shelter findShelterByName(String name) {
		List<Shelter> shelters = shelters();

		return shelters.stream()
		    .filter(shelter -> shelter.getName().equals(name))
		    .findFirst()
		    .orElse(null);
	}

	/**
	 * Finds an animal shelter by its maximum capacity of animals.
	 *
	 * @param maxAnimals the maximum capacity of animals of the shelter to find
	 * @return the shelter with the specified maximum capacity of animals, or null if not found
	 */
	public static Shelter findShelterByMaxAnimals(int maxAnimals) {
		List<Shelter> shelters = shelters();

		return shelters.stream()
		    .filter(shelter -> shelter.getCapacityOfAnimals() == maxAnimals)
		    .findFirst()
		    .orElse(null);
	}

	/**
	 * Finds an animal shelter by its phone number.
	 *
	 * @param phoneNumber the phone number of the shelter to find
	 * @return the shelter with the specified phone number, or null if not found
	 */
	public static Shelter findShelterByPhoneNumber(String phoneNumber) {
		List<Shelter> shelters = shelters();

		return shelters.stream()
		    .filter(shelter -> shelter.getPhone().equals(phoneNumber))
		    .findFirst()
		    .orElse(null);
	}

	/**
	 * Finds an animal shelter by its address.
	 *
	 * @param address the address of the shelter to find
	 * @return the shelter with the specified address, or null if not found
	 */
	public static Shelter findShelterByAddress(String address) {
		List<Shelter> shelters = shelters();

		return shelters.stream()
		    .filter(shelter -> shelter.getAddress().equals(address))
		    .findFirst()
		    .orElse(null);
	}

	/**
	 * Retrieves a list of all animal shelters.
	 *
	 * @return a list of all animal shelters
	 */
	public static List<Shelter> getAllShelters() {
		List<Shelter> allShelters = shelters();

		if (allShelters == null) {
			allShelters = new ArrayList<>();
		}

		return allShelters;
	}

	/**
	 * Retrieves a list of all animal shelters.
	 *
	 * @return a list of all animal shelters
	 */
	static List<Shelter> shelters() {
		return JsonConverter.deserialization(JsonPaths.SHELTERS, Shelter.class);
	}

	/**
	 * Validates the name of the animal shelter.
	 *
	 * @param name the name to validate
	 */
	private static void isValidName(String name) {
		final String FIELD_NAME = "назви притулку";
		final int MIN_SIZE = 3;
		final int MAX_SIZE = 50;

		List<Shelter> allShelters = getAllShelters();

		if (Boolean.TRUE.equals(ValidatorServiceUtil.isFieldBlankValidate(name))) {
			errors.add(ExceptionTemplate.EMPTY_FIELD_EXCEPTION.getTemplate().formatted(FIELD_NAME));
		}
		if (Boolean.TRUE.equals(ValidatorServiceUtil.isFieldSizeValidate(name, MIN_SIZE, MAX_SIZE))) {
			errors.add(ExceptionTemplate.TOO_SHORT_LONG_EXCEPTION.getTemplate()
			    .formatted(FIELD_NAME, MIN_SIZE, MAX_SIZE));
		}
		if (allShelters.stream().anyMatch(shelter -> shelter.getName().equalsIgnoreCase(name))) {
			errors.add("Притулок з таким іменем вже існує.");
		}
	}

	/**
	 * Validates the address of the animal shelter.
	 *
	 * @param address the address to validate
	 */
	private static void isValidAddress(String address) {
		final String FIELD_ADDRESS = "адреси притулку";
		final int MIN_SIZE = 5;
		final int MAX_SIZE = 100;

		if (Boolean.TRUE.equals(ValidatorServiceUtil.isFieldBlankValidate(address))) {
			errors.add(ExceptionTemplate.EMPTY_FIELD_EXCEPTION.getTemplate().formatted(FIELD_ADDRESS));
		}
		if (Boolean.TRUE.equals(ValidatorServiceUtil.isFieldSizeValidate(address, MIN_SIZE, MAX_SIZE))) {
			errors.add(ExceptionTemplate.TOO_SHORT_LONG_EXCEPTION.getTemplate()
			    .formatted(FIELD_ADDRESS, MIN_SIZE, MAX_SIZE));
		}
	}

	/**
	 * Validates the phone number of the animal shelter.
	 *
	 * @param phone the phone number to validate
	 */
	private static void isValidPhone(String phone) {
		final String FIELD_PHONE = "телефону притулку";
		final int MIN_SIZE = 7;
		final int MAX_SIZE = 15;

		if (Boolean.TRUE.equals(ValidatorServiceUtil.isFieldBlankValidate(phone))) {
			errors.add(ExceptionTemplate.EMPTY_FIELD_EXCEPTION.getTemplate().formatted(FIELD_PHONE));
		}
		if (Boolean.TRUE.equals(ValidatorServiceUtil.isFieldSizeValidate(phone, MIN_SIZE, MAX_SIZE))) {
			errors.add(ExceptionTemplate.TOO_SHORT_LONG_EXCEPTION.getTemplate()
			    .formatted(FIELD_PHONE, MIN_SIZE, MAX_SIZE));
		}
	}

	/**
	 * Validates the capacity of animals that the animal shelter can accommodate.
	 *
	 * @param capacityOfAnimals the capacity of animals to validate
	 */
	private static void isValidCapacityOfAnimals(int capacityOfAnimals) {
		final String FIELD_CAPACITY = "кількості тварин, яку може вмістити притулок";
		final int MIN_CAPACITY = 1;

		if (capacityOfAnimals < MIN_CAPACITY) {
			errors.add("Invalid field: " + FIELD_CAPACITY);
		}
	}
}
