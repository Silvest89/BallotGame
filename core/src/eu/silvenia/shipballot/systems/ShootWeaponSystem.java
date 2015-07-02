package eu.silvenia.shipballot.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.*;
import eu.silvenia.shipballot.BodyGenerator;
import eu.silvenia.shipballot.Mappers;
import eu.silvenia.shipballot.systems.Components.*;

/**
 * Created by Johnnie Ho on 29-6-2015.
 */
public class ShootWeaponSystem extends IteratingSystem {
    private Engine engine;
    private World world;

    public ShootWeaponSystem(Engine engine, World world) {
        super(Family.all(ShootingComponent.class, WeaponDataComponent.class, BodyComponent.class).get());
        this.engine = engine;
        this.world = world;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        float reloadTime = Mappers.weaponMap.get(entity).getReloadTime();
        if(Mappers.weaponMap.get(entity).canFire && Mappers.weaponMap.get(entity).reloadTimer >= reloadTime){
            BodyGenerator.generateBullet(world, engine, entity, 270 , 1.5f);
           /* Entity eBullet = new Entity();
            Body body2 = Mappers.bodyMap.get(entity).body;
            float weaponSpeed = Mappers.weaponMap.get(entity).getWeaponSpeed();

            Sprite sprite = new Sprite(new Texture("bullet.png"));
            sprite.setSize(0.4f, 0.1f);
            sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);

            RenderableComponent renderComponent   = new RenderableComponent();
            float position = 0f;
            //System.out.println(Mappers.playerData.get(entity).lookingDirection);

            Body body;
            BodyDef bodyDef = new BodyDef();
            FixtureDef fixtureDef = new FixtureDef();

            // a ball
            bodyDef.type = BodyDef.BodyType.DynamicBody;

            if(Mappers.playerData.get(entity).lookingDirection == PlayerTest.DIRECTION.EAST)
                position = body2.getPosition().x + 1.5f;
            else
                position = body2.getPosition().x - 1.5f;

            bodyDef.position.set(position, body2.getPosition().y);
            bodyDef.gravityScale = 0;

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(0.2f, 0.05f);

            fixtureDef.shape = shape;
            fixtureDef.density = 10f;

            body = world.createBody(bodyDef);
            body.createFixture(fixtureDef);
            body.setFixedRotation(true);
            body.setUserData(eBullet);
            body.setBullet(true);
            shape.dispose();

            PositionComponent positionComponent = new PositionComponent(body2.getPosition().x, body2.getPosition().y, 0);
            SpriteComponent     spriteComponent   = new SpriteComponent(sprite);
            BulletCollisionComponent bulletColCom = new BulletCollisionComponent();
            TypeComponent typeComponent     = new TypeComponent(PhysicsManager.COL_FRIENDLY_BULLET);

            BodyComponent bodyComponent     = new BodyComponent(body);
            eBullet.add(renderComponent)
                    .add(positionComponent)
                    .add(spriteComponent)
                    .add(bulletColCom)
                    .add(typeComponent);
            eBullet.add(bodyComponent);
            if(Mappers.playerData.get(entity).lookingDirection == PlayerTest.DIRECTION.EAST) {
                body.setTransform(body.getPosition().x, body.getPosition().y, (float)Math.toRadians(180));
                body.applyLinearImpulse(new Vector2(weaponSpeed *(float)Math.sin(-Math.toRadians(270)), weaponSpeed * (float)Math.cos(Math.toRadians(270))), body.getPosition(), true);
            }else
                body.applyLinearImpulse(new Vector2(-(weaponSpeed * (float) Math.sin(-Math.toRadians(270))), weaponSpeed * (float) Math.cos(Math.toRadians(270))), body.getPosition(), true);
            //body.applyLinearImpulse(new Vector2(-(weaponSpeed * (float) Math.sin(-Math.toRadians(270))), weaponSpeed * (float) Math.cos(Math.toRadians(270))), body.getPosition(), true);

            engine.addEntity(eBullet);
            AshleyEntityManager.add(eBullet);*/
            Mappers.weaponMap.get(entity).canFire = false;
            Mappers.weaponMap.get(entity).reloadTimer = 0f;
        }
        else{
            if(Mappers.weaponMap.get(entity).reloadTimer < reloadTime) {
                Mappers.weaponMap.get(entity).reloadTimer += deltaTime;
            }
        }
    }
}
