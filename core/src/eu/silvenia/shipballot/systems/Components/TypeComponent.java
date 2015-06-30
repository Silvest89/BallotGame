package eu.silvenia.shipballot.systems.Components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Johnnie Ho on 29-6-2015.
 */
public class TypeComponent extends Component {
    public short type = 0;

    public TypeComponent(short type) {
        this.type = type;
    }
}