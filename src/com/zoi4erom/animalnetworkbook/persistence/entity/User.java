package com.zoi4erom.animalnetworkbook.persistence.entity;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a user in the Animal Network Book system.
 */
public class User extends Entity {
	private String fullName;
	private String password;
	private final String phoneNumber;
	private String homeAddress;
	private String email;
	private final LocalDate birthdate;
	private Role role;
	private static final Role DEFAULT_ROLE = Role.USER;

	/**
	 * Constructs an instance of the User class with the specified parameters.
	 *
	 * @param id         The unique identifier of the user.
	 * @param fullName   The full name of the user.
	 * @param password   The password of the user.
	 * @param phoneNumber The phone number of the user.
	 * @param homeAddress The home address of the user.
	 * @param email      The email address of the user.
	 * @param birthdate  The birthdate of the user.
	 * @param role       The role of the user.
	 */
	public User(UUID id, String fullName, String password, String phoneNumber,
	    String homeAddress, String email, LocalDate birthdate, Role role) {
		super(id);
		this.fullName = fullName;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.homeAddress = homeAddress;
		this.email = email;
		this.birthdate = birthdate;
		this.role = (role != null) ? role : DEFAULT_ROLE;
	}

	/**
	 * Gets the full name of the user.
	 *
	 * @return The full name of the user.
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * Sets the full name of the user.
	 *
	 * @param fullName The new full name of the user.
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * Gets the password of the user.
	 *
	 * @return The password of the user.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password of the user.
	 *
	 * @param password The new password of the user.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the phone number of the user.
	 *
	 * @return The phone number of the user.
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * Gets the home address of the user.
	 *
	 * @return The home address of the user.
	 */
	public String getHomeAddress() {
		return homeAddress;
	}

	/**
	 * Sets the home address of the user.
	 *
	 * @param homeAddress The new home address of the user.
	 */
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	/**
	 * Gets the email address of the user.
	 *
	 * @return The email address of the user.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email address of the user.
	 *
	 * @param email The new email address of the user.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the birthdate of the user.
	 *
	 * @return The birthdate of the user.
	 */
	public LocalDate getBirthdate() {
		return birthdate;
	}

	/**
	 * Gets the role of the user.
	 *
	 * @return The role of the user.
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * Sets the role of the user.
	 *
	 * @param role The new role of the user.
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * Checks if this user is equal to another object.
	 *
	 * @param o The object to compare with this user.
	 * @return {@code true} if the objects are equal, {@code false} otherwise.
	 */
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
		User user = (User) o;
		return Objects.equals(phoneNumber, user.phoneNumber);
	}

	/**
	 * Calculates the hash code for this user.
	 *
	 * @return The hash code of the user.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), phoneNumber);
	}

	/**
	 * Returns a string representation of the User object.
	 *
	 * @return A string containing detailed information about the user.
	 */
	@Override
	public String toString() {
		return '\n' +"-------------------------------------------\n" +
		    "Full Name: " + fullName + '\n' +
		    "Password: " + password + '\n' +
		    "Phone Number: " + phoneNumber + '\n' +
		    "Home Address: " + homeAddress + '\n' +
		    "Email: " + email + '\n' +
		    "Birthdate: " + birthdate + '\n' +
		    "User ID: " + id + '\n' +
		    "Role: " + role + '\n' +
		    "-------------------------------------------"+ '\n';
	}

	/**
	 * Represents the role of a user in the Animal Network Book system.
	 */
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

		/**
		 * Constructs a Role enum with the specified name and permissions.
		 *
		 * @param name        The name of the role.
		 * @param permissions The permissions associated with the role.
		 */
		Role(String name, Map<EntityName, Permission> permissions) {
			this.name = name;
			this.permissions = permissions;
		}

		/**
		 * Gets the name of the role.
		 *
		 * @return The name of the role.
		 */
		public String getName() {
			return name;
		}

		/**
		 * Gets the permissions associated with the role.
		 *
		 * @return The permissions associated with the role.
		 */
		public Map<EntityName, Permission> getPermissions() {
			return permissions;
		}

		/**
		 * Represents the names of entities in the Animal Network Book system.
		 */
		public enum EntityName {ANIMAL, REQUEST, SHELTER, USER}

		/**
		 * Represents the permissions for a user role in the Animal Network Book system.
		 */
		public record Permission(boolean canAdd, boolean canEdit, boolean canDelete, boolean canRead) {
		}
	}
}
