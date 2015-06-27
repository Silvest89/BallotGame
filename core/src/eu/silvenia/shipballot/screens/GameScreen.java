package eu.silvenia.shipballot.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.*;
import eu.silvenia.shipballot.FpsTimer;
import eu.silvenia.shipballot.PhysicsManager;
import eu.silvenia.shipballot.ShipBallot;
import eu.silvenia.shipballot.TextManager;
import eu.silvenia.shipballot.creature.Player;
import eu.silvenia.shipballot.data.Assets;
import net.dermetfan.gdx.graphics.g2d.AnimatedBox2DSprite;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;
import net.dermetfan.gdx.physics.box2d.Box2DMapObjectParser;
import net.dermetfan.gdx.physics.box2d.Box2DUtils;


/**
 * Created by Johnnie Ho on 24-6-2015.
 */
public class GameScreen implements Screen {
    public static long currentTimeMillis;

    public World world;
    private Box2DDebugRenderer debugRenderer;

    ShipBallot game;

    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    private Player player;

    public GameScreen(ShipBallot game){
        this.game = game;
    }

    Batch batch;

    private Array<Body> tmpBodies = new Array<Body>();

    public static float SCALE;

    @Override
    public void show() {
        batch = new SpriteBatch();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        SCALE = 1;

        camera = new OrthographicCamera();

        debugRenderer = new Box2DDebugRenderer();
        world = new World(new Vector2(0f, -9.81f), false);

        TiledMap map = new TmxMapLoader().load("map/untitled.tmx");

        Box2DMapObjectParser parser = new Box2DMapObjectParser(.03125f);
        parser.load(world, map);

        tiledMapRenderer = new OrthogonalTiledMapRenderer(map, parser.getUnitScale());

        TextureAtlas playerAtlas = new TextureAtlas("player.pack");

        //Animation animation = new Animation(1 / 3f, playerAtlas.f);
        //AnimatedSprite animatedSprite = new AnimatedSprite(animation);

        player = new Player(this, playerAtlas, "Johnnie", world, camera);
        world.setContactFilter(player);
        world.setContactListener(player);

        TextManager.setBatch(batch);

        Gdx.input.setInputProcessor(player);
        TextManager.setBatch(batch);
    }

    @Override
    public void render(float delta) {
        currentTimeMillis = System.currentTimeMillis();
        FpsTimer.update();
        world.step(1 / 60f, 8, 3);

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.set(player.getBody().getPosition().x, player.getBody().getPosition().y, 0);
        player.update(delta);
        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        AnimatedBox2DSprite.draw(batch, world);
        player.draw(batch);
        TextManager.Draw("FPS: " + Gdx.graphics.getFramesPerSecond() + " Time: " + FpsTimer.time, camera);
        batch.end();

        //debugRenderer.render(world, camera.combined);
    }

    @Override
    public void resize(int width, int height){
        camera.viewportWidth = width / 35;
        camera.viewportHeight = height / 35;
        camera.update();
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
        world.dispose();
    }
}
