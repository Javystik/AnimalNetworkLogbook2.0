package com.zoi4erom.animalnetworkbook.persistence.entity;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Represents a request in the Animal Network Book system.
 */
public class Request extends Entity {
	private String name;
	private Animal animal;
	private User user;
	private RequestStatus status;
	private LocalDate requestCreateDate;

	/**
	 * Constructs an instance of the Request class with the specified parameters.
	 *
	 * @param id                The unique identifier of the request.
	 * @param name              The name of the request.
	 * @param animal            The animal associated with the request.
	 * @param user              The user making the request.
	 * @param requestCreateDate The date when the request was created.
	 */
	public Request(UUID id, String name, Animal animal, User user, LocalDate requestCreateDate) {
		super(id);
		this.name = name;
		this.animal = animal;
		this.user = user;
		this.status = RequestStatus.PENDING;
		this.requestCreateDate = requestCreateDate;
	}

	/**
	 * Gets the name of the request.
	 *
	 * @return The name of the request.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the request.
	 *
	 * @param name The new name of the request.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the associated animal with the request.
	 *
	 * @return The associated animal with the request.
	 */
	public Animal getAnimal() {
		return animal;
	}

	/**
	 * Sets the associated animal with the request.
	 *
	 * @param animal The new associated animal with the request.
	 */
	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	/**
	 * Gets the user making the request.
	 *
	 * @return The user making the request.
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets the user making the request.
	 *
	 * @param user The new user making the request.
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Gets the status of the request.
	 *
	 * @return The status of the request.
	 */
	public RequestStatus getStatus() {
		return status;
	}

	/**
	 * Sets the status of the request.
	 *
	 * @param status The new status of the request.
	 */
	public void setStatus(RequestStatus status) {
		this.status = status;
	}

	/**
	 * Gets the date when the request was created.
	 *
	 * @return The date when the request was created.
	 */
	public LocalDate getRequestCreateDate() {
		return requestCreateDate;
	}

	/**
	 * Sets the date when the request was created.
	 *
	 * @param requestCreateDate The new date when the request was created.
	 */
	public void setRequestCreateDate(LocalDate requestCreateDate) {
		this.requestCreateDate = requestCreateDate;
	}

	/**
	 * Returns a string representation of the Request object.
	 *
	 * @return A string containing detailed information about the request.
	 */
	@Override
	public String toString() {
		return '\n' + "-------------------------------------------\n" +
		    "Request Name: " + name + '\n' +
		    "Animal: \n" +
		    "  - Name: " + animal.getName() + '\n' +
		    "  - Species: " + animal.getSpecies() + '\n' +
		    "  - Breed: " + animal.getBreed() + '\n' +
		    "  - Shelter: " + animal.getShelter().getName() + '\n' +
		    "User: \n" +
		    "  - Full Name: " + user.getFullName() + '\n' +
		    "  - Home Address: " + user.getHomeAddress() + '\n' +
		    "  - Phone Number: " + user.getPhoneNumber() + '\n' +
		    "  - Birthdate: " + user.getBirthdate() + '\n' +
		    "Status: " + status + '\n' +
		    "Request Creation Date: " + requestCreateDate + '\n' +
		    "Identifier: " + id + '\n' +
		    "-------------------------------------------" + '\n';
	}

	/**
	 * Represents the status of a request.
	 */
	public enum RequestStatus {
		APPROVED("Approved"),
		REJECTED("Rejected"),
		PENDING("Pending Approval");

		private final String description;

		/**
		 * Constructs a RequestStatus enum with the specified description.
		 *
		 * @param description The description of the request status.
		 */
		RequestStatus(String description) {
			this.description = description;
		}

		/**
		 * Gets the description of the request status.
		 *
		 * @return The description of the request status.
		 */
		public String getDescription() {
			return description;
		}
	}
}
