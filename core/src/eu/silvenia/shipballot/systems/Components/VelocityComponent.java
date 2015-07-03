package eu.silvenia.shipballot.systems.Components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Johnnie Ho on 29-6-2015.
 */
public class VelocityComponent extends Component {
    public float x = 0.0f;
    public float y = 0.0f;

    public VelocityComponent(float x, float y){
        this.x = x;
        this.y = y;
    }
}
