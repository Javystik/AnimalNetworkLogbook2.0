package com.zoi4erom.animalnetworkbook.businesslogic;

import com.zoi4erom.animalnetworkbook.persistence.entity.User;
import com.zoi4erom.animalnetworkbook.persistence.jsonhandler.JsonConverter;
import com.zoi4erom.animalnetworkbook.persistence.jsonhandler.JsonPaths;
import org.mindrot.bcrypt.BCrypt;

import java.util.List;

/**
 * Provides authentication services for user authorization.
 */
public class AuthorizationService {

	/**
	 * Checks if the provided plain password matches the hashed password.
	 *
	 * @param plainPassword  the plain password
	 * @param hashedPassword the hashed password
	 * @return true if the passwords match, false otherwise
	 */
	private static boolean checkPassword(String plainPassword, String hashedPassword) {
		return BCrypt.checkpw(plainPassword, hashedPassword);
	}

	/**
	 * Authenticates a user based on the provided full name, password, and email.
	 *
	 * @param fullName the full name of the user
	 * @param password the password of the user
	 * @param email    the email of the user
	 * @return the authenticated user or null if authentication fails
	 */
	public static User authorization(String fullName, String password, String email) {
		List<User> userList = JsonConverter.deserialization(JsonPaths.USER, User.class);

		return userList.stream()
		    .filter(user -> user.getFullName().equals(fullName) && user.getEmail().equals(email))
		    .findFirst()
		    .filter(user -> checkPassword(password, user.getPassword()))
		    .orElse(null);
	}
}
