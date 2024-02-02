package com.zoi4erom.animalnetworkbook.businesslogic;

import com.zoi4erom.animalnetworkbook.persistence.entity.User;
import com.zoi4erom.animalnetworkbook.persistence.jsonhandler.JsonConverter;
import com.zoi4erom.animalnetworkbook.persistence.jsonhandler.JsonPaths;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The {@code UserService} class provides methods for managing user data.
 */
public class UserService {

	/**
	 * Retrieves a list of all users.
	 *
	 * @return A list of all users.
	 */
	public static List<User> getAllUsers() {
		List<User> allUsers = users();
		if (allUsers == null) {
			allUsers = new ArrayList<>();
		}
		return allUsers;
	}

	/**
	 * Finds users by their full name.
	 *
	 * @param fullName The full name to search for.
	 * @return A list of users matching the given full name.
	 */
	public static List<User> findUserByName(String fullName) {
		List<User> users = users();
		return users.stream()
		    .filter(user -> user.getFullName().equals(fullName))
		    .collect(Collectors.toList());
	}

	/**
	 * Updates user information.
	 *
	 * @param updatedUser The updated user information.
	 */
	public static void updateUser(User updatedUser) {
		List<User> allUsers = users();
		if (allUsers != null) {
			for (int i = 0; i < allUsers.size(); i++) {
				User user = allUsers.get(i);
				if (user.getId().equals(updatedUser.getId())) {
					allUsers.remove(i);
					allUsers.add(updatedUser);
					JsonConverter.serialization(allUsers, JsonPaths.USER);
					return;
				}
			}
		}
	}

	/**
	 * Finds a user by their UUID.
	 *
	 * @param uuidString The UUID in string format.
	 * @return The user corresponding to the given UUID, or {@code null} if not found.
	 */
	public static User findUserByUUID(String uuidString) {
		List<User> users = users();
		try {
			UUID uuid = UUID.fromString(uuidString);
			return users.stream()
			    .filter(user -> user.getId().equals(uuid))
			    .findFirst()
			    .orElse(null);
		} catch (IllegalArgumentException e) {
			System.err.println("Invalid UUID format");
			return null;
		}
	}

	/**
	 * Utility method that deserializes user data from JSON using {@link JsonConverter}.
	 *
	 * @return A list of user objects.
	 */
	private static List<User> users() {
		return JsonConverter.deserialization(JsonPaths.USER, User.class);
	}
}
