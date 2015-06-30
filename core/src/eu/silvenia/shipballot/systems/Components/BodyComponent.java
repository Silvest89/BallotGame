package eu.silvenia.shipballot.systems.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Johnnie Ho on 29-6-2015.
 */
public class BodyComponent extends Component {
    public Body body;

    public BodyComponent(Body body) {
        this.body = body;
        //this.body.setTransform(positionCom.x * PhysicsManager.PIXELS_TO_METERS, positionCom.y * PhysicsManager.PIXELS_TO_METERS, 0);
    }
}