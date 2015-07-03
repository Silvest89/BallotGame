package eu.silvenia.shipballot.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import eu.silvenia.shipballot.systems.Components.BitmapFontComponent;
import eu.silvenia.shipballot.systems.Components.PositionComponent;
import eu.silvenia.shipballot.systems.Components.RenderableComponent;

/**
 * Created by Johnnie Ho on 3-7-2015.
 */
public class BitmapFontRenderSystem extends IteratingSystem {
    private Batch batch;

    private ComponentMapper<BitmapFontComponent> bm = ComponentMapper.getFor(BitmapFontComponent.class);
    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);

    public BitmapFontRenderSystem(Batch batch) {
        super(Family.all(RenderableComponent.class, BitmapFontComponent.class, PositionComponent.class).get());
        this.batch = batch;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        BitmapFont bFont = bm.get(entity).bFont;
        CharSequence msg = bm.get(entity).text;
        float x = pm.get(entity).x;
        float y = pm.get(entity).y;

        bFont.draw(batch, msg, x, y);
    }
}
