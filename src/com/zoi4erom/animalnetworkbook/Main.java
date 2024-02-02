package com.zoi4erom.animalnetworkbook;

import com.zoi4erom.animalnetworkbook.aui.AuthenticationAndRegistrationView;
import com.zoi4erom.animalnetworkbook.persistence.jsonhandler.FileCheckerUtil;
import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		FileCheckerUtil.checkAndCreateDirectoriesAndFiles();
		while (true) {
			AuthenticationAndRegistrationView authenticationAndRegistrationView
			    = new AuthenticationAndRegistrationView();
			authenticationAndRegistrationView.render();
		}
	}
}