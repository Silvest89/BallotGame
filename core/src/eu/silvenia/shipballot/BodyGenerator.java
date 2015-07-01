package eu.silvenia.shipballot;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import eu.silvenia.shipballot.creature.PlayerTest;
import eu.silvenia.shipballot.systems.Components.*;


/**
 * Created by KevinPC on 7/1/2015.
 */
public class BodyGenerator {

    public static void generateBullet(World world, Engine engine, Entity entity, int radians, float targetPosition) {
        float position = 0f;
        float weaponSpeed = Mappers.weaponMap.get(entity).getWeaponSpeed();
        Entity bullet = new Entity();
        Body body;
        Body playerBody = Mappers.bodyMap.get(entity).body;
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();

        // a ball
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        if(Mappers.playerData.get(entity).lookingDirection == PlayerTest.DIRECTION.EAST)
            position = playerBody.getPosition().x + targetPosition;
        else
            position = playerBody.getPosition().x - targetPosition;

        bodyDef.position.set(position, playerBody.getPosition().y);
        bodyDef.gravityScale = 0;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.2f, 0.05f);

        fixtureDef.shape = shape;
        fixtureDef.density = 10f;

        Sprite sprite = new Sprite(new Texture("bullet.png"));
        sprite.setSize(0.4f, 0.1f);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);



        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setFixedRotation(true);
        body.setUserData(bullet);
        body.setBullet(true);
        shape.dispose();

        BodyComponent bodyComponent = new BodyComponent(body);
        bullet.add(new RenderableComponent())
                .add(new PositionComponent(body.getPosition().x, body.getPosition().y, 0))
                .add(new SpriteComponent(sprite))
                .add(new BulletCollisionComponent())
                .add(new TypeComponent(PhysicsManager.COL_FRIENDLY_BULLET));
        bullet.add(bodyComponent);

        engine.addEntity(bullet);
        AshleyEntityManager.add(bullet);

        if(Mappers.playerData.get(entity).lookingDirection == PlayerTest.DIRECTION.EAST) {
            body.setTransform(body.getPosition().x, body.getPosition().y, (float)Math.toRadians(180));
            body.applyLinearImpulse(new Vector2(weaponSpeed *(float)Math.sin(-Math.toRadians(270)), weaponSpeed * (float)Math.cos(Math.toRadians(270))), body.getPosition(), true);
        }else
            body.applyLinearImpulse(new Vector2(-(weaponSpeed * (float) Math.sin(-Math.toRadians(270))), weaponSpeed * (float) Math.cos(Math.toRadians(270))), body.getPosition(), true);
        //body.applyLinearImpulse(new Vector2(-(weaponSpeed * (float) Math.sin(-Math.toRadians(270))), weaponSpeed * (float) Math.cos(Math.toRadians(270))), body.getPosition(), true);
    }
}
