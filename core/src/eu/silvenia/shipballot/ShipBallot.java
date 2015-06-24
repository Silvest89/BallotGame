package eu.silvenia.shipballot;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import eu.silvenia.shipballot.screens.MainMenu;
import eu.silvenia.shipballot.screens.Splash;

public class ShipBallot extends Game {
	public static final String TITLE="Ship Ballot";
	public static final int WIDTH=1280,HEIGHT=720;
	Splash splashScreen;
	MainMenu mainMenu;

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
}
