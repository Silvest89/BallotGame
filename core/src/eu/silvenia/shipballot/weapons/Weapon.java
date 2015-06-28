package eu.silvenia.shipballot.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import eu.silvenia.shipballot.creature.Creature;

/**
 * Created by Johnnie Ho on 28-6-2015.
 */
public class Weapon {

    Creature owner;

    public enum WeaponType{
        PISTOL,
        SHOTGUN,
        RIFLE
    }

    private float reloadTime;
    private float reloadTimer;
    private boolean canFire;

    WeaponType weaponType;
    public Weapon(Creature owner, WeaponType weaponType){
        this.weaponType = weaponType;
        this.owner = owner;

        switch(weaponType){
            case PISTOL:{
                reloadTime = 2.0f * 60;
                break;
            }
            case RIFLE:{
                reloadTime = 2.6f * 60;
            }
            case SHOTGUN:{
                reloadTime = 2.4f * 60;
            }
        }
        reload();
    }

    public void update(){
        if(reloadTimer > 0)
            reloadTimer -= 1;

        if(!canFire && reloadTimer <= 0){
            reload();
        }
    }

    public void fire(){
        if(canFire) {
            //new Bullet(new Texture("bullet.png"), this).fireShotgun();
            fireShotgun();
            canFire = false;
        }
    }

    private void reload(){
        reloadTimer = reloadTime;
        canFire = true;
    }

    public void fireShotgun(){
        Bullet bullet = new Bullet(new Texture("bullet.png"), this);
        Body body;
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();

        // a ball
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        if(owner.getLookingDirection() == Creature.DIRECTION.EAST)
            bodyDef.position.set(owner.getBody().getPosition().x+1.2f, owner.getBody().getPosition().y);
        else
            bodyDef.position.set(owner.getBody().getPosition().x-1.2f, owner.getBody().getPosition().y);
        bodyDef.gravityScale = 0;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.2f, 0.05f);

        fixtureDef.shape = shape;
        fixtureDef.density = 10f;

        body = owner.getWorld().createBody(bodyDef);
        bullet.setBody(body);
        body.createFixture(fixtureDef);
        body.setFixedRotation(true);
        body.setUserData(bullet);
        body.setBullet(true);
        shape.dispose();

        if(owner.getLookingDirection() == Creature.DIRECTION.EAST) {
            body.setTransform(body.getPosition().x, body.getPosition().y, (float)Math.toRadians(180));
            body.applyLinearImpulse(new Vector2(2f, 0),body.getPosition(), true);
        }else
            body.applyLinearImpulse(new Vector2(-2f, 0), body.getPosition(), true);

        bullet = new Bullet(new Texture("bullet.png"), this);

        // a ball
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        if(owner.getLookingDirection() == Creature.DIRECTION.EAST)
            bodyDef.position.set(owner.getBody().getPosition().x+1.2f, owner.getBody().getPosition().y+0.8f);
        else
            bodyDef.position.set(owner.getBody().getPosition().x-1.2f, owner.getBody().getPosition().y+0.8f);
        bodyDef.gravityScale = 0;

        shape = new PolygonShape();
        shape.setAsBox(0.2f, 0.05f);

        fixtureDef.shape = shape;
        fixtureDef.density = 10f;

        body = owner.getWorld().createBody(bodyDef);
        bullet.setBody(body);
        body.createFixture(fixtureDef);
        body.setFixedRotation(true);
        body.setUserData(bullet);
        body.setBullet(true);
        shape.dispose();
        //body.applyLinearImpulse(new Vector2(2 *(float)Math.sin(-Math.toRadians(290)), 2 * (float)Math.cos(Math.toRadians(290))), body.getPosition(), true);
        if(owner.getLookingDirection() == Creature.DIRECTION.EAST) {
            body.setTransform(body.getPosition().x, body.getPosition().y, (float)Math.toRadians(200));
            body.applyLinearImpulse(new Vector2(2 *(float)Math.sin(-Math.toRadians(290)), 2 * (float)Math.cos(Math.toRadians(290))), body.getPosition(), true);
        }else {
            body.setTransform(body.getPosition().x, body.getPosition().y, (float) -Math.toRadians(200));
            body.applyLinearImpulse(new Vector2(-(2 * (float) Math.sin(-Math.toRadians(290))), 2 * (float) Math.cos(Math.toRadians(290))), body.getPosition(), true);
        }
    }
}
