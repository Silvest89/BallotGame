package eu.silvenia.shipballot.systems.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * Created by Johnnie Ho on 30-6-2015.
 */
public class AttachedComponent extends Component {
    public Entity attachedTo;

    public AttachedComponent(Entity attachedTo) {
        this.attachedTo = attachedTo;
    }
}
