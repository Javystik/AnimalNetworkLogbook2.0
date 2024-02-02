package com.zoi4erom.animalnetworkbook.businesslogic;

import com.zoi4erom.animalnetworkbook.persistence.entity.User;
import com.zoi4erom.animalnetworkbook.persistence.jsonhandler.JsonConverter;
import com.zoi4erom.animalnetworkbook.persistence.jsonhandler.JsonPaths;
import java.util.List;
import org.mindrot.bcrypt.BCrypt;

public class AuthorizationService {
	private static boolean checkPassword(String plainPassword, String hashedPassword) {
		return BCrypt.checkpw(plainPassword, hashedPassword);
	}
	public static User authorization(String fullName, String password, String email) {
		List<User> userList = JsonConverter.deserialization(JsonPaths.USER, User.class);

		return userList.stream()
		    .filter(user -> user.getFullName().equals(fullName) && user.getEmail().equals(email))
		    .findFirst()
		    .filter(user -> checkPassword(password, user.getPassword()))
		    .orElse(null);
	}
}