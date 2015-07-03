package eu.silvenia.shipballot.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import eu.silvenia.shipballot.Mappers;
import eu.silvenia.shipballot.systems.Components.FauxGravityComponent;
import eu.silvenia.shipballot.systems.Components.PositionComponent;
import eu.silvenia.shipballot.systems.Components.VelocityComponent;

/**
 * Created by Johnnie Ho on 3-7-2015.
 */
public class FauxGravitySystem extends IteratingSystem {

    public FauxGravitySystem() {
        super(Family.all(PositionComponent.class, FauxGravityComponent.class, VelocityComponent.class).get());
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PositionComponent positionCom   = Mappers.positionMap.get(entity);
        VelocityComponent velocityCom   = Mappers.velocityMap.get(entity);
        float             gravity       = Mappers.fauxGravityMap.get(entity).gravity;

        velocityCom.y -= gravity;

        positionCom.x += velocityCom.x;
        positionCom.y += velocityCom.y;
    }
}
