package com.zoi4erom.animalnetworkbook.aui;

import static com.zoi4erom.animalnetworkbook.aui.MainMenuView.MainMenu.*;

import com.zoi4erom.animalnetworkbook.businesslogic.GenerateReport;
import com.zoi4erom.animalnetworkbook.persistence.entity.User;
import de.codeshelf.consoleui.prompt.ConsolePrompt;
import de.codeshelf.consoleui.prompt.ListResult;
import de.codeshelf.consoleui.prompt.builder.PromptBuilder;
import java.io.IOException;

public class MainMenuView implements Renderable {
	private User activeUser;
	private ShelterView shelterView;
	private AnimalView animalView;
	private RequestView requestView;
	private UserView userView;

	public MainMenuView(User activeUser) {
		this.activeUser = activeUser;
		this.shelterView = new ShelterView(activeUser);
		this.animalView = new AnimalView(activeUser);
		this.requestView = new RequestView(activeUser);
		this.userView = new UserView(activeUser);
	}
	private void process(MainMenuView.MainMenu selectedItem) throws IOException {
		switch (selectedItem) {
			case SHELTER -> shelterView.render();
			case ANIMALS -> animalView.render();
			case REQUEST -> requestView.render();
			case USER -> userView.render();
			case REPORTS -> {
				GenerateReport.start();
				render();
			}

			case LOG_OUT -> {
				System.out.print("\033[H\033[2J");
				activeUser = null;
				AuthenticationAndRegistrationView authenticationAndRegistrationView
				    = new AuthenticationAndRegistrationView();
				authenticationAndRegistrationView.render();
			}

			case EXIT -> {
				System.exit(0);
			}
		}
	}
	@Override
	public void render() throws IOException {
		ConsolePrompt prompt = new ConsolePrompt();
		PromptBuilder promptBuilder = prompt.getPromptBuilder();

		promptBuilder.createListPrompt()
		    .name("main-menu")
		    .message("Оберіть пункт меню")

		    .newItem(SHELTER.toString()).text(SHELTER.getName()).add()
		    .newItem(ANIMALS.toString()).text(ANIMALS.getName()).add()
		    .newItem(REQUEST.toString()).text(REQUEST.getName()).add()
		    .newItem(USER.toString()).text(USER.getName()).add()
		    .newItem(REPORTS.toString()).text(REPORTS.getName()).add()
		    .newItem(LOG_OUT.toString()).text(LOG_OUT.getName()).add()
		    .newItem(EXIT.toString()).text(EXIT.getName()).add()
		    .addPrompt();

		var result = prompt.prompt(promptBuilder.build());
		ListResult resultItem = (ListResult) result.get("main-menu");

		MainMenuView.MainMenu selectedItem = MainMenuView.MainMenu.valueOf(resultItem.getSelectedId());

		User.Role role = activeUser != null ? activeUser.getRole() : null;

		if ((selectedItem == USER || selectedItem == REPORTS) && (role == null || role != User.Role.ADMIN)) {
			System.out.println("У вас немає дозволу на управління користувачами або генерацію документації.");
			render();
		} else {
			process(selectedItem);
		}

	}
	public enum MainMenu {
		SHELTER("Приюти"),
		ANIMALS("Тварини"),
		REQUEST("Запити"),
		USER("Користувачі"),
		REPORTS("Зробити документацію"),
		LOG_OUT("Вийти з облікового запису"),
		EXIT("Вихід з програми");
		private final String name;
		MainMenu(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
}