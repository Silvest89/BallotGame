package eu.silvenia.shipballot.projectile;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import eu.silvenia.shipballot.EntityManager;
import eu.silvenia.shipballot.GameObject;
import eu.silvenia.shipballot.creature.Creature;
import eu.silvenia.shipballot.creature.Player;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

import javax.swing.text.html.parser.Entity;

/**
 * Created by Johnnie Ho on 26-6-2015.
 */
public class Bullet extends GameObject{
    World world;

    public Bullet(Texture texture, Player player, World world){
        super(texture);

        this.world = world;
        // reusable construction objects
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();

        // a ball
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(player.getBody().getPosition());
        bodyDef.gravityScale = 0;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.4f, 0.1f);

        fixtureDef.shape = shape;

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setUserData(this);

        body.setBullet(true);
        if(player.getLookingDirection() == Creature.DIRECTION.EAST) {
            body.setTransform(body.getPosition(), (float) Math.toRadians(180));
            body.applyLinearImpulse(new Vector2(10, 0), body.getPosition(), true);
        }
        else
            body.applyLinearImpulse(new Vector2(-10, 0), body.getPosition(), true);

        shape.dispose();
    }

    @Override
    public void handleCollision(Body body) {
        if(body.getUserData() == null)
           EntityManager.setToDestroy(this);


    }

    @Override
    public void update(float delta) {

    }
}
