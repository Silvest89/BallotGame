package eu.silvenia.shipballot.systems.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

/**
 * Created by Johnnie Ho on 29-6-2015.
 */
public class SpriteComponent extends Component{
    public Sprite sprite;

    public SpriteComponent(Texture texture) {
        this.sprite = new Sprite(texture);
    }
    public SpriteComponent(Sprite sprite) {
        this.sprite = new Sprite(sprite);
    }
    public SpriteComponent(AnimatedSprite sprite) {
        this.sprite = new Sprite(sprite);
    }
}
