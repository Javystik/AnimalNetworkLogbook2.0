package com.zoi4erom.animalnetworkbook.persistence.entity;

import java.util.UUID;

/**
 * Represents a shelter in the Animal Network Book system.
 */
public class Shelter extends Entity {
	private String name;
	private String address;
	private String phone;
	private int numbersOfAnimals;
	private int capacityOfAnimals;

	/**
	 * Constructs an instance of the Shelter class with the specified parameters.
	 *
	 * @param id                The unique identifier of the shelter.
	 * @param name              The name of the shelter.
	 * @param address           The address of the shelter.
	 * @param phone             The phone number of the shelter.
	 * @param numbersOfAnimals  The current number of animals in the shelter.
	 * @param capacityOfAnimals The maximum capacity of animals the shelter can accommodate.
	 */
	public Shelter(UUID id, String name, String address, String phone, int numbersOfAnimals, int capacityOfAnimals) {
		super(id);
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.numbersOfAnimals = numbersOfAnimals;
		this.capacityOfAnimals = capacityOfAnimals;
	}

	/**
	 * Gets the name of the shelter.
	 *
	 * @return The name of the shelter.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the shelter.
	 *
	 * @param name The new name of the shelter.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the address of the shelter.
	 *
	 * @return The address of the shelter.
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets the address of the shelter.
	 *
	 * @param address The new address of the shelter.
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Gets the phone number of the shelter.
	 *
	 * @return The phone number of the shelter.
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Sets the phone number of the shelter.
	 *
	 * @param phone The new phone number of the shelter.
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * Gets the current number of animals in the shelter.
	 *
	 * @return The current number of animals in the shelter.
	 */
	public int getNumbersOfAnimals() {
		return numbersOfAnimals;
	}

	/**
	 * Sets the current number of animals in the shelter.
	 *
	 * @param numbersOfAnimals The new current number of animals in the shelter.
	 */
	public void setNumbersOfAnimals(int numbersOfAnimals) {
		this.numbersOfAnimals = numbersOfAnimals;
	}

	/**
	 * Gets the maximum capacity of animals the shelter can accommodate.
	 *
	 * @return The maximum capacity of animals the shelter can accommodate.
	 */
	public int getCapacityOfAnimals() {
		return capacityOfAnimals;
	}

	/**
	 * Sets the maximum capacity of animals the shelter can accommodate.
	 *
	 * @param capacityOfAnimals The new maximum capacity of animals the shelter can accommodate.
	 */
	public void setCapacityOfAnimals(int capacityOfAnimals) {
		this.capacityOfAnimals = capacityOfAnimals;
	}

	/**
	 * Returns a string representation of the Shelter object.
	 *
	 * @return A string containing detailed information about the shelter.
	 */
	@Override
	public String toString() {
		return '\n' + "-------------------------------------------\n" +
		    "Shelter Name: " + name + '\n' +
		    "Address: " + address + '\n' +
		    "Phone: " + phone + '\n' +
		    "Number of Animals: " + numbersOfAnimals + '\n' +
		    "Capacity of Animals: " + capacityOfAnimals + '\n' +
		    "Identifier: " + id + '\n' +
		    "-------------------------------------------" + '\n';
	}
}
