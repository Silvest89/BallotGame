package eu.silvenia.shipballot.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.*;
import eu.silvenia.shipballot.BodyGenerator;
import eu.silvenia.shipballot.Mappers;
import eu.silvenia.shipballot.systems.Components.*;

/**
 * Created by Johnnie Ho on 29-6-2015.
 */
public class ShootWeaponSystem extends IteratingSystem {
    private Engine engine;
    private World world;

    public ShootWeaponSystem(Engine engine, World world) {
        super(Family.all(ShootingComponent.class, WeaponDataComponent.class).get());
        this.engine = engine;
        this.world = world;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        float reloadTime = Mappers.weaponMap.get(entity).getReloadTime();

        if(Mappers.weaponMap.get(entity).canFire && Mappers.weaponMap.get(entity).reloadTimer >= reloadTime){
            BodyGenerator.generateBullet(world, engine, entity, 270 , 1.5f);
            Mappers.weaponMap.get(entity).canFire = false;
            Mappers.weaponMap.get(entity).reloadTimer = 0f;
        }
        else{
            if(Mappers.weaponMap.get(entity).reloadTimer < reloadTime) {
                Mappers.weaponMap.get(entity).reloadTimer += deltaTime;
            }
        }
    }
}
