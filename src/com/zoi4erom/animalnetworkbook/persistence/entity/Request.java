package com.zoi4erom.animalnetworkbook.persistence.entity;

import java.time.LocalDate;
import java.util.UUID;

public class Request extends Entity{
	private String name;
	private Animal animal;
	private User user;
	private RequestStatus status;
	private LocalDate requestCreateDate;

	public Request(UUID id, String name, Animal animal, User user, LocalDate requestCreateDate) {
		super(id);
		this.name = name;
		this.animal = animal;
		this.user = user;
		this.status = RequestStatus.PENDING;
		this.requestCreateDate = requestCreateDate;
	}
	public void setAnimal(Animal animal) {
		this.animal = animal;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public void setStatus(RequestStatus status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public Animal getAnimal() {
		return animal;
	}
	public User getUser() {
		return user;
	}
	public RequestStatus getStatus() {
		return status;
	}
	@Override
	public String toString() {
		return '\n' + "-------------------------------------------\n" +
		    "Ім'я: " + name + '\n' +
		    "Тварина: \n" +
		    "  - Ім'я: " + animal.getName() + '\n' +
		    "  - Вид: " + animal.getSpecies() + '\n' +
		    "  - Порода: " + animal.getBreed() + '\n' +
		    "  - Приют: " + animal.getShelter().getName() + '\n' +
		    "Користувач: \n" +
		    "  - Повне ім'я: " + user.getFullName() + '\n' +
		    "  - Домашня адреса: " + user.getHomeAddress() + '\n' +
		    "  - Номер телефону: " + user.getPhoneNumber() + '\n' +
		    "  - Дата народження: " + user.getBirthdate() + '\n' +
		    "Статус: " + status + '\n' +
		    "Дата створення запиту: " + requestCreateDate + '\n' +
		    "Ідентифікатор: " + id + '\n' +
		    "-------------------------------------------" + '\n';
	}

	public enum RequestStatus {
		APPROVED("Погоджений"),
		REJECTED("Не погоджений"),
		PENDING("Очікує погодження");
		private final String description;
		RequestStatus(String description) {
			this.description = description;
		}
		public String getDescription() {
			return description;
		}
	}
}
