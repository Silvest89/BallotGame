package eu.silvenia.shipballot.creature;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import eu.silvenia.shipballot.EntityManager;
import eu.silvenia.shipballot.GameObject;
import eu.silvenia.shipballot.projectile.Bullet;
import eu.silvenia.shipballot.screens.GameScreen;

import net.dermetfan.gdx.graphics.g2d.AnimatedBox2DSprite;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;


/**
 * Created by Johnnie Ho on 24-6-2015.
 */
public class Player extends Creature{

    World world;
    boolean isMoving;
    boolean isHolding;

    private int experience;
    private int tilePixels = 32;

    private Vector2 keyforce = new Vector2();

    private float movementForce = 100;
    private float jumpForce = 100;

    private boolean canJump;
    OrthographicCamera camera;


    public Player(GameScreen game, AnimatedSprite animatedSprite, String name, World world, OrthographicCamera camera){
        super(game, animatedSprite, name);
        this.camera = camera;
        this.world = world;

        this.experience = 0;
        this.canJump = false;

        // reusable construction objects
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();

        // a ball
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(8,8);
        bodyDef.fixedRotation = true;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1f, 1f);

        fixtureDef.shape = shape;
        fixtureDef.density = 3.0f;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 0.8f;

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setUserData(this);

        shape.dispose();

        healthBar = new HealthBar(this);
        nameBar = new NameBar(this);
        //this.play();
        EntityManager.setToUpdate(this);
    }

    public void setupAnimation(TextureAtlas playerAtlas){
        this.southStanding = new Animation(1 / 4f, playerAtlas.findRegions("southStanding"));
        this.westStanding = new Animation(1 / 4f, playerAtlas.findRegions("westStanding"));
        this.eastStanding = new Animation(1 / 4f, playerAtlas.findRegions("eastStanding"));
        this.northStanding = new Animation(1 / 4f, playerAtlas.findRegions("northStanding"));

        this.south = new Animation(1 / 4f, playerAtlas.findRegions("south"));
        this.west = new Animation(1 / 4f, playerAtlas.findRegions("west"));
        this.east = new Animation(1 / 4f, playerAtlas.findRegions("east"));
        this.north = new Animation(1 / 4f, playerAtlas.findRegions("north"));

        this.south.setPlayMode(Animation.PlayMode.LOOP);
        this.west.setPlayMode(Animation.PlayMode.LOOP);
        this.east.setPlayMode(Animation.PlayMode.LOOP);
        this.north.setPlayMode(Animation.PlayMode.LOOP);
    }

    @Override
    public void draw(Batch batch){
        super.draw(batch);
    }

    @Override
    public void update(float delta){
        super.update(delta);
        body.applyForceToCenter(keyforce, true);

        setAnimationTime(getAnimationTime() + (getSpeed() * delta / 100));
        if(movingDirection != null)
            animatePlayer();
    }

    public void animatePlayer(){
        switch(movingDirection){
            case WEST:{
                setLookingDirection(DIRECTION.WEST);
                setRegion(west.getKeyFrame(getAnimationTime()));
                break;
            }
            case EAST:{
                setLookingDirection(DIRECTION.EAST);
                setRegion(east.getKeyFrame(getAnimationTime()));
                break;
            }
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        switch(keycode){
            case Input.Keys.A: {
                movePlayer(DIRECTION.WEST);
                break;
            }
            case Input.Keys.D: {
                movePlayer(DIRECTION.EAST);
                break;
            }
            case Input.Keys.SPACE:{
                if(canJump) {
                    body.applyLinearImpulse(0, jumpForce, body.getWorldCenter().x, body.getWorldCenter().y, true);
                    canJump = false;
                }

                break;
            }
            case Input.Keys.SHIFT_LEFT:{
                Bullet bullet = new Bullet(new Texture("bullet.png"), this, world);
                EntityManager.add(bullet);
                break;
            }
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode){
            case Input.Keys.A:
            case Input.Keys.D:{
                stopPlayer();
                break;
            }
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 testPoint = new Vector3();
        camera.unproject(testPoint.set(screenX, screenY, 0));
        if(getBody().getPosition().x < testPoint.x)
            movePlayer(DIRECTION.EAST);
        if(getBody().getPosition().x > testPoint.x)
            movePlayer(DIRECTION.WEST);

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        stopPlayer();
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void handleCollision(Body body){
        if(game.parser.getBodies().get("ground") == body) {
            canJump = true;
        }
    }

    public void movePlayer(DIRECTION direction){
        switch(direction){
            case WEST:{
                keyforce.x = -movementForce;
                movingDirection = DIRECTION.WEST;
                break;
            }
            case EAST:{
                keyforce.x = movementForce;
                movingDirection = DIRECTION.EAST;
                break;
            }
        }
    }
    public void stopPlayer(){
        if(movingDirection == null)
            return;
        keyforce.x = 0;
        setAnimationTime(0);

        switch(movingDirection){
            case WEST:{
                setRegion(westStanding.getKeyFrame(0));
                break;
            }
            case EAST:{
                setRegion(eastStanding.getKeyFrame(0));
                break;
            }
        }
        movingDirection = null;
    }
}
