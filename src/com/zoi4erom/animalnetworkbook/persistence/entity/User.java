package com.zoi4erom.animalnetworkbook.persistence.entity;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class User extends Entity {
	private String fullName;
	private String password;
	private final String phoneNumber;
	private String homeAddress;
	private String email;
	private final LocalDate birthdate;
	private Role role;
	private static final Role DEFAULT_ROLE = Role.USER;
	public User(UUID id, String fullName, String password, String phoneNumber,
	    String homeAddress, String email, LocalDate dateOfBorn, Role role) {
		super(id);
		this.fullName = fullName;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.homeAddress = homeAddress;
		this.email = email;
		this.birthdate = dateOfBorn;
		this.role = (role != null) ? role : DEFAULT_ROLE;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Role getRole() {
		return role;
	}
	public String getFullName() {
		return fullName;
	}

	public String getPassword() {
		return password;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public String getHomeAddress() {
		return homeAddress;
	}
	public String getEmail() {
		return email;
	}
	public LocalDate getBirthdate() {
		return birthdate;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		User client = (User) o;
		return Objects.equals(phoneNumber, client.phoneNumber);
	}
	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), phoneNumber);
	}
	@Override
	public String toString() {
		return '\n' +"-------------------------------------------\n" +
		    "Повне ім'я: " + fullName + '\n' +
		    "Пароль: " + password + '\n' +
		    "Номер телефону: " + phoneNumber + '\n' +
		    "Домашня адреса: " + homeAddress + '\n' +
		    "Електронна пошта: " + email + '\n' +
		    "Дата народження: " + birthdate + '\n' +
		    "Ідентифікатор: " + id + '\n' +
		    "Роль: " + role + '\n' +
		    "-------------------------------------------"+ '\n';
	}
	public enum Role {
		ADMIN("admin", Map.of(
		    EntityName.ANIMAL, new Permission(true, true, true, true),
		    EntityName.REQUEST, new Permission(true, true, true, true),
		    EntityName.SHELTER, new Permission(true, true, true, true),
		    EntityName.USER, new Permission(true, true, true, true))),
		PERSONAL("personal", Map.of(
		    EntityName.ANIMAL, new Permission(true, true, true, true),
		    EntityName.REQUEST, new Permission(true, true, true, true),
		    EntityName.SHELTER, new Permission(true, true, true, true),
		    EntityName.USER, new Permission(false, false, false, false))),
		USER("user", Map.of(
		    EntityName.ANIMAL, new Permission(true, false, false, true),
		    EntityName.REQUEST, new Permission(true, false, false, false),
		    EntityName.SHELTER, new Permission(false, false, false, true),
		    EntityName.USER, new Permission(false, false, false, false)));
		private final String name;
		private final Map<EntityName, Permission> permissions;
		Role(String name, Map<EntityName, Permission> permissions) {
			this.name = name;
			this.permissions = permissions;
		}
		public String getName() {
			return name;
		}
		public Map<EntityName, Permission> getPermissions() {
			return permissions;
		}
		public enum EntityName {ANIMAL, REQUEST, SHELTER, USER}
		public record Permission(boolean canAdd, boolean canEdit, boolean canDelete, boolean canRead) {
		}
	}
}