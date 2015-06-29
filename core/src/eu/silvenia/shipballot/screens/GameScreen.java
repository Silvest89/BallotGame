package eu.silvenia.shipballot.screens;

import com.badlogic.ashley.core.Engine;
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
import com.badlogic.gdx.utils.Array;
import eu.silvenia.shipballot.*;
import eu.silvenia.shipballot.creature.Creature;
import eu.silvenia.shipballot.creature.Player;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;
import net.dermetfan.gdx.physics.box2d.Box2DMapObjectParser;

import java.util.ArrayList;


/**
 * Created by Johnnie Ho on 24-6-2015.
 */
public class GameScreen implements Screen, ContactFilter, ContactListener{
    public static long currentTimeMillis;

    public World world;
    private Box2DDebugRenderer debugRenderer;
    public Engine engine;

    ShipBallot game;

    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    public Box2DMapObjectParser parser;

    public static ArrayList<Creature> playerList = new ArrayList<>();

    public GameScreen(ShipBallot game){
        this.game = game;
    }

    Player player;
    Batch batch;

    private Array<Body> tmpBodies = new Array<Body>();

    public static float SCALE;

    @Override
    public void show() {

        engine = new Engine();

        batch = new SpriteBatch();
        world = new World(new Vector2(0f, -9.81f), false);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        SCALE = 1;

        camera = new OrthographicCamera();

        debugRenderer = new Box2DDebugRenderer();

        TiledMap map = new TmxMapLoader().load("map/untitled.tmx");

        parser = new Box2DMapObjectParser(.03125f);
        parser.load(world, map);

        tiledMapRenderer = new OrthogonalTiledMapRenderer(map, parser.getUnitScale());

        TextureAtlas playerAtlas = new TextureAtlas("player.pack");

        //Animation animation = new Animation(1 / 3f, playerAtlas.f);
        //AnimatedSprite animatedSprite = new AnimatedSprite(animation);

        AnimatedSprite animatedSprite = new AnimatedSprite(new Animation(1 / 4f, playerAtlas.findRegions("southStanding")));

        player = new Player(this, animatedSprite, "Johnnie", world, camera);
        player.setupAnimation(playerAtlas);
        playerList.add(player);
        Player player2 = new Player(this, animatedSprite, "Kevin", world, camera);
        player2.setupAnimation(playerAtlas);
        playerList.add(player2);

        //world.setContactFilter(this);
        world.setContactListener(this);

        TextManager.setBatch(batch);

        Gdx.input.setInputProcessor(player);
        TextManager.setBatch(batch);
    }

    @Override
    public void render(float delta) {
        world.step(1 / 60f, 8, 3);
        EntityManager.update(world, delta);
        currentTimeMillis = System.currentTimeMillis();
        FpsTimer.update();

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        camera.position.set(player.getBody().getPosition().x, player.getBody().getPosition().y, 0);
        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        Box2DSprite.draw(batch, world);
        EntityManager.draw(batch);
        TextManager.Draw("FPS: " + Gdx.graphics.getFramesPerSecond() + " Time: " + FpsTimer.time, camera);
        batch.end();

        debugRenderer.render(world, camera.combined);
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

    @Override
    public void beginContact(Contact contact) {
        GameObject object = (GameObject) contact.getFixtureA().getBody().getUserData();
        GameObject object2 = (GameObject) contact.getFixtureB().getBody().getUserData();

        if(object != null) {
            object.handleCollision(contact.getFixtureB().getBody());
        }
        if(object2 != null)
            object2.handleCollision(contact.getFixtureA().getBody());
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    @Override
    public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
        if(fixtureA.getBody().getUserData() == null || fixtureB.getBody().getUserData() == null) {
            return true;
        }
        return false;
    }

    /*@Override
    public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
        if(fixtureA.getBody().getUserData() == null || fixtureB.getBody().getUserData() == null) {
            return true;
        }

        return false;
    }

    @Override
    public void beginContact(Contact contact) {

        //Entity entity = (Entity) contact.getFixtureA().getBody().getUserData();
        //Entity entity2 = (Entity) contact.getFixtureB().getBody().getUserData();

        /*if(entity != null) {
            object.handleCollision(contact.getFixtureB().getBody());
        }
        if(object2 != null)
            object2.handleCollision(contact.getFixtureA().getBody());
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }*/
}
