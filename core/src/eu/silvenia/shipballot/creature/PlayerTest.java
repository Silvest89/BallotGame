package eu.silvenia.shipballot.creature;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import eu.silvenia.shipballot.Mappers;
import eu.silvenia.shipballot.Updateable;

/**
 * Created by Johnnie Ho on 29-6-2015.
 */
public class PlayerTest implements Updateable, InputProcessor {
    private Entity entity;

    public enum DIRECTION{
        EAST,
        WEST
    }
    private float movementForce = 40;
    private float animationTime = 0;
    private float jumpForce = 60;

    protected Animation southStanding, westStanding, eastStanding, northStanding;
    protected Animation south, west, east, north;

    public PlayerTest(Entity entity){
        this.entity = entity;
    }

    public void update(float delta) {
        animationTime = animationTime + (160 * delta / 100);
        move();
        if(Mappers.playerData.get(entity).movingDirection != null)
            animatePlayer();
    }
    public void setLookingDirection(DIRECTION lookingDirection) {
        Mappers.playerData.get(entity).lookingDirection = lookingDirection;
    }

    public void animatePlayer(){
        switch(Mappers.playerData.get(entity).movingDirection){
            case WEST:{
                setLookingDirection(DIRECTION.WEST);
                Mappers.spriteMap.get(entity).spritesList.first().setRegion(west.getKeyFrame(animationTime));
                break;
            }
            case EAST:{
                setLookingDirection(DIRECTION.EAST);
                Mappers.spriteMap.get(entity).spritesList.first().setRegion(east.getKeyFrame(animationTime));
                break;
            }
        }
    }

    public void move(){
        Mappers.bodyMap.get(entity).body.applyForceToCenter(Mappers.playerData.get(entity).keyforce, true);
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
    public boolean keyDown(int keycode) {
        switch(keycode) {
            case Input.Keys.A: {
                movePlayer(DIRECTION.WEST);
                break;
            }
            case Input.Keys.D: {
                movePlayer(DIRECTION.EAST);
                break;
            }
            case Input.Keys.SPACE:{
                if(Mappers.playerData.get(entity).canJump) {

                    Mappers.bodyMap.get(entity).body.applyLinearImpulse(0,
                            jumpForce,
                            Mappers.bodyMap.get(entity).body.getWorldCenter().x,
                            Mappers.bodyMap.get(entity).body.getWorldCenter().y,
                            true);
                    Mappers.playerData.get(entity).canJump = false;
                }

                break;
            }
            case Input.Keys.SHIFT_LEFT:{
                if(Mappers.weaponMap.get(entity).reloadTimer >= Mappers.weaponMap.get(entity).getReloadTime())
                    Mappers.weaponMap.get(entity).canFire = true;
            }
        }
        return false;
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
        return false;
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

    public void movePlayer(DIRECTION direction){
        switch(direction){
            case WEST:{
                Mappers.playerData.get(entity).keyforce.x = -movementForce;
                Mappers.playerData.get(entity).movingDirection = DIRECTION.WEST;
                break;
            }
            case EAST:{
                Mappers.playerData.get(entity).keyforce.x = movementForce;
                Mappers.playerData.get(entity).movingDirection = DIRECTION.EAST;
                break;
            }
        }
    }

    public void stopPlayer(){
        if(Mappers.playerData.get(entity).movingDirection == null)
            return;
        Mappers.playerData.get(entity).keyforce.x = 0;
        animationTime = 0;

        switch(Mappers.playerData.get(entity).movingDirection){
            case WEST:{
                Mappers.spriteMap.get(entity).spritesList.first().setRegion(westStanding.getKeyFrame(0));
                break;
            }
            case EAST:{
                Mappers.spriteMap.get(entity).spritesList.first().setRegion(eastStanding.getKeyFrame(0));
                break;
            }
        }
        Mappers.playerData.get(entity).movingDirection = null;
    }
}
