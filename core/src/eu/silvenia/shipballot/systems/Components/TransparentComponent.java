package eu.silvenia.shipballot.systems.Components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Johnnie Ho on 3-7-2015.
 */
public class TransparentComponent extends Component {
    public float transparency = 1;

    public TransparentComponent(float transparency) {
        this.transparency = transparency;
    }
}