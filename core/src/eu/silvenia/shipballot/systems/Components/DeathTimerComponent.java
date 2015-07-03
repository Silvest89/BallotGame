package eu.silvenia.shipballot.systems.Components;

import com.badlogic.ashley.core.Component;
import eu.silvenia.shipballot.screens.Game;

/**
 * Created by Johnnie Ho on 3-7-2015.
 */
public class DeathTimerComponent extends Component {
    public long createTime;
    public long deathTime;

    public DeathTimerComponent(long deathTime) {
        createTime = Game.currentTimeMillis;
        this.deathTime = deathTime;
    }
}