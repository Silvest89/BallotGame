package eu.silvenia.shipballot.systems.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Johnnie Ho on 29-6-2015.
 */
public class PositionComponent extends Component{
    public float x = 0.0f;
    public float y = 0.0f;
    public int z = 0;

    public PositionComponent(float x, float y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
