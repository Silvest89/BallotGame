package eu.silvenia.shipballot.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import eu.silvenia.shipballot.AshleyEntityManager;
import eu.silvenia.shipballot.Mappers;
import eu.silvenia.shipballot.systems.Components.*;

/**
 * Created by Johnnie Ho on 30-6-2015.
 */
public class HealthBarSystem extends IteratingSystem {

    public HealthBarSystem() {
        super(Family.all(HealthBarComponent.class, AttachedComponent.class, PositionComponent.class).get());
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PositionComponent posCom = Mappers.positionMap.get(entity);
        AttachedComponent attCom = Mappers.attachedMap.get(entity);
        SpriteComponent spriteCom = Mappers.spriteMap.get(entity);

        BodyComponent attPosCom = Mappers.bodyMap.get(attCom.attachedTo);
        SpriteComponent attSprite = Mappers.spriteMap.get(attCom.attachedTo);
        PlayerDataComponent dataCom = Mappers.playerData.get(attCom.attachedTo);

        spriteCom.spritesList.get(1).setScale(dataCom.health / (float) dataCom.maxHealth, 1);

        posCom.x = attPosCom.body.getPosition().x;
        posCom.y = attPosCom.body.getPosition().y + 1.2f; // 20 looks better with properly sized sprites

        if (spriteCom.spritesList.get(1).getScaleX() <= 0) {
            entity.remove(RenderableComponent.class);
            AshleyEntityManager.setToDestroy(entity);
        }
    }
}