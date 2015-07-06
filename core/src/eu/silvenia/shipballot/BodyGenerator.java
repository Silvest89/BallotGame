package eu.silvenia.shipballot;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import eu.silvenia.shipballot.creature.Player;
import eu.silvenia.shipballot.systems.Components.*;


/**
 * Created by KevinPC on 7/1/2015.
 */
public class BodyGenerator {

    public static void generateBullet(World world, Engine engine, Entity entity, int radians, float targetPositionX, float targetPositionY) {
        float positionX = 0f;
        float weaponSpeed = Mappers.weaponMap.get(entity).getWeaponSpeed();
        Entity player = Mappers.attachedMap.get(entity).attachedTo;
        Entity bullet = new Entity();
        Body body;
        Body playerBody = Mappers.bodyMap.get(player).body;
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();

        // a ball
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        Sprite sprite = new Sprite(new Texture("bullet.png"));
        sprite.setSize(0.4f, 0.1f);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);

        if(Mappers.playerData.get(player).lookingDirection == Player.DIRECTION.EAST) {
            positionX = playerBody.getPosition().x + targetPositionX;
            sprite.setRotation(radians - 90);
        }
        else{
            positionX = playerBody.getPosition().x - targetPositionX;
            sprite.setRotation(-radians - 90);
        }

        bodyDef.position.set(positionX, playerBody.getPosition().y + targetPositionY);
        bodyDef.gravityScale = 0;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.2f, 0.05f);

        fixtureDef.shape = shape;
        fixtureDef.density = 10f;
        fixtureDef.filter.categoryBits = PhysicsManager.BULLET;

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setFixedRotation(true);
        body.setUserData(bullet);
        body.setBullet(true);
        shape.dispose();

        long damage = Mappers.weaponMap.get(entity).getWeaponDamage();

        damage = (long)(Math.random()*damage + (damage / 2));
        BodyComponent bodyComponent = new BodyComponent(body);
        bullet.add(new RenderableComponent())
                .add(new PositionComponent(body.getPosition().x, body.getPosition().y, 0))
                .add(new SpriteComponent(sprite))
                .add(new BulletCollisionComponent())
                .add(new AttachedComponent(entity))
                .add(new BulletDamageComponent(damage))
                .add(new TypeComponent(PhysicsManager.COL_FRIENDLY_BULLET));
        bullet.add(bodyComponent);

        engine.addEntity(bullet);
        AshleyEntityManager.add(bullet);

        if(Mappers.playerData.get(player).lookingDirection == Player.DIRECTION.EAST) {
            body.setTransform(body.getPosition().x, body.getPosition().y, (float)Math.toRadians(radians-90));
            body.applyLinearImpulse(new Vector2(weaponSpeed *(float)Math.sin(-Math.toRadians(radians)), weaponSpeed * (float)Math.cos(Math.toRadians(radians))), body.getPosition(), true);
        }else {
            body.setTransform(body.getPosition().x, body.getPosition().y, (float)-Math.toRadians(radians-90));
            body.applyLinearImpulse(new Vector2(-(weaponSpeed * (float) Math.sin(-Math.toRadians(radians))), weaponSpeed * (float) Math.cos(Math.toRadians(radians))), body.getPosition(), true);
        }
    }
}
