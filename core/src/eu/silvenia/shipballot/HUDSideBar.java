package eu.silvenia.shipballot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import eu.silvenia.shipballot.creature.Player;
import eu.silvenia.shipballot.weapons.Weapon;

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
    Image weaponSlot = new Image(new Texture(Gdx.files.internal("WeaponSlot.png")));
    Sprite weaponSprite;
    Table table = new Table();
    Player player;

    String healthBar;

    public HUDSideBar(Player player){
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
        healthBarBackground.setPosition(112, getCamera().viewportHeight - 45);
        healthBarForeground.setPosition(115, getCamera().viewportHeight - 42);
        healthBarForeground.setOrigin(0, 0);

        experienceBackground.setPosition(135, getCamera().viewportHeight - 56);
        experienceForeground.setPosition(137, getCamera().viewportHeight - 54);
        experienceForeground.setOrigin(0, 0);

        font.getData().setScale(0.60f);

        levelCircle.setPosition(123, getCamera().viewportHeight - 55);
        weaponSlot.setPosition(5, getCamera().viewportHeight - 140);
        updateWeapon(player.getWeaponType());

    }

    @Override
    public void act(){
        super.act();
        healthBar = Integer.toString(player.getHealth()) + "/" + Integer.toString(player.getMaxHealth());

        healthBarForeground.setScale(player.getHealth() / (float)player.getMaxHealth(), 1f);
        experienceForeground.setScale(player.getExperience() / (float) Player.getExpForNextLv(player.getLevel() + 1), 1f);
    }

    @Override
    public void draw(){
        super.draw();

        getBatch().begin();

        healthBarBackground.draw(getBatch());
        healthBarForeground.draw(getBatch());
        experienceBackground.draw(getBatch());
        experienceForeground.draw(getBatch());
        weaponSlot.draw(getBatch(), 1);
        weaponSprite.draw(getBatch());
        levelCircle.draw(getBatch(), 1);

        font.draw(getBatch(), healthBar, healthBarBackground.getX() + 70f , healthBarBackground .getY() + 22);
        if(player.getLevel() < 10)
            font.draw(getBatch(), Integer.toString(player.getLevel()), levelCircle.getX() + 9 , levelCircle.getY() + 19);
        else
            font.draw(getBatch(), Integer.toString(player.getLevel()), levelCircle.getX() + 4 , levelCircle.getY() + 19);

        getBatch().end();
    }

    public void updateWeapon(Weapon.WeaponType weaponType){
        switch(weaponType){
            case PISTOL:{
                weaponSprite = new Sprite(new Texture(Gdx.files.internal("weapons/pistol.png")));
                break;
            }
            case SHOTGUN:{
                weaponSprite = new Sprite(new Texture(Gdx.files.internal("weapons/shotgun.png")));
                break;
            }
            case RIFLE:{
                weaponSprite = new Sprite(new Texture(Gdx.files.internal("weapons/assault_rifle.png")));
                break;
            }
        }
        weaponSprite.setPosition(weaponSlot.getX() + 20, weaponSlot.getY() + 20);
    }

}
