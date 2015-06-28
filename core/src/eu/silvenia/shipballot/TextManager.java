package eu.silvenia.shipballot;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Johnnie Ho on 26-6-2015.
 */
public class TextManager {
    private static BitmapFont font = new BitmapFont();
    private static Batch batch;

    public static void setBatch(Batch batch2){
        font.setColor(Color.WHITE);
        font.getData().setScale(0.05f);
        batch = batch2;
    }

    public static void Draw(String text, OrthographicCamera cam){
        Vector3 position = new Vector3(0, 0, 0);
        cam.unproject(position);
        font.draw(batch, text, position.x, position.y);
    }
}
