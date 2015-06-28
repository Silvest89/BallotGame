package eu.silvenia.shipballot;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Created by Johnnie Ho on 28-6-2015.
 */
interface DisplayObject {
    void draw(Batch batch);
    void update();
}
