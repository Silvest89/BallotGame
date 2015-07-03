package eu.silvenia.shipballot.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import eu.silvenia.shipballot.AshleyEntityManager;
import eu.silvenia.shipballot.Mappers;
import eu.silvenia.shipballot.screens.Game;
import eu.silvenia.shipballot.systems.Components.BitmapFontComponent;
import eu.silvenia.shipballot.systems.Components.DeathTimerComponent;
import eu.silvenia.shipballot.systems.Components.SpriteComponent;
import eu.silvenia.shipballot.systems.Components.TransparentComponent;

/**
 * Created by Johnnie Ho on 3-7-2015.
 */
public class DeathTimerSystem extends IteratingSystem {

    public DeathTimerSystem() {
        super(Family.all(DeathTimerComponent.class)
                .one(SpriteComponent.class, BitmapFontComponent.class).get());
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        DeathTimerComponent     deathCom = Mappers.deathTimerMap.get(entity);
        BitmapFontComponent     bFontCom = Mappers.bitmapFontMap.get(entity);
        SpriteComponent         spriteCom = Mappers.spriteMap.get(entity);
        TransparentComponent transCom = Mappers.transparencyMap.get(entity);

        long deathDelta = Game.currentTimeMillis - deathCom.createTime;

        transCom.transparency = 1 - (float) Math.pow(deathDelta / (double) deathCom.deathTime, 2.0d);

        if (bFontCom != null) {
            //bFontCom.bFont.setColor(1, 1, 1, transCom.transparency);
        }
        if (spriteCom != null) {
            spriteCom.spritesList.get(0).setAlpha(transCom.transparency);
        }

        if (deathCom.deathTime + deathCom.createTime <= Game.currentTimeMillis) {
            AshleyEntityManager.setToDestroy(entity);
        }
    }
}