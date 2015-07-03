package eu.silvenia.shipballot;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import eu.silvenia.shipballot.creature.Player;
import eu.silvenia.shipballot.systems.*;
import eu.silvenia.shipballot.systems.Components.*;
import eu.silvenia.shipballot.weapons.Weapon;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

/**
 * Created by Johnnie Ho on 29-6-2015.
 */
public class AshleyEntityManager {
    private static Engine engine;
    public static Entity enityPlayer;
    public static Player player;
    private World world;
    private Batch batch;

    public AshleyEntityManager(Engine engine, World world, Batch batch){
        this.engine = engine;

        this.world = world;
        this.batch = batch;
        MovementSystem movementSystem = new MovementSystem();
        RenderSystem renderSystem = new RenderSystem(batch);
        PositionSystem positionSystem = new PositionSystem();
        ShootWeaponSystem shootWeaponSystem = new ShootWeaponSystem(engine, world);
        HealthBarSystem healthBarSystem = new HealthBarSystem();
        NameBarSystem nameBarSystem = new NameBarSystem(batch);
        CollisionManager collisionSystem         = new CollisionManager(engine, world);
        engine.addSystem(healthBarSystem);
        engine.addSystem(positionSystem);
        engine.addSystem(nameBarSystem);
        engine.addSystem(movementSystem);
        engine.addSystem(renderSystem);
        engine.addSystem(new BitmapFontRenderSystem(batch));
        engine.addSystem(new DeathTimerSystem());
        engine.addSystem(new FauxGravitySystem());

        engine.addSystem(shootWeaponSystem);


        TextureAtlas playerAtlas = new TextureAtlas("player.pack");

        AnimatedSprite animatedSprite = new AnimatedSprite(new Animation(1 / 4f, playerAtlas.findRegions("southStanding")));
        animatedSprite.setSize(1.8f, 1.8f);
        animatedSprite.setOrigin(animatedSprite.getWidth() / 2, animatedSprite.getHeight() / 2);
        enityPlayer = new Entity();
        player = new Player(enityPlayer);
        player.setupAnimation(playerAtlas);
        Body body;
        // reusable construction objects
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();

        // a ball
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(3, 15);
        bodyDef.fixedRotation = true;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.9f, 0.9f);

        fixtureDef.shape = shape;
        fixtureDef.density = 2.5f;
        fixtureDef.restitution = 0f;

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        PolygonShape shape2 = new PolygonShape();
        shape2.setAsBox(0.7f, 0.1f, new Vector2(0, -1.1f), 0);

        fixtureDef.shape = shape2;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = PhysicsManager.FOOT_SENSOR;
        body.createFixture(fixtureDef);


        body.setUserData(enityPlayer);

        enityPlayer.add(new SpriteComponent(animatedSprite))
                .add(new BodyComponent(body))
                .add(new PlayerDataComponent())
                .add(new PositionComponent(8, 8, 0))
                .add(new RenderableComponent())
                .add(new PlayerCollisionComponent())
                .add(new TypeComponent(PhysicsManager.COL_PLAYER))
                .add(new VelocityComponent(0, 0));

        Entity healthBar = new Entity();
        AttachedComponent attachedComponent = new AttachedComponent(enityPlayer);
        PositionComponent positionComponent = new PositionComponent(0, 0, 0);
        HealthBarComponent healthBarComponent = new HealthBarComponent();

        Sprite sprite = new Sprite(new Texture("healthbarbg.png"));
        Sprite sprite2 = new Sprite(new Texture("healthbarfg.png"));

        sprite.setSize(2 , 0.3f);
        sprite2.setSize(2, 0.3f);
        sprite2.setOrigin(0, 0);

        SpriteComponent spriteComponent = new SpriteComponent(sprite, sprite2);

        healthBar.add(attachedComponent).add(positionComponent).add(spriteComponent).add(new RenderableComponent()).add(healthBarComponent);

        Entity nameBar = new Entity();
        attachedComponent = new AttachedComponent(enityPlayer);
        positionComponent = new PositionComponent(0, 0, 0);
        NameBarComponent nameBarComponent = new NameBarComponent();
        nameBar.add(attachedComponent).add(positionComponent).add(nameBarComponent).add(new RenderableComponent());

        engine.addEntity(enityPlayer);
        engine.addEntity(healthBar);
        engine.addEntity(nameBar);
        engine.addEntity(player.getWeapon());
        AshleyEntityManager.add(player);
    }

    public static Engine getEngine(){
        return engine;
    }

    private static Array<Entity> entities = new Array<Entity>();
    private static Array<Entity> destroyEntities = new Array<Entity>();
    private static Array<Updateable> updateables = new Array<Updateable>();

    public void update(float deltaTime){
        engine.update(deltaTime);
        for (Updateable updateable : updateables) {
            updateable.update(deltaTime);
        }

        for (Entity entity : destroyEntities) {
            try {
                world.destroyBody(entity.getComponent(BodyComponent.class).body);
            } catch(Exception e) {
                //do nothing
            }
            engine.removeEntity(entity);
        }
        destroyEntities.clear();
    }

    public static void add(Entity entity) {
        entities.add(entity);
    }
    public static void add(Updateable agent) { updateables.add(agent); }

    public static void setToDestroy(Entity entity) {
        entities.removeValue(entity, true);
        destroyEntities.add(entity);
    }

    public static void update(World world) {
        for (Entity entity : destroyEntities) {
            world.destroyBody(entity.getComponent(BodyComponent.class).body);
        }
        destroyEntities.clear();
    }
}
