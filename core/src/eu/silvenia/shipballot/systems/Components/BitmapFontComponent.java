package eu.silvenia.shipballot.systems.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Created by Johnnie Ho on 3-7-2015.
 */
public class BitmapFontComponent extends Component {
    public BitmapFont bFont;
    public String text;

    public BitmapFontComponent(BitmapFont font, String text) {
        bFont = font;
        font.getData().setScale(0.04f);
        font.setColor(Color.RED);
        this.text = text;
    }
}