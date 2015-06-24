package eu.silvenia.shipballot.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import eu.silvenia.shipballot.ShipBallot;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width=ShipBallot.WIDTH; // sets window width
		config.height=ShipBallot.HEIGHT;  // sets window height
		config.resizable = false;
		new LwjglApplication(new ShipBallot(), config);
	}
}
