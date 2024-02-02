package com.zoi4erom.animalnetworkbook;

import com.zoi4erom.animalnetworkbook.aui.AuthenticationAndRegistrationView;
import com.zoi4erom.animalnetworkbook.persistence.jsonhandler.FileCheckerUtil;
import java.io.IOException;

/**
 * The main class for the Animal Network Book program.
 * Initializes and checks necessary files and directories, and triggers the authentication and registration view.
 */
public class Main {

	/**
	 * The main method called upon program execution.
	 * Initializes and checks files and directories, then displays the authentication and registration view.
	 *
	 * @param args Command line arguments (not used in this program).
	 * @throws IOException If an I/O error occurs during file and directory initialization.
	 */
	public static void main(String[] args) throws IOException {
		while (true) {
			System.setProperty("console.encoding", "Cp1251");

			FileCheckerUtil.checkAndCreateDirectoriesAndFiles();

			// Display the authentication and registration view
			AuthenticationAndRegistrationView authenticationAndRegistrationView
			    = new AuthenticationAndRegistrationView();
			authenticationAndRegistrationView.render();
		}
	}
}
