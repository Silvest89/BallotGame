package eu.silvenia.shipballot.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Sprite;
import eu.silvenia.shipballot.Mappers;
import eu.silvenia.shipballot.systems.Components.BodyComponent;
import eu.silvenia.shipballot.systems.Components.PositionComponent;
import eu.silvenia.shipballot.systems.Components.SpriteComponent;

/**
 * Created by Johnnie Ho on 29-6-2015.
 */
public class PositionSystem extends IteratingSystem {

    public PositionSystem() {
        super(Family.all(PositionComponent.class).get());
    }

    public void processEntity(Entity entity, float deltaTime) {
        PositionComponent positionCom  = Mappers.positionMap.get(entity);
        SpriteComponent spriteCom    = Mappers.spriteMap.get(entity);
        BodyComponent     bodyCom      = Mappers.bodyMap.get(entity);

        // Position priority: Body => PositionComponent => Sprites  (highest to lowest)
        if (bodyCom != null) {
            positionCom.x = bodyCom.body.getPosition().x;
            positionCom.y = bodyCom.body.getPosition().y;
        }

        if (spriteCom != null) {
            Sprite sprite = spriteCom.sprite;
            sprite.setPosition(positionCom.x - sprite.getWidth() / 2, positionCom.y - sprite.getHeight() / 2);
        }
    }
}