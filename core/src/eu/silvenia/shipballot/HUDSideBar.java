package eu.silvenia.shipballot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import eu.silvenia.shipballot.creature.PlayerTest;
import eu.silvenia.shipballot.screens.Game;

/**
 * Created by Johnnie Ho on 2-7-2015.
 */
public class HUDSideBar extends Stage {

    BitmapFont font;
    //Image backgroundImage = new Image(new Texture(Gdx.files.internal("hud_background.png")));
    Sprite healthBarBackground = new Sprite(new Texture(Gdx.files.internal("hud_healthbarbg.png")));
    Sprite healthBarForeground = new Sprite(new Texture(Gdx.files.internal("hud_healthbarfg.png")));
    Sprite experienceBackground = new Sprite(new Texture(Gdx.files.internal("experiencebarbg.png")));
    Sprite experienceForeground = new Sprite(new Texture(Gdx.files.internal("experiencebarfg.png")));
    Image levelCircle = new Image(new Texture(Gdx.files.internal("hud_levelcircle.png")));
    Table table = new Table();
    PlayerTest player;

    String healthBar;

    public HUDSideBar(PlayerTest player){
        super();

        this.player = player;
        //addActor(backgroundImage);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Quicksand-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.MipMapLinearNearest;
        parameter.color = Color.WHITE;

        generator.scaleForPixelHeight(30);

        BitmapFont font12 = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose();
        this.font = font12;
        healthBarBackground.setPosition(12, getCamera().viewportHeight - 40);
        healthBarForeground.setPosition(15, getCamera().viewportHeight - 37);
        healthBarForeground.setOrigin(0, 0);

        experienceBackground.setPosition(24, getCamera().viewportHeight - 51);
        experienceForeground.setPosition(26, getCamera().viewportHeight - 49);
        experienceForeground.setOrigin(0, 0);

        font.getData().setScale(0.60f);

        healthBarForeground.setScale(player.getHealth() / (float)player.getMaxHealth(), 1f);
        experienceForeground.setScale(player.getExperience() / (float)PlayerTest.getExpForNextLv(player.getLevel() + 1), 1f);
        levelCircle.setPosition(8, getCamera().viewportHeight - 50);
        System.out.println(player.getExpForNextLv(2));
    }

    @Override
    public void act(){
        super.act();
        healthBar = Integer.toString(player.getHealth()) + "/" + Integer.toString(player.getMaxHealth());
    }

    @Override
    public void draw(){
        super.draw();

        getBatch().begin();

        healthBarBackground.draw(getBatch());
        healthBarForeground.draw(getBatch());
        experienceBackground.draw(getBatch());
        experienceForeground.draw(getBatch());
        levelCircle.draw(getBatch(), 1);

        font.draw(getBatch(), healthBar, healthBarBackground.getX() + 70f , healthBarBackground .getY() + 22);
        if(player.getLevel() < 10)
            font.draw(getBatch(), Integer.toString(player.getLevel()), 12 + 5 , levelCircle.getY() + 19);
        else
            font.draw(getBatch(), Integer.toString(player.getLevel()), 12, levelCircle.getY() + 19);

        getBatch().end();


    }

}
