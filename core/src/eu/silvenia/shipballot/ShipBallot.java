package eu.silvenia.shipballot;

import eu.silvenia.shipballot.screens.Game;
import eu.silvenia.shipballot.screens.MainMenu;
import eu.silvenia.shipballot.screens.Splash;

public class ShipBallot extends com.badlogic.gdx.Game {
	public static final String TITLE="Ship Ballot";
	public static final int WIDTH=1280,HEIGHT=720;
	Splash splashScreen;
	MainMenu mainMenu;
	Game game;

	@Override
	public void create() {
		setSplashScreen();
	}

	public void setSplashScreen()
	{
		splashScreen = new Splash(this);

		setScreen(splashScreen);
	}

	public void goToMainMenu(){
		mainMenu = new MainMenu(this);

		setScreen(mainMenu);
	}

	public void goToGame(){
		game = new Game(this);

		setScreen(game);
	}
}
