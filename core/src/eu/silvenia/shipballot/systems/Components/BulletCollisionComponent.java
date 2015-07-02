package eu.silvenia.shipballot.systems.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import eu.silvenia.shipballot.AshleyEntityManager;
import eu.silvenia.shipballot.Collidable;
import eu.silvenia.shipballot.Mappers;
import eu.silvenia.shipballot.PhysicsManager;

/**
 * Created by Johnnie Ho on 29-6-2015.
 */
public class BulletCollisionComponent extends Component implements Collidable {
    @Override
    public void handleCollision(Engine engine, Entity collider, Entity collidee) {
        if(collidee != null) {
            short type = collidee.getComponent(TypeComponent.class).type;
            switch (type){
                case PhysicsManager.COL_PLAYER:{
                    System.out.println("Bullet hit player.");
                }
            }
        }
        if(collider != null && collider.getComponent(RenderableComponent.class) != null) {
            // Bullets will always be destroyed when handling a collision?
            collider.getComponent(BodyComponent.class).body.setUserData(null); // <= is this a good solution?
            collider.remove(RenderableComponent.class);
            AshleyEntityManager.setToDestroy(collider);
        }
    }
}
