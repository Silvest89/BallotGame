package eu.silvenia.shipballot.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import eu.silvenia.shipballot.AshleyEntityManager;
import eu.silvenia.shipballot.Mappers;
import eu.silvenia.shipballot.screens.Game;
import eu.silvenia.shipballot.systems.Components.*;

/**
 * Created by Johnnie Ho on 30-6-2015.
 */
public class NameBarSystem extends IteratingSystem {

    private BitmapFont font;
    private Batch batch;

    public NameBarSystem(Batch batch) {
        super(Family.all(NameBarComponent.class, AttachedComponent.class, PositionComponent.class).get());

        this.batch = batch;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Quicksand-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.MipMapLinearNearest;
        parameter.color = Color.WHITE;

        generator.scaleForPixelHeight((int)(30 * Game.SCALE));

        BitmapFont font12 = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose();
        this.font = font12;

        font.getData().setScale(0.02f);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent posCom = Mappers.positionMap.get(entity);
        AttachedComponent attCom = Mappers.attachedMap.get(entity);
        SpriteComponent spriteCom = Mappers.spriteMap.get(entity);

        BodyComponent attPosCom = Mappers.bodyMap.get(attCom.attachedTo);
        SpriteComponent attSprite = Mappers.spriteMap.get(attCom.attachedTo);
        PlayerDataComponent dataCom = Mappers.playerData.get(attCom.attachedTo);

        font.draw(batch, dataCom.name, attPosCom.body.getPosition().x - (0.8f + (0.05f * dataCom.name.length())), attPosCom.body.getPosition().y + 1.9f);
        if(dataCom.health <= 0) {
            //entity.remove(NameBarComponent.class);
            AshleyEntityManager.setToDestroy(entity);
        }
        else
            font.draw(batch, dataCom.name, attPosCom.body.getPosition().x - (0.8f + (0.05f * dataCom.name.length())), attPosCom.body.getPosition().y + 1.9f);
    }
}