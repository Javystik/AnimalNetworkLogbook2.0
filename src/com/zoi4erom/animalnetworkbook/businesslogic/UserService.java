package com.zoi4erom.animalnetworkbook.businesslogic;

import com.google.gson.JsonDeserializer;
import com.zoi4erom.animalnetworkbook.aui.Renderable;
import com.zoi4erom.animalnetworkbook.persistence.entity.Shelter;
import com.zoi4erom.animalnetworkbook.persistence.entity.User;
import com.zoi4erom.animalnetworkbook.persistence.jsonhandler.JsonConverter;
import com.zoi4erom.animalnetworkbook.persistence.jsonhandler.JsonPaths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserService {
	private UserService() {}
	static List<User> users(){
		return JsonConverter.deserialization(JsonPaths.USER, User.class);
	}
	public static List<User> getAllUsers() {
		List<User> allUsers = users();

		if (allUsers == null) {
			allUsers = new ArrayList<>();
		}

		return allUsers;
	}
	public static List<User> findUserByName(String fullName) {
		List<User> users = users();

		return users.stream()
		    .filter(user -> user.getFullName().equals(fullName))
		    .collect(Collectors.toList());
	}
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
}