package com.zoi4erom.animalnetworkbook.businesslogic;

import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * Utility class providing common validation methods.
 */
public final class ValidatorServiceUtil {

	private ValidatorServiceUtil() {
	}

	/**
	 * Checks if the specified value is blank (null, empty, or contains only whitespace).
	 *
	 * @param value the value to validate
	 * @return true if the value is blank, false otherwise
	 */
	public static Boolean isFieldBlankValidate(String value) {
		return value.isBlank();
	}

	/**
	 * Checks if the length of the specified value is outside the specified size range.
	 *
	 * @param value        the value to validate
	 * @param minimumSize  the minimum allowed size
	 * @param maximumSize  the maximum allowed size
	 * @return true if the value's length is outside the specified range, false otherwise
	 */
	public static Boolean isFieldSizeValidate(String value, int minimumSize, int maximumSize) {
		return value.length() < minimumSize || value.length() > maximumSize;
	}

	/**
	 * Checks if the specified password follows a valid pattern.
	 *
	 * @param password the password to validate
	 * @return true if the password is valid, false otherwise
	 */
	public static Boolean isFieldPasswordValidate(String password) {
		Pattern validPasswordPattern = Pattern.compile("^[a-zA-Z0-9]+$");
		return !validPasswordPattern.matcher(password).matches();
	}

	/**
	 * Checks if the specified date is in the future or the current date.
	 *
	 * @param date the date to validate
	 * @return true if the date is in the future or the current date, false otherwise
	 */
	public static Boolean isValidDate(LocalDate date) {
		LocalDate currentDate = LocalDate.now();
		return date.isAfter(currentDate) || date.isEqual(currentDate);
	}
}
