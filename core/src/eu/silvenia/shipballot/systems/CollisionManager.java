package eu.silvenia.shipballot.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.*;
import eu.silvenia.shipballot.Mappers;
import eu.silvenia.shipballot.PhysicsManager;

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
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        /*if ((fixtureA.getBody().isBullet() && fixtureB.getFilterData().categoryBits == PhysicsManager.LEVEL_BITS) ||
                (fixtureB.getBody().isBullet() && fixtureA.getFilterData().categoryBits == PhysicsManager.LEVEL_BITS)) {

            position = contact.getWorldManifold().getPoints()[0];
            //EntityManager.createBulletHole(position, normal);
        }*/


        Entity actorA = (Entity) fixtureA.getBody().getUserData();
        Entity actorB = (Entity) fixtureB.getBody().getUserData();

        createCollision(actorA, actorB);
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        Entity actorA = (Entity) fixtureA.getUserData();
        Entity actorB = (Entity) fixtureB.getUserData();
    }
}
