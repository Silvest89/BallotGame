package eu.silvenia.shipballot.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import eu.silvenia.shipballot.Mappers;
import eu.silvenia.shipballot.systems.Components.*;

/**
 * Created by Johnnie Ho on 29-6-2015.
 */
public class RenderSystem extends SortedIteratingSystem {
    private Batch batch;

    public RenderSystem(Batch batch){
        super(Family.all(RenderableComponent.class, SpriteComponent.class, PositionComponent.class).get(), new ZComparator());
        this.batch = batch;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        for (Sprite sprite : Mappers.spriteMap.get(entity).spritesList) {
            sprite.draw(batch);
        }
    }
}
