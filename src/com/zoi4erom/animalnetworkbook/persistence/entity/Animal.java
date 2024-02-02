package com.zoi4erom.animalnetworkbook.persistence.entity;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Represents an animal in the Animal Network Book system.
 */
public class Animal extends Entity {
	private String name;
	private int age;
	private String species;
	private String breed;
	private final LocalDate dateOfDelivery;
	private Shelter shelter;

	/**
	 * Constructs an instance of the Animal class with the specified parameters.
	 *
	 * @param id              The unique identifier of the animal.
	 * @param name            The name of the animal.
	 * @param age             The age of the animal.
	 * @param species         The species of the animal.
	 * @param breed           The breed of the animal.
	 * @param dateOfDelivery  The date of delivery for the animal.
	 * @param shelter         The shelter where the animal is housed.
	 */
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

	/**
	 * Gets the name of the animal.
	 *
	 * @return The name of the animal.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the animal.
	 *
	 * @param name The new name of the animal.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the age of the animal.
	 *
	 * @return The age of the animal.
	 */
	public int getAge() {
		return age;
	}

	/**
	 * Gets the species of the animal.
	 *
	 * @return The species of the animal.
	 */
	public String getSpecies() {
		return species;
	}

	/**
	 * Gets the breed of the animal.
	 *
	 * @return The breed of the animal.
	 */
	public String getBreed() {
		return breed;
	}

	/**
	 * Gets the date of delivery for the animal.
	 *
	 * @return The date of delivery for the animal.
	 */
	public LocalDate getDateOfDelivery() {
		return dateOfDelivery;
	}

	/**
	 * Gets the shelter where the animal is housed.
	 *
	 * @return The shelter where the animal is housed.
	 */
	public Shelter getShelter() {
		return shelter;
	}

	/**
	 * Sets the age of the animal.
	 *
	 * @param age The new age of the animal.
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * Sets the species of the animal.
	 *
	 * @param species The new species of the animal.
	 */
	public void setSpecies(String species) {
		this.species = species;
	}

	/**
	 * Sets the breed of the animal.
	 *
	 * @param breed The new breed of the animal.
	 */
	public void setBreed(String breed) {
		this.breed = breed;
	}

	/**
	 * Sets the shelter where the animal is housed.
	 *
	 * @param shelter The new shelter for the animal.
	 */
	public void setShelter(Shelter shelter) {
		this.shelter = shelter;
	}

	/**
	 * Returns a string representation of the Animal object.
	 *
	 * @return A string containing detailed information about the animal.
	 */
	@Override
	public String toString() {
		return '\n' + "-------------------------------------------\n" +
		    "Name: " + name + '\n' +
		    "Age: " + age + '\n' +
		    "Species: " + species + '\n' +
		    "Breed: " + breed + '\n' +
		    "Shelter: \n" +
		    "  - Shelter Name: " + shelter.getName() + '\n' +
		    "  - Shelter Address: " + shelter.getAddress() + '\n' +
		    "  - Shelter Phone Number: " + shelter.getPhone() + '\n' +
		    "Date of Delivery: " + dateOfDelivery + '\n' +
		    "Identifier: " + id + '\n' +
		    "-------------------------------------------\n";
	}
}
