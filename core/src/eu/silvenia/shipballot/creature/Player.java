package eu.silvenia.shipballot.creature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import eu.silvenia.shipballot.PhysicsManager;
import eu.silvenia.shipballot.screens.GameScreen;
import eu.silvenia.shipballot.world.TileObjects;
import net.dermetfan.gdx.graphics.g2d.AnimatedBox2DSprite;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;
import com.badlogic.gdx.math.Vector2;

import javax.lang.model.element.Name;

/**
 * Created by Johnnie Ho on 24-6-2015.
 */
public class Player extends Creature implements ContactFilter, ContactListener{

    Body body;
    //Box2DSprite sprite;

    boolean isMoving;
    boolean isHolding;

    private int experience;
    private int tilePixels = 32;

    private Vector2 keyforce = new Vector2();

    private float movementForce = 100;
    private float jumpForce = 100;

    private boolean canJump;
    HealthBar healthBar;
    NameBar nameBar;
    OrthographicCamera camera;

    public Player(GameScreen game, TextureAtlas playerAtlas, String name, World world, OrthographicCamera camera){
        super(game, name);
    this.camera = camera;
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

        this.experience = 0;
        this.canJump = false;

        AnimatedSprite animatedSprite = new AnimatedSprite(this.southStanding);
        animatedBox2DSprite = new AnimatedBox2DSprite(animatedSprite);
        //sprite = new Box2DSprite(new Animation(1 / 4f, playerAtlas.findRegions("southStanding")).getKeyFrame(0));

        //nameBar = new NameBar(this);

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
        body.setUserData(animatedBox2DSprite);
        animatedBox2DSprite.play();
        healthBar = new HealthBar(this);
        nameBar = new NameBar(this);
    }

    public void draw(Batch batch){
        healthBar.update();
        healthBar.render(batch);
        nameBar.draw(batch);
    }

    public void update(float delta){
        animatedBox2DSprite.update(delta);
        body.applyForceToCenter(keyforce, true);
        setAnimationTime(getAnimationTime() + (100 * delta / 100));
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

    public Body getBody() {
        return body;
    }

    @Override
    public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
        if(fixtureA.getBody().getType() == BodyDef.BodyType.StaticBody)
            return true;

        return false;
    }

    @Override
    public void beginContact(Contact contact) {
        if(contact.getFixtureA().getBody().getType() == BodyDef.BodyType.StaticBody) {
            canJump = true;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public void movePlayer(DIRECTION direction){
        switch(direction){
            case WEST:{
                keyforce.x = -movementForce;
                animatedBox2DSprite.setAnimation(west);
                movingDirection = DIRECTION.WEST;
                break;
            }
            case EAST:{
                keyforce.x = movementForce;
                animatedBox2DSprite.setAnimation(east);
                movingDirection = DIRECTION.EAST;
                break;
            }
        }
    }
    public void stopPlayer(){
        keyforce.x = 0;
        switch(movingDirection){
            case WEST:{
                animatedBox2DSprite.setAnimation(westStanding);
                break;
            }
            case EAST:{
                animatedBox2DSprite.setAnimation(eastStanding);
                break;
            }
        }
    }
}
