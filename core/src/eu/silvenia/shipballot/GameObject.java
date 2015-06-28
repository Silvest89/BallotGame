package eu.silvenia.shipballot;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

/**
 * Created by Johnnie Ho on 28-6-2015.
 */
public interface GameObject {

    void update(float delta);
    void draw(Batch batch);
    void handleCollision(Body body);

    Body getBody();

}
