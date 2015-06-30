package eu.silvenia.shipballot;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;

/**
 * Created by Johnnie Ho on 29-6-2015.
 */
public interface Collidable {
    public void handleCollision(Engine engine, Entity collider, Entity collidee);
//    public void handleSensorCollision(short categoryBits, boolean beginCollision);
}