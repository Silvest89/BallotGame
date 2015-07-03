package eu.silvenia.shipballot.systems.Components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Johnnie Ho on 3-7-2015.
 */
public class FauxGravityComponent extends Component {
    public float gravity = 0.0f;

    public FauxGravityComponent(float gravity) {
        this.gravity = gravity;
    }
}