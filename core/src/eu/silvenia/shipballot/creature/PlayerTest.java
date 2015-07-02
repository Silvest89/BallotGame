package eu.silvenia.shipballot.creature;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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
    private float animationTime = 0;

    protected Animation southStanding, westStanding, eastStanding, northStanding;
    protected Animation south, west, east, north;

    public PlayerTest(Entity entity){
        this.entity = entity;
    }

    public void update(float delta) {
        animationTime = animationTime + (160 * delta / 100);
        move();
        if(getMovingDirection() != null)
            animatePlayer();
    }

    public void animatePlayer(){
        switch(getMovingDirection()){
            case WEST:{
                setLookDirection(DIRECTION.WEST);
                getSprite().setRegion(west.getKeyFrame(animationTime));
                break;
            }
            case EAST:{
                setLookDirection(DIRECTION.EAST);
                getSprite().setRegion(east.getKeyFrame(animationTime));
                break;
            }
        }
    }

    public void move(){
        getBody().applyForceToCenter(getKeyForce(), true);
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
                if(canJump()) {

                    getBody().applyLinearImpulse(0,
                            getJumpForce(),
                            getBody().getWorldCenter().x,
                            getBody().getWorldCenter().y,
                            true);
                    setCanJump(false);
                }

                break;
            }
            case Input.Keys.SHIFT_LEFT:{
                if(getReloadTimer() >= getReloadTime())
                    setCanFire(true);
                break;
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
                getKeyForce().x = -getMovementForce();
                setMovingDirection(DIRECTION.WEST);
                break;
            }
            case EAST:{
                getKeyForce().x = getMovementForce();
                setMovingDirection(DIRECTION.EAST);
                break;
            }
        }
    }

    public void stopPlayer(){
        if(getMovingDirection() == null)
            return;
        setKeyForce(0, 0);
        animationTime = 0;

        switch(Mappers.playerData.get(entity).movingDirection){
            case WEST:{
                getSprite().setRegion(westStanding.getKeyFrame(0));
                break;
            }
            case EAST:{
                getSprite().setRegion(eastStanding.getKeyFrame(0));
                break;
            }
        }
        Mappers.playerData.get(entity).movingDirection = null;
    }

    public int getLevel(){
        return Mappers.playerData.get(entity).level;
    }
    public Vector2 getKeyForce(){
        return Mappers.playerData.get(entity).keyforce;
    }
    public void setKeyForce(float x, float y){
        Mappers.playerData.get(entity).keyforce = new Vector2(x, y);
    }

    public boolean canJump(){
        return Mappers.playerData.get(entity).canJump;
    }
    public void setCanJump(boolean canJump){
        Mappers.playerData.get(entity).canJump = canJump;
    }

    public Body getBody(){
        return Mappers.bodyMap.get(entity).body;
    }
    public int getHealth(){
        return Mappers.playerData.get(entity).health;
    }
    public int getMaxHealth(){
        return Mappers.playerData.get(entity).maxHealth;
    }

    public int getJumpForce(){
        return Mappers.playerData.get(entity).jumpForce;
    }

    public int getMovementForce(){
        return Mappers.playerData.get(entity).movementForce;
    }

    public DIRECTION getMovingDirection(){
        return Mappers.playerData.get(entity).movingDirection;
    }
    public void setMovingDirection(DIRECTION direction){
        Mappers.playerData.get(entity).movingDirection = direction;
    }

    public DIRECTION getLookDirection(){
        return Mappers.playerData.get(entity).lookingDirection;
    }

    public void setLookDirection(DIRECTION direction){
        Mappers.playerData.get(entity).lookingDirection = direction;
    }

    public Sprite getSprite(){
        return Mappers.spriteMap.get(entity).spritesList.first();
    }

    public float getReloadTimer(){
        return Mappers.weaponMap.get(entity).reloadTimer;
    }
    public float getReloadTime(){
        return Mappers.weaponMap.get(entity).getReloadTime();
    }
    public boolean canFire(){
        return Mappers.weaponMap.get(entity).canFire;
    }
    public void setCanFire(boolean canFire){
        Mappers.weaponMap.get(entity).canFire = canFire;
    }
}
