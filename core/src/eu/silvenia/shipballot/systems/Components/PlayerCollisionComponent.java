package eu.silvenia.shipballot.systems.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import eu.silvenia.shipballot.Collidable;
import eu.silvenia.shipballot.Mappers;
import eu.silvenia.shipballot.PhysicsManager;

/**
 * Created by Johnnie Ho on 30-6-2015.
 */
public class PlayerCollisionComponent extends Component implements Collidable {


    @Override
    public void handleCollision(Engine engine, Entity collider, Entity collidee) {
        if(collider == null || collidee != null){
            if(Mappers.playerData.has(collidee))
                collidee.getComponent(PlayerDataComponent.class).canJump = true;
        }
        else if(collider != null || collidee == null){
            if(Mappers.playerData.has(collider))
                collider.getComponent(PlayerDataComponent.class).canJump = true;
        }
        else {
            short type = collidee.getComponent(TypeComponent.class).type;
        }
    }
}
