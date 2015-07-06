package eu.silvenia.shipballot.creature;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import eu.silvenia.shipballot.Mappers;
import eu.silvenia.shipballot.Updateable;
import eu.silvenia.shipballot.screens.Game;
import eu.silvenia.shipballot.systems.Components.*;
import eu.silvenia.shipballot.weapons.Weapon;

import java.util.Map;

/**
 * Created by Johnnie Ho on 29-6-2015.
 */
public class Player implements Updateable, InputProcessor {
    private Entity entity;
    private Entity weapon;

    public StateMachine<Player> stateMachine;

    public enum DIRECTION{
        EAST,
        WEST
    }
    public float animationTime = 0;
    public Touchpad touchpad;

    public long jumpTimer;

    protected Animation southStanding, westStanding, eastStanding, northStanding;
    protected Animation south, west, east, north;

    public Player(Entity entity){
        this.entity = entity;
        stateMachine = new DefaultStateMachine<Player>(this, PlayerState.GROUNDED);

        weapon = new Entity();
        weapon.add(new WeaponDataComponent(Weapon.WeaponType.PISTOL))
            .add(new ShootingComponent())
            .add(new AttachedComponent(entity));

    }

    public void update(float delta) {

        //touchPadHandler();

        animationTime = animationTime + (160 * delta / 100);

        stateMachine.update();

        if(getMovingDirection() != null)
            animatePlayer();

    }

    public void touchPadHandler(){
        if(touchpad.isTouched()) {
            System.out.println(touchpad.getKnobPercentX() + " " + touchpad.getKnobPercentY());

        }
        else
            stopPlayer();
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
                jumpPlayer();
                break;
            }
            case Input.Keys.SHIFT_LEFT:{
                if(!canFire() && getReloadTimer() <= Game.currentTimeMillis) {
                    setCanFire(true);
                }
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

    public void jumpPlayer(){
        if (canJump() && Game.currentTimeMillis >= jumpTimer) {

            getBody().applyLinearImpulse(0,
                    getJumpForce(),
                    getBody().getWorldCenter().x,
                    getBody().getWorldCenter().y,
                    true);
            jumpTimer = Game.currentTimeMillis + 1000;
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

    public long getReloadTimer(){
        return Mappers.weaponMap.get(weapon).reloadTimer;
    }
    public boolean canFire(){
        return Mappers.weaponMap.get(weapon).canFire;
    }
    public void setCanFire(boolean canFire){
        Mappers.weaponMap.get(weapon).canFire = canFire;
    }

    public long getExperience(){
        return Mappers.playerData.get(entity).experience;
    }
    public static long getExpForNextLv(int level){
        level--;
        return ((50L * level * level * level) - (150L * level * level) + (400L * level)) / 3L;
    }

    public Weapon.WeaponType getWeaponType(){
        return Mappers.weaponMap.get(weapon).getWeaponType();
    }
    public Entity getWeapon(){
        return weapon;
    }

    ChangeListener touchPadListener = new ChangeListener() {
        @Override
        public void changed(ChangeEvent event, Actor actor) {

        }
    };

    public void setTouchPad(Touchpad touchPad){
        this.touchpad = touchPad;
        touchpad.addListener(new ChangeListener(){

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(touchpad.getKnobPercentY() != 0.0 && touchpad.getKnobPercentX() != 0.0) {
                    if (touchpad.getKnobPercentX() > -0.75f && touchpad.getKnobPercentX() < 0.75f && touchpad.getKnobPercentY() > 0.65f)
                        jumpPlayer();
                    if (touchpad.getKnobPercentX() > -0.75f && touchpad.getKnobPercentX() < 0.75f && touchpad.getKnobPercentY() < -0.65f)
                        System.out.println("DOWN");
                    if (touchpad.getKnobPercentY() > -0.75f && touchpad.getKnobPercentY() < 0.75f && touchpad.getKnobPercentX() > 0.65f)
                        movePlayer(DIRECTION.EAST);
                    if (touchpad.getKnobPercentY() > -0.75f && touchpad.getKnobPercentY() < 0.75f && touchpad.getKnobPercentX() < -0.65f)
                        movePlayer(DIRECTION.WEST);
                }else
                    stopPlayer();
            }

        });
    }
}
