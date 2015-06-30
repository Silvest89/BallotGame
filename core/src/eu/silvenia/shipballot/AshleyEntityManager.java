package eu.silvenia.shipballot;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import eu.silvenia.shipballot.creature.Player;
import eu.silvenia.shipballot.creature.PlayerTest;
import eu.silvenia.shipballot.systems.*;
import eu.silvenia.shipballot.systems.Components.*;
import eu.silvenia.shipballot.weapons.Weapon;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

/**
 * Created by Johnnie Ho on 29-6-2015.
 */
public class AshleyEntityManager {
    private Engine engine;
    public static Entity player;
    public static PlayerTest playerTest;
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
        CollisionManager collisionSystem         = new CollisionManager(engine, world);
        engine.addSystem(positionSystem);
        engine.addSystem(movementSystem);
        engine.addSystem(renderSystem);

        engine.addSystem(shootWeaponSystem);


        TextureAtlas playerAtlas = new TextureAtlas("player.pack");

        AnimatedSprite animatedSprite = new AnimatedSprite(new Animation(1 / 4f, playerAtlas.findRegions("southStanding")));
        animatedSprite.setSize(2f, 2f);
        animatedSprite.setOrigin(animatedSprite.getWidth() / 2, animatedSprite.getHeight() / 2);
        player = new Entity();
        playerTest = new PlayerTest(player);
        playerTest.setupAnimation(playerAtlas);
        Body body;
        // reusable construction objects
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();

        // a ball
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(8,4);
        bodyDef.fixedRotation = true;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1f, 1f);

        fixtureDef.shape = shape;
        fixtureDef.density = 2.0f;
        fixtureDef.restitution = 0f;

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setUserData(player);

        player.add(new SpriteComponent(animatedSprite))
                .add(new BodyComponent(body))
                .add(new PlayerDataComponent())
                .add(new PositionComponent(8, 8, 0))
                .add(new RenderableComponent())
                .add(new PlayerCollisionComponent())
                .add(new TypeComponent(PhysicsManager.COL_PLAYER))
                .add(new VelocityComponent(0))
                .add(new WeaponDataComponent(Weapon.WeaponType.PISTOL))
                .add(new ShootingComponent());

        engine.addEntity(player);
        AshleyEntityManager.add(player);
        AshleyEntityManager.add(playerTest);
    }

    private static Array<Entity> entities = new Array<Entity>();
    private static Array<Entity> destroyEntities = new Array<Entity>();
    private static Array<Updateable> updateables = new Array<Updateable>();

    public void update(float deltaTime){
        engine.update(deltaTime);
        for (Updateable updateable : updateables) {
            updateable.update(deltaTime);
        }
        //Box2DSprite.draw(batch, world);
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
