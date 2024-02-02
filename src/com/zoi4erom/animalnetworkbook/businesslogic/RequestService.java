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

public class RequestService {
	private static final List<String> errors = new ArrayList<>();
	private RequestService() {
	}
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
	private static void createRequest(String name, Animal animal, User user) {
		List<Request> requests = requests();

		Request request = new Request(UUID.randomUUID(), name, animal, user, LocalDate.now());

		requests.add(request);

		JsonConverter.serialization(requests, JsonPaths.REQUEST);
	}
	public static Request updateRequest(Request updatedRequest) {
		List<Request> allRequests = requests();

		allRequests.removeIf(request -> request.getId().equals(updatedRequest.getId()));

		allRequests.add(updatedRequest);

		JsonConverter.serialization(allRequests, JsonPaths.REQUEST);

		return updatedRequest;
	}
	public static Request findRequestByName(String name) {
		List<Request> requests = requests();

		return requests.stream()
		    .filter(request -> request.getName().equals(name))
		    .findFirst()
		    .orElse(null);
	}
	public static List<Request> findRequestsByStatus(RequestStatus requestStatus) {
		List<Request> requests = requests();

		return requests.stream()
		    .filter(request -> request.getStatus() == requestStatus)
		    .collect(Collectors.toList());
	}
	public static List<Request> findRequestsByAnimal(Animal animal) {
		List<Request> requests = getAllRequests();

		return requests.stream()
		    .filter(request -> request.getAnimal().equals(animal))
		    .collect(Collectors.toList());
	}
	public static List<Request> findRequestsByUser(User user) {
		List<Request> requests = getAllRequests();

		return requests.stream()
		    .filter(request -> request.getUser().equals(user))
		    .collect(Collectors.toList());
	}


	public static List<Request> getAllRequests() {
		List<Request> allRequests = requests();

		if (allRequests == null) {
			allRequests = new ArrayList<>();
		}

		return allRequests;
	}
	public static List<Request> getRequestsByAnimal(Animal animal) {
		List<Request> allRequests = getAllRequests();

		return allRequests.stream()
		    .filter(request -> request.getAnimal().equals(animal))
		    .collect(Collectors.toList());
	}
	public static List<Request> getRequestsByUser(User user) {
		List<Request> allRequests = getAllRequests();

		return allRequests.stream()
		    .filter(request -> request.getUser().equals(user))
		    .collect(Collectors.toList());
	}
	public static List<Request> getRequestsByStatus(RequestStatus status) {
		List<Request> allRequests = getAllRequests();

		return allRequests.stream()
		    .filter(request -> request.getStatus().equals(status))
		    .collect(Collectors.toList());
	}
	public static List<Request> requests() {
		return JsonConverter.deserialization(JsonPaths.REQUEST, Request.class);
	}

	private static void isValidName(String name) {
		final String FIELD_NAME = "назви запиту";

		if (Boolean.TRUE.equals(ValidatorServiceUtil.isFieldBlankValidate(name))) {
			errors.add(ExceptionTemplate.EMPTY_FIELD_EXCEPTION.getTemplate().formatted(FIELD_NAME));
		}
	}
	private static void isValidAnimal(Animal animal) {
		final String FIELD_NAME = "тварини в запиті";

		if (animal == null) {
			errors.add(ExceptionTemplate.EMPTY_FIELD_EXCEPTION.getTemplate().formatted(FIELD_NAME));
		}
	}
	private static void isValidUser(User user) {
		final String FIELD_NAME = "користувача в запиті";

		if (user == null) {
			errors.add(ExceptionTemplate.EMPTY_FIELD_EXCEPTION.getTemplate().formatted(FIELD_NAME));
		}
	}
}
