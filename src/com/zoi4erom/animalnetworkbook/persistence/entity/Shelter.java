package com.zoi4erom.animalnetworkbook.persistence.entity;

import java.util.List;
import java.util.UUID;

public class Shelter extends Entity{
	private String name;
	private String address;
	private String phone;
	private int numbersOfAnimals;
	private int capacityOfAnimals;
	public Shelter(UUID id, String name, String address, String phone, int numbersOfAnimals,
	    int capacityOfAnimals) {
		super(id);
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.numbersOfAnimals = numbersOfAnimals;
		this.capacityOfAnimals = capacityOfAnimals;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setCapacityOfAnimals(int capacityOfAnimals) {
		this.capacityOfAnimals = capacityOfAnimals;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setNumbersOfAnimals(int numbersOfAnimals) {
		this.numbersOfAnimals = numbersOfAnimals;
	}
	public String getName() {
		return name;
	}
	public String getAddress() {
		return address;
	}
	public String getPhone() {
		return phone;
	}
	public int getNumbersOfAnimals() {
		return numbersOfAnimals;
	}
	public int getCapacityOfAnimals() {
		return capacityOfAnimals;
	}
	@Override
	public String toString() {
		return '\n' +"-------------------------------------------\n" +
		    "Ім'я: " + name + '\n' +
		    "Адреса: " + address + '\n' +
		    "Телефон: " + phone + '\n' +
		    "Кількість тварин: " + numbersOfAnimals + '\n' +
		    "Вмістимість тварин: " + capacityOfAnimals + '\n' +
		    "Ідентифікатор: " + id + '\n' +
		    "-------------------------------------------" + '\n';
	}

}
