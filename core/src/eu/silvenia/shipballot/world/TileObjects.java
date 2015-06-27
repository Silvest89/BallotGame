package eu.silvenia.shipballot.world;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.HashMap;

/**
 * Created by Johnnie Ho on 26-6-2015.
 */
public class TileObjects {
    public static HashMap<TiledMapTileLayer.Cell, TileObjects> mapTiles = new HashMap<>();
    public boolean hasCreature;

    public TileObjects(){
        hasCreature = false;
    }

    public void add(TiledMapTileLayer.Cell cell){
        mapTiles.put(cell, this);
    }

    public boolean isHasCreature() {
        return hasCreature;
    }

    public void setHasPlayer(boolean hasCreature) {
        this.hasCreature = hasCreature;
    }
}
