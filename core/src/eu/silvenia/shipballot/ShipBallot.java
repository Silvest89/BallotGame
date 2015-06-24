package eu.silvenia.shipballot;

import com.badlogic.gdx.Game;
import eu.silvenia.shipballot.screens.GameScreen;
import eu.silvenia.shipballot.screens.MainMenu;
import eu.silvenia.shipballot.screens.Splash;

public class ShipBallot extends Game {
	public static final String TITLE="Ship Ballot";
	public static final int WIDTH=1280,HEIGHT=720;
	Splash splashScreen;
	MainMenu mainMenu;
	GameScreen gameScreen;

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
		gameScreen = new GameScreen(this);

		setScreen(gameScreen);
	}
}
