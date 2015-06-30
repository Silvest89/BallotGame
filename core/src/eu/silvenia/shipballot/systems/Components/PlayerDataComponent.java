package eu.silvenia.shipballot.systems.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import eu.silvenia.shipballot.creature.PlayerTest;

/**
 * Created by Johnnie Ho on 29-6-2015.
 */
public class PlayerDataComponent extends CreatureDataComponent {
    public String name = "John Doe";
    public long level = 1;
    public long money = 0;
    public long health = 100;
    public long maxHealth = 100;
    public long shield = 0;
    public long maxShield = 0;
    public Vector2 keyforce = new Vector2();
    public boolean canJump = false;
    public PlayerTest.DIRECTION movingDirection = null;
    public PlayerTest.DIRECTION lookingDirection = null;
}
