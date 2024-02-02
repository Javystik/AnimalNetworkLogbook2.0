package com.zoi4erom.animalnetworkbook.businesslogic;

import java.time.LocalDate;
import java.util.regex.Pattern;

public final class ValidatorServiceUtil {

	private ValidatorServiceUtil() {
	}
	public static Boolean isFieldBlankValidate(String value){
		return value.isBlank();
	}
	public static Boolean isFieldSizeValidate(String value, int minimumSize, int maximumSize){
		return value.length() < minimumSize || value.length() > maximumSize;
	}
	public static Boolean isFieldPasswordValidate(String password) {
		Pattern validPasswordPattern = Pattern.compile("^[a-zA-Z0-9]+$");
		return !validPasswordPattern.matcher(password).matches();
	}
	public static Boolean isValidDate(LocalDate date){
		LocalDate currentDate = LocalDate.now();
		return date.isAfter(currentDate) || date.isEqual(currentDate);
	}
}
