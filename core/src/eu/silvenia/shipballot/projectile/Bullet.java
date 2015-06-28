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
public class Bullet extends Box2DSprite implements GameObject{
    World world;
    Body body;

    private float damage = 20;
    private Creature owner;

    public Bullet(Texture texture, Player player, World world){
        super(texture);
        this.world = world;
        this.owner = player;

        // reusable construction objects
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();

        // a ball
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        if(player.getLookingDirection() == Creature.DIRECTION.EAST)
            bodyDef.position.set(player.getBody().getPosition().x+1f, player.getBody().getPosition().y);
        else
            bodyDef.position.set(player.getBody().getPosition().x-1f, player.getBody().getPosition().y);
        bodyDef.gravityScale = 0;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.2f, 0.05f);

        fixtureDef.shape = shape;
        fixtureDef.density = 10f;

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setUserData(this);
        body.setFixedRotation(true);

        body.setBullet(true);
        if(player.getLookingDirection() == Creature.DIRECTION.EAST) {
            body.setTransform(body.getPosition(), (float) Math.toRadians(180));
            body.applyLinearImpulse(new Vector2(3f, 0), body.getPosition(), true);
        }
        else
            body.applyLinearImpulse(new Vector2(-3f, 0), body.getPosition(), true);

        shape.dispose();
    }

    @Override
    public void handleCollision(Body body) {
        if(body.getUserData() == null)
           EntityManager.setToDestroy(this);

        if(body.getUserData() instanceof Player){
            Player player = (Player)body.getUserData();
            if(player != this.owner) {
                player.hit(this);
                EntityManager.setToDestroy(this);
            }
        }

    }

    @Override
    public Body getBody() {
        return body;
    }

    @Override
    public void update(float delta) {

    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public Creature getOwner() {
        return owner;
    }

    public void setOwner(Creature owner) {
        this.owner = owner;
    }
}
