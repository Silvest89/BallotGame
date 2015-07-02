package eu.silvenia.shipballot.creature;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import eu.silvenia.shipballot.EntityManager;
import eu.silvenia.shipballot.Updateable;
import eu.silvenia.shipballot.screens.Game;

import eu.silvenia.shipballot.weapons.Weapon;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;


/**
 * Created by Johnnie Ho on 24-6-2015.
 */
public class Player extends Creature implements Updateable{

    private Vector2 keyforce = new Vector2();

    OrthographicCamera camera;

    Weapon weapon;

    public Player(Game game, AnimatedSprite animatedSprite, String name, World world, OrthographicCamera camera){
        super(game, animatedSprite, name, world);
        this.camera = camera;

        weapon = new Weapon(this, Weapon.WeaponType.SHOTGUN);

        // reusable construction objects
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();

        // a ball
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(8,4);
        bodyDef.fixedRotation = true;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1f, 1f);

        fixtureDef.shape = shape;
        fixtureDef.density = 2.0f;
        fixtureDef.restitution = 0f;

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setUserData(this);

        shape.dispose();

        EntityManager.setToUpdate(this);

    }

    public void setupAnimation(TextureAtlas playerAtlas){
        this.southStanding = new Animation(1 / 4f, playerAtlas.findRegions("southStanding"));
        this.westStanding = new Animation(1 / 4f, playerAtlas.findRegions("westStanding"));
        this.eastStanding = new Animation(1 / 4f, playerAtlas.findRegions("eastStanding"));

        this.south = new Animation(1 / 4f, playerAtlas.findRegions("south"));
        this.west = new Animation(1 / 4f, playerAtlas.findRegions("west"));
        this.east = new Animation(1 / 4f, playerAtlas.findRegions("east"));

        this.south.setPlayMode(Animation.PlayMode.LOOP);
        this.west.setPlayMode(Animation.PlayMode.LOOP);
        this.east.setPlayMode(Animation.PlayMode.LOOP);
    }

    @Override
    public void draw(Batch batch){
        super.draw(batch);
    }

    @Override
    public void update(float delta){
        super.update(delta);
        weapon.update();
        getBody().applyForceToCenter(keyforce, true);

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
                if(canJump()) {
                    getBody().applyLinearImpulse(0, getJumpForce(), getBody().getWorldCenter().x, getBody().getWorldCenter().y, true);
                    setCanJump(false);
                }

                break;
            }
            case Input.Keys.SHIFT_LEFT:{
                weapon.fire();
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
            setCanJump(true);
        }
        if(body.getUserData() != null){
            if(body.getUserData() instanceof Creature)
                setCanJump(true);
        }
    }

    public void movePlayer(DIRECTION direction){
        switch(direction){
            case WEST:{
                keyforce.x = -getMovementForce();
                movingDirection = DIRECTION.WEST;
                break;
            }
            case EAST:{
                keyforce.x = getMovementForce();
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
