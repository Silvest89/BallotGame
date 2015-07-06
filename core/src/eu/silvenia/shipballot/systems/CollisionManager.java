package eu.silvenia.shipballot.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.physics.box2d.*;
import eu.silvenia.shipballot.AshleyEntityManager;
import eu.silvenia.shipballot.Mappers;
import eu.silvenia.shipballot.PhysicsManager;
import eu.silvenia.shipballot.systems.Components.*;

/**
 * Created by Johnnie Ho on 29-6-2015.
 */
public class CollisionManager implements ContactListener {
    private Engine engine;

    private void createCollision(Entity a, Entity b) {
        if(a == null && b != null) {
            if(Mappers.playerColMap.has(b))
                Mappers.playerColMap.get(b).handleCollision(engine, a, b);
            else
                Mappers.bulletColMap.get(b).handleCollision(engine, b, a);
            return;
        }
        else if(a != null && b == null){
            Mappers.playerColMap.get(a).handleCollision(engine, a, b);
            return;
        }
        short typeA;
        short typeB;

        try {
            typeA = Mappers.typeMap.get(a).type;
            typeB = Mappers.typeMap.get(b).type;
        } catch (Exception e) {
            // If one of the objects doesn't have a type, then it's not a useful collision
            return;
        }

        if (typeA == PhysicsManager.COL_PLAYER)
            Mappers.playerColMap.get(a).handleCollision(engine, a, b);
        else if (typeA == PhysicsManager.COL_FRIENDLY_BULLET)
            Mappers.bulletColMap.get(a).handleCollision(engine, a, b);

        if (typeB == PhysicsManager.COL_PLAYER)
            Mappers.playerColMap.get(b).handleCollision(engine, b, a);
        else if (typeB == PhysicsManager.COL_FRIENDLY_BULLET)
            Mappers.bulletColMap.get(b).handleCollision(engine, b, a);

    }

    public CollisionManager(Engine engine, World world) {
        this.engine = engine;
        world.setContactListener(this);
        //world.setContactFilter(this);
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if(fixtureA.getFilterData().categoryBits == PhysicsManager.BULLET){
            handleBulletCollision(fixtureA, fixtureB);
            return;
        }
        else if(fixtureB.getFilterData().categoryBits == PhysicsManager.BULLET){
            handleBulletCollision(fixtureB, fixtureA);
            return;
        }
        /*if ((fixtureA.getBody().isBullet() && fixtureB.getFilterData().categoryBits == PhysicsManager.LEVEL_BITS) ||
                (fixtureB.getBody().isBullet() && fixtureA.getFilterData().categoryBits == PhysicsManager.LEVEL_BITS)) {

            position = contact.getWorldManifold().getPoints()[0];
            //EntityManager.createBulletHole(position, normal);
        }*/

        if(fixtureA.isSensor()) {
            handleSensorCollision(fixtureA, false);
            return;
        }

        if(fixtureB.isSensor()) {
            handleSensorCollision(fixtureB, false);
            return;
        }


        Entity actorA = (Entity) fixtureA.getUserData();
        Entity actorB = (Entity) fixtureB.getBody().getUserData();

        //createCollision(actorA, actorB);
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if(fixtureA.isSensor()) {
            handleSensorCollision(fixtureA, true);
        }
        else if(fixtureB.isSensor())
            handleSensorCollision(fixtureB, true);

        Entity actorA = (Entity) fixtureA.getUserData();
        Entity actorB = (Entity) fixtureB.getUserData();
    }

    public void handleSensorCollision(Fixture fixture, boolean endContact){
        Entity entity2 = (Entity) fixture.getBody().getUserData();
        if(!endContact) {
            if (fixture.getFilterData().categoryBits == PhysicsManager.FOOT_SENSOR) {
                Entity entity = (Entity) fixture.getBody().getUserData();
                Mappers.playerData.get(entity).canJump = true;
            }
        }
        else{
            if (fixture.getFilterData().categoryBits == PhysicsManager.FOOT_SENSOR) {
                Entity entity = (Entity) fixture.getBody().getUserData();
                Mappers.playerData.get(entity).canJump = false;
            }
        }

    }

    public void handleBulletCollision(Fixture fixtureA, Fixture fixtureB){;
        if(fixtureB.getBody().getUserData() == null){

        }
        else{
            if(fixtureA.getBody().isBullet() != fixtureB.getBody().isBullet()) {
                Entity bullet = (Entity) fixtureA.getBody().getUserData();
                Entity entity = (Entity) fixtureB.getBody().getUserData();
                if (entity != null && bullet != null) {
                    Mappers.playerData.get(entity).health -= Mappers.bulletDamageMap.get(bullet).damage;

                    Body body = Mappers.bodyMap.get(entity).body;
                    Entity indicator = new Entity();
                    BitmapFontComponent bFontCom = new BitmapFontComponent(new BitmapFont(), "" + Mappers.bulletDamageMap.get(bullet).damage);
                    PositionComponent posCom = new PositionComponent(body.getPosition().x, body.getPosition().y, 500);
                    VelocityComponent vCom = new VelocityComponent((float) (Math.random() - 0.5d) * 1f, 0.3f); // make these random
                    RenderableComponent renderCom = new RenderableComponent();
                    TransparentComponent transCom = new TransparentComponent(1);
                    DeathTimerComponent deathCom = new DeathTimerComponent(2000); // die after 2 seconds?
                    FauxGravityComponent fauxGCom = new FauxGravityComponent(0.01f);
                    indicator.add(bFontCom)
                            .add(posCom)
                            .add(vCom)
                            .add(renderCom)
                            .add(transCom)
                            .add(deathCom)
                            .add(fauxGCom);
                    AshleyEntityManager.getEngine().addEntity(indicator);
                }
            }
        }
        Entity bullet = (Entity) fixtureA.getBody().getUserData();
        if(bullet != null) {
            bullet.getComponent(BodyComponent.class).body.setUserData(null); // <= is this a good solution?
            bullet.remove(RenderableComponent.class);
            AshleyEntityManager.setToDestroy(bullet);
        }
    }
}
