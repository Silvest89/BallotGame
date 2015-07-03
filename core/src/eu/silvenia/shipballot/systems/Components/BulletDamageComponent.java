package eu.silvenia.shipballot.systems.Components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Johnnie Ho on 3-7-2015.
 */
public class BulletDamageComponent extends Component {
    public long damage;

    public BulletDamageComponent(long damage) {
        this.damage = damage;
    }
}