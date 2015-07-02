package eu.silvenia.shipballot.creature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.physics.box2d.*;
import eu.silvenia.shipballot.*;
import eu.silvenia.shipballot.weapons.Bullet;
import eu.silvenia.shipballot.screens.Game;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

/**
 * Created by Johnnie Ho on 25-6-2015.
 */
abstract public class Creature extends Box2DSprite implements GameObject, InputProcessor {

    private World world;
    protected Body body;

    private boolean canJump;
    private int experience;

    private float movementForce = 30;
    private float jumpForce = 20;
    private float speed;
    private float animationTime;

    protected Game game;

    protected Animation southStanding, westStanding, eastStanding, northStanding;
    protected Animation south, west, east, north;

    public enum DIRECTION{
        EAST,
        WEST
    }
    private String name;

    private int level;
    private int health;
    private int maxHealth;

    HealthBar healthBar;
    NameBar nameBar;

    DIRECTION movingDirection = null;
    DIRECTION lookingDirection = null;

    public Creature(Game game, AnimatedSprite animatedSprite, String name, World world){
        super(animatedSprite);

        this.world = world;
        this.speed = (80 * 2);
        this.animationTime = 0;
        this.game = game;
        this.name = name;

        this.experience = 25;
        this.canJump = false;

        this.health = 100;
        this.maxHealth = 100;

        healthBar = new HealthBar(this);
        nameBar = new NameBar(this);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getExperience() {
        return experience;
    }

    public void addExperience(int experience) {
        this.experience += experience;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public DIRECTION getLookingDirection() {
        return lookingDirection;
    }

    public float getMovementForce() {
        return movementForce;
    }

    public void setMovementForce(float movementForce) {
        this.movementForce = movementForce;
    }

    public float getJumpForce() {
        return jumpForce;
    }

    public void setJumpForce(float jumpForce) {
        this.jumpForce = jumpForce;
    }

    public boolean canJump() {
        return canJump;
    }

    public void setCanJump(boolean canJump) {
        this.canJump = canJump;
    }

    public World getWorld() {
        return world;
    }

    public void setLookingDirection(DIRECTION lookingDirection) {
        this.lookingDirection = lookingDirection;
    }

    public Body getBody() {
        return body;
    }

    public void hit(Bullet bullet){
        int damage = (int)((Math.random()*15) + bullet.getDamage());
        health -= damage;
        new DamageText(Integer.toString(damage), getBody().getPosition().x, getBody().getPosition().y);
        if(health <= 0) {
            health = 0;
            if(bullet.getOwner() != null) {
                new DamageText(Integer.toString(experience), bullet.getOwner().getBody().getPosition().x, bullet.getOwner().getBody().getPosition().y);
                bullet.getOwner().addExperience(getExperience());

            }
            EntityManager.setToDestroy(this);
            Game.playerList.remove(this);
        }
    }


    @Override public void draw(Batch batch){
        healthBar.render(batch);
        nameBar.draw(batch);
    }

    public void update(float delta){
        healthBar.update();
    }

    protected class HealthBar{
        private Sprite healthBarBackground;
        private Sprite healthBarForeground;

        private Creature owner;

        public HealthBar(Creature owner){
            this.owner = owner;
            healthBarBackground = new Box2DSprite(new Texture("healthbarbg.png"));
            healthBarForeground = new Box2DSprite(new Texture("healthbarfg.png"));

            healthBarBackground.setSize(2 , 0.3f);
            healthBarForeground.setSize(2, 0.3f);
            healthBarForeground.setOrigin(0, 0);

        }

        public void update(){
            healthBarBackground.setPosition(owner.getBody().getPosition().x - 1, owner.getBody().getPosition().y + 1.1f);
            healthBarForeground.setPosition(owner.getBody().getPosition().x - 1, owner.getBody().getPosition().y + 1.1f);
            healthBarForeground.setScale(owner.getHealth() / (float)owner.getMaxHealth(), 1f);
        }

        public void render(Batch batch){
            healthBarBackground.draw(batch);
            healthBarForeground.draw(batch);
        }
    }

    protected class NameBar{
        Creature owner;
        BitmapFont font;

        public NameBar(Creature owner){
            this.owner = owner;

            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Quicksand-Bold.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = (int)(12 * Game.SCALE);
            parameter.minFilter = Texture.TextureFilter.Linear;
            parameter.magFilter = Texture.TextureFilter.MipMapLinearNearest;
            parameter.color = Color.WHITE;

            generator.scaleForPixelHeight((int)(12 * Game.SCALE));

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
