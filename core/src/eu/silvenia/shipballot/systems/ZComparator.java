package eu.silvenia.shipballot.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import eu.silvenia.shipballot.systems.Components.PositionComponent;

import java.util.Comparator;

/**
 * Created by Johnnie Ho on 29-6-2015.
 */


/**
 * Created by Phil on 3/3/2015.
 */
public class ZComparator implements Comparator<Entity> {
    private ComponentMapper<PositionComponent> posMap = ComponentMapper.getFor(PositionComponent.class);

    @Override
    public int compare(Entity e1, Entity e2) {
        return (int)Math.signum(posMap.get(e1).z - posMap.get(e2).z);
    }
}