package eu.silvenia.shipballot;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Johnnie Ho on 28-6-2015.
 */
public class DamageText implements DisplayObject{
    private static BitmapFont font = new BitmapFont();
    private String text;
    private float x, y;
    private float gravity = 0.004f;
    private float transparency = 0;
    private Vector2 momentum = new Vector2(0.03f, 0.1f);

    public DamageText(String text, float x, float y){
        font.getData().setScale(0.03f);
        this.text = text;
        this.x = x;
        this.y = y + 0.5f;
        font.setColor(Color.RED);

        EntityManager.addToDisplay(this);
    }

    public void update(){
        y+=momentum.y;
        x+= momentum.x;
        transparency += 1;
            momentum.y -= gravity;

        if(transparency >= 60)
            EntityManager.removeFromDisplay(this);
    }

    public void draw(Batch batch){
        font.draw(batch, text, x, y);
    }
}