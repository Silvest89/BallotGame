package eu.silvenia.shipballot.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.AtlasTmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import eu.silvenia.shipballot.ShipBallot;
import eu.silvenia.shipballot.creature.Creature;

/**
 * Created by Johnnie Ho on 24-6-2015.
 */
public class GameScreen implements Screen {

    ShipBallot game;

    private TiledMap tiledMap;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private MapProperties prop;

    private Creature player;

    public GameScreen(ShipBallot game){
        this.game = game;
    }

    @Override
    public void show() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false,w,h);
        camera.update();

        AtlasTmxMapLoader.AtlasTiledMapLoaderParameters parameters = new AtlasTmxMapLoader.AtlasTiledMapLoaderParameters();
        parameters.forceTextureFilters = true;
        parameters.textureMinFilter = Texture.TextureFilter.Nearest;
        parameters.textureMagFilter = Texture.TextureFilter.Nearest;

        tiledMap = new AtlasTmxMapLoader().load("map/untitled.tmx", parameters);
        prop = tiledMap.getProperties();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        TextureAtlas playerAtlas = new TextureAtlas("player.pack");

        player = new Creature(this, playerAtlas, (TiledMapTileLayer)tiledMap.getLayers().get(0));
        player.setPosition(2 * player.getCollisionLayer().getTileWidth(), (player.getCollisionLayer().getHeight() - 46) * player.getCollisionLayer().getTileHeight());
        Gdx.input.setInputProcessor(player);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        tiledMapRenderer.setView(camera);

        tiledMapRenderer.getBatch().begin();
        tiledMapRenderer.renderTileLayer((TiledMapTileLayer) tiledMap.getLayers().get("background"));
        player.draw(tiledMapRenderer.getBatch());
        tiledMapRenderer.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        tiledMap.dispose();
        tiledMapRenderer.dispose();
    }

    public int getMapWidth(){
        return prop.get("width", Integer.class);
    }

    public int getMapHeight(){
        return prop.get("height", Integer.class);
    }

    public int getTileWidth(){
        return prop.get("tilewidth", Integer.class);
    }

    public int getTileHeight(){
        return prop.get("tileheight", Integer.class);
    }
}
