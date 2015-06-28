package eu.silvenia.shipballot;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import eu.silvenia.shipballot.creature.Creature;
import eu.silvenia.shipballot.creature.Player;
import eu.silvenia.shipballot.weapons.Bullet;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Johnnie Ho on 28-6-2015.
 */
public class EntityManager {
    private static Array<GameObject> objects= new Array<>();
    private static Array<GameObject> updateObjects= new Array<>();
    private static Array<GameObject> destroyObjects= new Array<>();
    private static Array<DisplayObject> displayObjects= new Array<>();

    public static void add(GameObject object){
        objects.add(object);
    }

    public static void setToUpdate(GameObject object){
        add(object);
        updateObjects.add(object);
    }

    public static void setToDestroy(GameObject object){
        objects.removeValue(object, true);
        updateObjects.removeValue(object, true);
        if(!destroyObjects.contains(object, true))
            destroyObjects.add(object);
    }

    public static void update(World world, float delta){
        for(GameObject object : destroyObjects){
                world.destroyBody(object.getBody());
        }
        destroyObjects.clear();

        for (GameObject object : updateObjects){
                object.update(delta);
        }
    }

    public static void addToDisplay(DisplayObject object){
        displayObjects.add(object);
    }

    public static void draw(Batch batch){
        for (GameObject object : updateObjects){
            object.draw(batch);
        }
        for (DisplayObject object : displayObjects){
            object.update();
            object.draw(batch);
        }
    }

    public static void removeFromDisplay(DisplayObject object){
        displayObjects.removeValue(object, true);
    }
}
