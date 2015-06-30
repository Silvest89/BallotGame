package eu.silvenia.shipballot.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import eu.silvenia.shipballot.systems.Components.PositionComponent;
import eu.silvenia.shipballot.systems.Components.VelocityComponent;

/**
 * Created by Johnnie Ho on 29-6-2015.
 */
public class MovementSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    public MovementSystem(){

    }

    public void addedToEngine(Engine engine){
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class, VelocityComponent.class).get());
    }

    public void update(float deltaTime){
        /*for(int i = 0; i < entities.size(); i++){
            Entity entity = entities.get(i);
            PositionComponent position = positionComponent.get(entity);
            VelocityComponent velocity = velocityComponent.get(entity);
        }*/
    }
}
