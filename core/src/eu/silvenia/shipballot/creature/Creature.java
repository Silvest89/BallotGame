package eu.silvenia.shipballot.creature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import eu.silvenia.shipballot.PhysicsManager;
import eu.silvenia.shipballot.screens.GameScreen;
import eu.silvenia.shipballot.world.TileObjects;
import net.dermetfan.gdx.graphics.g2d.AnimatedBox2DSprite;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

/**
 * Created by Johnnie Ho on 25-6-2015.
 */
abstract public class Creature implements InputProcessor {
    private float speed;
    private float animationTime;

    protected GameScreen game;

    protected Animation southStanding, westStanding, eastStanding, northStanding;
    protected Animation south, west, east, north;

    protected Vector2 targetPosition = new Vector2();

    public AnimatedBox2DSprite animatedBox2DSprite;

    public enum DIRECTION{
        NORTH,
        EAST,
        SOUTH,
        WEST
    }

    float oldX, oldY;

    private String name;

    private int health;
    private int maxHealth;

    DIRECTION movingDirection = null;

    public Creature(GameScreen game, String name){
        this.speed = (80 * 2);
        this.animationTime = 0;
        this.game = game;
        //this.healthBar = new HealthBar(this);
        this.name = name;

        this.health = 100;
        this.maxHealth = 100;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getAnimationTime() {
        return animationTime;
    }

    public void setAnimationTime(float animationTime) {
        this.animationTime = animationTime;
    }

    abstract public void update(float delta);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected class HealthBar{
        private Sprite healthBarBackground;
        private Sprite healthBarForeground;

        private Player owner;

        public HealthBar(Player owner){
            this.owner = owner;
            healthBarBackground = new Box2DSprite(new Texture("healthbarbg.png"));
            healthBarForeground = new Box2DSprite(new Texture("healthbarfg.png"));
            //healthBarBackground
            healthBarBackground.setSize(2 , 0.3f);
            healthBarForeground.setSize(2, 0.3f);
            healthBarForeground.setOrigin(0, 0);

            update();
        }
        public void update(){
            healthBarBackground.setPosition(owner.getBody().getPosition().x - 1, owner.getBody().getPosition().y + 1.1f);
            healthBarForeground.setPosition(owner.getBody().getPosition().x - 1, owner.getBody().getPosition().y + 1.1f);
            //healthBarForeground.setScale(owner.health / (float)owner.maxHealth, 1f);
        }

        public void render(Batch batch){
            healthBarBackground.draw(batch);
           healthBarForeground.draw(batch);
        }
    }

    protected class NameBar{
        Player owner;
        BitmapFont font;

        public NameBar(Player owner){
            this.owner = owner;

            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Quicksand-Bold.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = (int)(12 * GameScreen.SCALE);
            parameter.minFilter = Texture.TextureFilter.Linear;
            parameter.magFilter = Texture.TextureFilter.MipMapLinearNearest;
            parameter.color = Color.WHITE;

            generator.scaleForPixelHeight((int)(12 * GameScreen.SCALE));

            BitmapFont font12 = generator.generateFont(parameter); // font size 12 pixels
            generator.dispose();
            this.font = font12;

            font.getData().setScale(0.05f);

        }

        public void draw(Batch batch){
            font.draw(batch, owner.getName(), owner.getBody().getPosition().x - (0.8f + (0.05f * owner.getName().length())), owner.getBody().getPosition().y + 1.9f);
        }
    }
}
