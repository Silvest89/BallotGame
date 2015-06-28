package eu.silvenia.shipballot;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

/**
 * Created by Johnnie Ho on 28-6-2015.
 */
public abstract class GameObject extends Box2DSprite {

    protected Body body;
    public GameObject(Texture texture){
        super(texture);
    }
    public GameObject(AnimatedSprite sprite){
        super(sprite);
    }

    public Body getBody(){
        return body;
    }

    public abstract void handleCollision(Body body);
    public abstract void update(float delta);
}
