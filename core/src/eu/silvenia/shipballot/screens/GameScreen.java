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
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.*;
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

    Viewport viewport;
    Stage stage;

    @Override
    public void show() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(800, 480,camera);
        viewport.apply();

        stage = new Stage();
        Image splashImage = new Image(new Texture(Gdx.files.internal("splash.png")));

        stage.addActor(splashImage);

        TmxMapLoader.Parameters parameters = new TmxMapLoader.Parameters();
        parameters.textureMinFilter = Texture.TextureFilter.Nearest;
        parameters.textureMagFilter = Texture.TextureFilter.Nearest;

        tiledMap = new TmxMapLoader().load("map/untitled.tmx", parameters);
        prop = tiledMap.getProperties();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        TextureAtlas playerAtlas = new TextureAtlas("player.pack");

        player = new Creature(this, playerAtlas, (TiledMapTileLayer)tiledMap.getLayers().get(0));
        player.setPosition(2 * player.getCollisionLayer().getTileWidth(), 2 * player.getCollisionLayer().getTileHeight());
        Gdx.input.setInputProcessor(player);

        //camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);
        camera.position.set((player.getX() + player.getWidth() / 2), (player.getY() + player.getHeight() / 2), 0);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl.glViewport(0, Gdx.graphics.getHeight() - 100, Gdx.graphics.getWidth(), 100);
        stage.draw();
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - 100);

        camera.position.set((player.getX() + player.getWidth() / 2), (player.getY() + player.getHeight() / 2), 0);
        camera.update();

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.getBatch().begin();
        tiledMapRenderer.renderTileLayer((TiledMapTileLayer) tiledMap.getLayers().get("background"));
        player.draw(tiledMapRenderer.getBatch());
        tiledMapRenderer.getBatch().end();
    }

    @Override
    public void resize(int width, int height){
        viewport.update(width,height);
        camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);
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
