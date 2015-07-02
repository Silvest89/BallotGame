package eu.silvenia.shipballot.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import eu.silvenia.shipballot.*;
import eu.silvenia.shipballot.creature.Creature;
import eu.silvenia.shipballot.creature.Player;
import eu.silvenia.shipballot.data.Assets;
import eu.silvenia.shipballot.systems.Components.BodyComponent;
import eu.silvenia.shipballot.systems.Components.PositionComponent;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;
import net.dermetfan.gdx.physics.box2d.Box2DMapObjectParser;

import java.util.ArrayList;


/**
 * Created by Johnnie Ho on 24-6-2015.
 */
public class Game implements Screen{
    public static long currentTimeMillis;

    public World world;
    private Box2DDebugRenderer debugRenderer;
    public Engine engine;

    public AshleyEntityManager ashleyEntityManager;
    ShipBallot game;

    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    public Box2DMapObjectParser parser;

    public static ArrayList<Creature> playerList = new ArrayList<>();

    public Game(ShipBallot game){
        this.game = game;
    }

    Player player;
    Batch batch;

    private Array<Body> tmpBodies = new Array<Body>();

    public static float SCALE;

    private Touchpad touchpad;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;
    private Stage stage;
    private HUDSideBar hudStage;

    private Skin skin = Assets.menuSkin;
    Table table = new Table();
    Button buttonPlay = new TextButton("Start Game", skin);


    @Override
    public void show() {

        engine = new Engine();

        batch = new SpriteBatch();
        world = new World(new Vector2(0f, -9.81f), false);

        ashleyEntityManager = new AshleyEntityManager(engine, world, batch);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        SCALE = 1;

        camera = new OrthographicCamera();

        debugRenderer = new Box2DDebugRenderer();

        TiledMap map = new TmxMapLoader().load("map/naamloos.tmx");

        parser = new Box2DMapObjectParser(1 / 35f);
        parser.load(world, map);

        tiledMapRenderer = new OrthogonalTiledMapRenderer(map, parser.getUnitScale());

        TextureAtlas playerAtlas = new TextureAtlas("player.pack");

        //Create a touchpad skin
        touchpadSkin = new Skin();
        //Set background image
        touchpadSkin.add("touchBackground", new Texture("touchBackground.png"));
        //Set knob image
        touchpadSkin.add("touchKnob", new Texture("touchKnob.png"));
        //Create TouchPad Style
        touchpadStyle = new Touchpad.TouchpadStyle();
        //Create Drawable's from TouchPad skin
        touchBackground = touchpadSkin.getDrawable("touchBackground");
        touchKnob = touchpadSkin.getDrawable("touchKnob");
        //Apply the Drawables to the TouchPad Style
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        //Create new TouchPad with the created style
        touchpad = new Touchpad(10, touchpadStyle);
        //setBounds(x,y,width,height)
        touchpad.setBounds(15, 15, 200, 200);

        hudStage = new HUDSideBar(AshleyEntityManager.playerTest);

        stage = new Stage();
        stage.addActor(touchpad);
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(AshleyEntityManager.playerTest);
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);

        TextManager.setBatch(batch);
    }

    @Override
    public void render(float delta) {
        world.step(1 / 60f, 8, 3);
        //EntityManager.update(world, delta);
        currentTimeMillis = System.currentTimeMillis();
        FpsTimer.update();

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Gdx.gl.glViewport( 0,0,Gdx.graphics.getWidth()-200,Gdx.graphics.getHeight() );
        //camera.position.set(player.getBody().getPosition().x, player.getBody().getPosition().y, 0);
        camera.position.set(AshleyEntityManager.player.getComponent(BodyComponent.class).body.getPosition().x, AshleyEntityManager.player.getComponent(BodyComponent.class).body.getPosition().y, 0);
        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        ashleyEntityManager.update(delta);
        batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        if(touchpad.isTouched()) {
            System.out.println(touchpad.getKnobPercentX() + " " + touchpad.getKnobPercentY());
            if(touchpad.getKnobPercentY() != 0.0 && touchpad.getKnobPercentX() != 0.0) {
                if (touchpad.getKnobPercentX() > -0.75f && touchpad.getKnobPercentX() < 0.75f && touchpad.getKnobPercentY() > 0.65f)
                    System.out.println("UP");
                if (touchpad.getKnobPercentX() > -0.75f && touchpad.getKnobPercentX() < 0.75f && touchpad.getKnobPercentY() < -0.65f)
                    System.out.println("DOWN");
                if (touchpad.getKnobPercentY() > -0.75f && touchpad.getKnobPercentY() < 0.75f && touchpad.getKnobPercentX() > 0.65f)
                    System.out.println("LEFT");
                if (touchpad.getKnobPercentY() > -0.75f && touchpad.getKnobPercentY() < 0.75f && touchpad.getKnobPercentX() < -0.65f)
                    System.out.println("RIGHT");
            }
        }
        debugRenderer.render(world, camera.combined);

        stage.getBatch().setProjectionMatrix(camera.combined);
        //Gdx.gl.glViewport( Gdx.graphics.getWidth() - 200,0, 200,Gdx.graphics.getHeight() );
        hudStage.act();
        hudStage.draw();
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

    /*@Override
    public void beginContact(Contact contact) {
        /*GameObject object = (GameObject) contact.getFixtureA().getBody().getUserData();
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
