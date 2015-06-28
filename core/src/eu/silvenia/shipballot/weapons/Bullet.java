package eu.silvenia.shipballot.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import eu.silvenia.shipballot.EntityManager;
import eu.silvenia.shipballot.GameObject;
import eu.silvenia.shipballot.creature.Creature;
import eu.silvenia.shipballot.creature.Player;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

/**
 * Created by Johnnie Ho on 26-6-2015.
 */
public class Bullet extends Box2DSprite implements GameObject{
    World world;
    Body body;

    private float damage = 10;
    private Creature owner;

    public Bullet(Texture texture, Weapon weapon){
        super(texture);
        this.owner = weapon.owner;
        this.world = owner.getWorld();
    }

    public void setBody(Body body){
        this.body = body;

    }
    @Override
    public void handleCollision(Body body) {
        if(body.getUserData() == null) {
            EntityManager.setToDestroy(this);
        }

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

    public Creature getOwner() {
        return owner;
    }
}
