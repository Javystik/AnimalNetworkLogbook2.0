package com.zoi4erom.animalnetworkbook.persistence.entity;

import java.time.LocalDate;
import java.util.UUID;

public class Animal extends Entity {
	private String name;
	private int age;
	private String species;
	private String breed;
	private final LocalDate dateOfDelivery;
	private Shelter shelter;
	public Animal(UUID id, String name, int age, String species, String breed,
	    LocalDate dateOfDelivery, Shelter shelter) {
		super(id);
		this.name = name;
		this.age = age;
		this.species = species;
		this.breed = breed;
		this.dateOfDelivery = dateOfDelivery;
		this.shelter = shelter;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public int getAge() {
		return age;
	}
	public String getSpecies() {
		return species;
	}
	public String getBreed() {
		return breed;
	}
	public LocalDate getDateOfDelivery() {
		return dateOfDelivery;
	}

	public Shelter getShelter() {
		return shelter;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public void setSpecies(String species) {
		this.species = species;
	}
	public void setBreed(String breed) {
		this.breed = breed;
	}
	public void setShelter(Shelter shelter) {
		this.shelter = shelter;
	}
	@Override
	public String toString() {
		return '\n' + "-------------------------------------------\n" +
		    "Ім'я: " + name + '\n' +
		    "Вік: " + age + '\n' +
		    "Вид: " + species + '\n' +
		    "Порода: " + breed + '\n' +
		    "Приют: \n" +
		    "  - Назва приюту: " + shelter.getName() + '\n' +
		    "  - Адреса приюту: " + shelter.getAddress() + '\n' +
		    "  - Номер телефону приюту: " + shelter.getPhone() + '\n' +
		    "Дата доставки: " + dateOfDelivery + '\n' +
		    "Ідентифікатор: " + id + '\n' +
		    "-------------------------------------------\n";
	}
}