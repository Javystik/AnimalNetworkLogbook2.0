package com.zoi4erom.animalnetworkbook.businesslogic;

import com.zoi4erom.animalnetworkbook.businesslogic.exception.ExceptionTemplate;
import com.zoi4erom.animalnetworkbook.persistence.entity.Animal;
import com.zoi4erom.animalnetworkbook.persistence.entity.Request;
import com.zoi4erom.animalnetworkbook.persistence.entity.Request.RequestStatus;
import com.zoi4erom.animalnetworkbook.persistence.entity.User;
import com.zoi4erom.animalnetworkbook.persistence.jsonhandler.JsonConverter;
import com.zoi4erom.animalnetworkbook.persistence.jsonhandler.JsonPaths;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Provides functionality related to animal adoption requests.
 */
public class RequestService {

	private static final List<String> errors = new ArrayList<>();

	private RequestService() {
	}

	/**
	 * Validates and creates a new adoption request.
	 *
	 * @param name   the name of the adoption request
	 * @param animal the animal in the request
	 * @param user   the user making the request
	 * @return a list of validation errors or an empty list if validation is successful
	 */
	public static List<String> createRequestValidation(String name, Animal animal, User user) {
		errors.clear();

		isValidName(name);
		isValidAnimal(animal);
		isValidUser(user);

		if (!errors.isEmpty()) {
			return errors;
		}

		createRequest(name, animal, user);

		return errors;
	}

	/**
	 * Creates a new adoption request and stores it in the request list.
	 *
	 * @param name   the name of the adoption request
	 * @param animal the animal in the request
	 * @param user   the user making the request
	 */
	private static void createRequest(String name, Animal animal, User user) {
		List<Request> requests = requests();

		Request request = new Request(UUID.randomUUID(), name, animal, user, LocalDate.now());

		requests.add(request);

		JsonConverter.serialization(requests, JsonPaths.REQUEST);
	}

	/**
	 * Updates an existing adoption request.
	 *
	 * @param updatedRequest the updated request
	 * @return the updated request
	 */
	public static Request updateRequest(Request updatedRequest) {
		List<Request> allRequests = requests();

		allRequests.removeIf(request -> request.getId().equals(updatedRequest.getId()));

		allRequests.add(updatedRequest);

		JsonConverter.serialization(allRequests, JsonPaths.REQUEST);

		return updatedRequest;
	}

	/**
	 * Finds an adoption request by its name.
	 *
	 * @param name the name of the adoption request to find
	 * @return the adoption request or null if not found
	 */
	public static Request findRequestByName(String name) {
		List<Request> requests = requests();

		return requests.stream()
		    .filter(request -> request.getName().equals(name))
		    .findFirst()
		    .orElse(null);
	}

	/**
	 * Finds adoption requests by their status.
	 *
	 * @param requestStatus the status of the adoption requests to find
	 * @return a list of adoption requests with the specified status
	 */
	public static List<Request> findRequestsByStatus(RequestStatus requestStatus) {
		List<Request> requests = requests();

		return requests.stream()
		    .filter(request -> request.getStatus() == requestStatus)
		    .collect(Collectors.toList());
	}

	/**
	 * Finds adoption requests by the specified animal.
	 *
	 * @param animal the animal in the adoption requests to find
	 * @return a list of adoption requests with the specified animal
	 */
	public static List<Request> findRequestsByAnimal(Animal animal) {
		List<Request> requests = getAllRequests();

		return requests.stream()
		    .filter(request -> request.getAnimal().equals(animal))
		    .collect(Collectors.toList());
	}

	/**
	 * Finds adoption requests by the specified user.
	 *
	 * @param user the user making the adoption requests to find
	 * @return a list of adoption requests made by the specified user
	 */
	public static List<Request> findRequestsByUser(User user) {
		List<Request> requests = getAllRequests();

		return requests.stream()
		    .filter(request -> request.getUser().equals(user))
		    .collect(Collectors.toList());
	}

	/**
	 * Retrieves all adoption requests.
	 *
	 * @return a list of all adoption requests
	 */
	public static List<Request> getAllRequests() {
		List<Request> allRequests = requests();

		if (allRequests == null) {
			allRequests = new ArrayList<>();
		}

		return allRequests;
	}

	/**
	 * Retrieves adoption requests for a specific animal.
	 *
	 * @param animal the animal in the adoption requests to retrieve
	 * @return a list of adoption requests with the specified animal
	 */
	public static List<Request> getRequestsByAnimal(Animal animal) {
		List<Request> allRequests = getAllRequests();

		return allRequests.stream()
		    .filter(request -> request.getAnimal().equals(animal))
		    .collect(Collectors.toList());
	}

	/**
	 * Retrieves adoption requests made by a specific user.
	 *
	 * @param user the user making the adoption requests to retrieve
	 * @return a list of adoption requests made by the specified user
	 */
	public static List<Request> getRequestsByUser(User user) {
		List<Request> allRequests = getAllRequests();

		return allRequests.stream()
		    .filter(request -> request.getUser().equals(user))
		    .collect(Collectors.toList());
	}

	/**
	 * Retrieves adoption requests with a specific status.
	 *
	 * @param status the status of the adoption requests to retrieve
	 * @return a list of adoption requests with the specified status
	 */
	public static List<Request> getRequestsByStatus(RequestStatus status) {
		List<Request> allRequests = getAllRequests();

		return allRequests.stream()
		    .filter(request -> request.getStatus().equals(status))
		    .collect(Collectors.toList());
	}

	/**
	 * Retrieves the list of adoption requests from the JSON file.
	 *
	 * @return a list of adoption requests
	 */
	public static List<Request> requests() {
		return JsonConverter.deserialization(JsonPaths.REQUEST, Request.class);
	}

	/**
	 * Validates the name of the adoption request.
	 *
	 * @param name the name to validate
	 */
	private static void isValidName(String name) {
		final String FIELD_NAME = "назви запиту";

		if (Boolean.TRUE.equals(ValidatorServiceUtil.isFieldBlankValidate(name))) {
			errors.add(ExceptionTemplate.EMPTY_FIELD_EXCEPTION.getTemplate().formatted(FIELD_NAME));
		}
	}

	/**
	 * Validates the animal in the adoption request.
	 *
	 * @param animal the animal to validate
	 */
	private static void isValidAnimal(Animal animal) {
		final String FIELD_NAME = "тварини в запиті";

		if (animal == null) {
			errors.add(ExceptionTemplate.EMPTY_FIELD_EXCEPTION.getTemplate().formatted(FIELD_NAME));
		}
	}

	/**
	 * Validates the user in the adoption request.
	 *
	 * @param user the user to validate
	 */
	private static void isValidUser(User user) {
		final String FIELD_NAME = "користувача в запиті";

		if (user == null) {
			errors.add(ExceptionTemplate.EMPTY_FIELD_EXCEPTION.getTemplate().formatted(FIELD_NAME));
		}
	}
}
