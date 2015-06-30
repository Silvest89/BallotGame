package eu.silvenia.shipballot.systems.Components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Johnnie Ho on 29-6-2015.
 */
public class VelocityComponent extends Component {
    public float velocity = 0.0f;

    public VelocityComponent(float velocity){
        this.velocity = velocity;
    }
}
