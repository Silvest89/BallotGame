package eu.silvenia.shipballot;

import com.badlogic.gdx.Gdx;

/**
 * Created by Johnnie Ho on 26-6-2015.
 */
public class FpsTimer {
    public static double time = 1.0d;

    private static int defaultFPS = 60;

    public static void update() {
        int actualFPS = Gdx.graphics.getFramesPerSecond();
        actualFPS = (actualFPS == 0) ?  3000 : actualFPS;
        time = (double)defaultFPS / actualFPS;
    }
}
