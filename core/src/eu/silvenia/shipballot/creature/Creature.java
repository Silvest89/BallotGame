package eu.silvenia.shipballot.creature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import eu.silvenia.shipballot.screens.GameScreen;

/**
 * Created by Johnnie Ho on 24-6-2015.
 */
public class Creature extends Sprite implements InputProcessor {

    private GameScreen game;

    private TiledMapTileLayer collisionLayer;

    /* Player movement velocity */
    private Vector2 velocity = new Vector2();

    private float speed = 60 * 2, animationTime = 0;

    private Animation southStanding, westStanding, eastStanding, northStanding;
    private Animation south, west, east, north;
    private boolean canMove = true, isPlayerMovingEast = false, isPlayerMovingWest = false, isPlayerMovingNorth = false, isPlayerMovingSouth = false;
    private float movementTimer = 0;
    private int targetX = 0;

    public Creature(GameScreen game, TextureAtlas playerAtlas, TiledMapTileLayer collisionLayer){
        super((new Animation(1 / 4f, playerAtlas.findRegions("southStanding"))).getKeyFrame(0));
        this.game = game;
        this.collisionLayer = collisionLayer;

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
    public void draw(Batch spriteBatch){
        update(Gdx.graphics.getDeltaTime());
        super.draw(spriteBatch);
    }

    public void update(float delta){

        boolean collision = false;
        // collision with x-borders
        if(getX()<0){
            setX(0);
            resetMovement();
            return;
        }
        else if(getX() > game.getTileWidth() * game.getMapWidth() - getWidth()){
            setX(game.getTileWidth() * game.getMapWidth() - getWidth());
            resetMovement();
            return;
        }
        // collision with y-borders
        if(getY()<0){
            setY(0);
            resetMovement();
            return;
        }
        else if(getY() > game.getTileHeight() * game.getMapHeight() - getHeight()){
            setY(-getHeight() + game.getTileHeight() * game.getMapHeight());
            resetMovement();
            return;
        }

        //targetX = (int)(getX() + 1);
        //setX((int)(getX() + 1));

            if (isPlayerMovingWest) {
                movePlayer(2, delta);
            }
            else if (isPlayerMovingEast)
                movePlayer(3, delta);
            else if (isPlayerMovingNorth)
                movePlayer(0, delta);
            else if (isPlayerMovingSouth)
                movePlayer(1, delta);

        //System.out.print("score: " + targetX + "\n" );

        //setY(getY() + (velocity.y * delta));

        /* Update animation */
        animationTime += getSpeed() * delta / 100;
        if(velocity.x < 0)
            setRegion(west.getKeyFrame(animationTime));
        else if(velocity.x > 0)
            setRegion(east.getKeyFrame(animationTime));
        if(velocity.y < 0)
            setRegion(south.getKeyFrame(animationTime));
        else if(velocity.y > 0)
            setRegion(north.getKeyFrame(animationTime));
    }

    private boolean isCellBlocked(float x, float y){
        TiledMapTileLayer.Cell cell = collisionLayer.getCell((int)(x / collisionLayer.getTileWidth()), (int)(y / collisionLayer.getTileHeight()));
        return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey("blocked");
    }
    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public boolean keyDown(int keycode) {
            switch (keycode) {
                case Input.Keys.W: {
                    animationTime = 0;
                    velocity.y = speed;
                    targetX = (int) (getY() + 32);
                    isPlayerMovingNorth = true;
                    canMove = false;
                    break;
                }
                case Input.Keys.A: {
                    animationTime = 0;
                    velocity.x = -speed;
                    targetX = (int) (getX() - 32);
                    isPlayerMovingWest = true;
                    canMove = false;
                    break;
                }
                case Input.Keys.S: {
                    animationTime = 0;
                    velocity.y = -speed;
                    targetX = (int) (getY() - 32);
                    isPlayerMovingSouth = true;
                    canMove = false;
                    break;
                }
                case Input.Keys.D: {
                    animationTime = 0;
                    velocity.x = speed;
                    targetX = (int) (getX() + 32);
                    isPlayerMovingEast = true;
                    canMove = false;
                    break;
                }
            }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch(keycode){
            case Input.Keys.A:
            {
                canMove = true;
                break;
            }
            case Input.Keys.D:
            {
                canMove = true;
                break;
            }
            case Input.Keys.W:
            {
                canMove = true;
                break;
            }
            case Input.Keys.S:
            {
                canMove = true;
                break;
            }
        }
        return true;
    }

    private void movePlayer(int direction, float delta) {
        boolean collision = false;
        float oldX = getX(); float oldY = getY();
        switch(direction){
            case 0: {
                setY(getY() + getSpeed() / 100 );

                collision = isCellBlocked((getX() + 1), getY() + (getHeight()));

                if(!collision)
                    collision = isCellBlocked((getX() + 1) + (getWidth() - 1) / 2, getY() + (getHeight()));

                if(!collision)
                    collision = isCellBlocked((getX() + 1) + (getWidth() - 1), getY() + (getHeight()));

                if(collision) {
                    setY(oldY);
                    targetX = 0;
                    resetMovement();
                }
                if(getY() > targetX && targetX != 0) {
                    setY(targetX);
                    targetX = 0;
                    if(isPlayerMovingNorth && !canMove) {
                        targetX = (int) (getY() + 32);
                    }
                    else {
                        resetMovement();
                        setRegion(northStanding.getKeyFrame(0));
                    }
                }
                break;
            }
            case 1: {
                setY(getY() - getSpeed() / 100 );

                collision = isCellBlocked((getX() + 1), getY());

                if(!collision)
                    collision = isCellBlocked((getX() + 1) + (getWidth() -1 ) / 2, getY());

                if(!collision)
                    collision = isCellBlocked((getX() + 1) + (getWidth() -1 ), getY());


                if(collision) {
                    setY(oldY);
                    targetX = 0;
                    resetMovement();
                }
                if(getY() < targetX) {
                    setY(targetX);
                    targetX = 0;
                    if(isPlayerMovingSouth && !canMove) {
                        targetX = (int) (getY() - 32);
                    }
                    else {
                        resetMovement();
                        setRegion(southStanding.getKeyFrame(0));
                    }
                }
                break;
            }
            case 2: {
                setX(getX() - getSpeed() / 100 );
                collision = isCellBlocked(getX(), getY() + (getHeight() - 5));

                if(!collision)
                    collision = isCellBlocked(getX(), getY() + (getHeight() - 5) / 2);

                if(!collision)
                    collision = isCellBlocked(getX(), getY());
                if(collision) {
                    setX(oldX);
                    targetX = 0;
                    resetMovement();
                }
                if(getX() < targetX && !collision) {
                    setX(targetX);
                    targetX = 0;
                    if(isPlayerMovingWest && !canMove) {
                        targetX = (int) (getX() - 32);
                    }
                    else {
                        resetMovement();
                        setRegion(westStanding.getKeyFrame(0));
                    }
                }
                break;
            }
            case 3: {
                setX(getX() + (getSpeed() / 100));

                collision = isCellBlocked(getX() + getWidth(), getY() + (getHeight() - 5));

                if(!collision)
                    collision = isCellBlocked(getX() + getWidth(), getY() + (getHeight() - 5) / 2);

                if(!collision)
                    collision = isCellBlocked(getX() + getWidth(), getY());

                if(collision) {
                    setX(oldX);
                    targetX = 0;
                    resetMovement();
                }
                if(getX() > targetX && !collision) {
                    setX(targetX);
                    targetX = 0;
                    if(isPlayerMovingEast && !canMove) {
                        targetX = (int) (getX() + 32);
                    }
                    else {
                        resetMovement();
                        setRegion(eastStanding.getKeyFrame(0));
                    }
                }
                break;
            }
        }
    }
    public void resetMovement()
    {
        targetX = 0;
        isPlayerMovingNorth = false;
        isPlayerMovingSouth = false;
        isPlayerMovingEast = false;
        isPlayerMovingWest = false;
        canMove = true;
        animationTime = 0;
        velocity.x = 0;
        velocity.y = 0;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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

    public TiledMapTileLayer getCollisionLayer() {
        return collisionLayer;
    }

    public void setCollisionLayer(TiledMapTileLayer collisionLayer) {
        this.collisionLayer = collisionLayer;
    }
}
