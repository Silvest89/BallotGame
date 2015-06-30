package eu.silvenia.shipballot;

import com.badlogic.ashley.core.ComponentMapper;
import eu.silvenia.shipballot.systems.Components.*;

/**
 * Created by Johnnie Ho on 30-6-2015.
 */
public class Mappers {
    public static final ComponentMapper<PositionComponent>positionMap = ComponentMapper.getFor(PositionComponent.class);
    public static final ComponentMapper<VelocityComponent>velocityMap = ComponentMapper.getFor(VelocityComponent.class);
    public static final ComponentMapper<PlayerDataComponent>playerData = ComponentMapper.getFor(PlayerDataComponent.class);
    public static final ComponentMapper<BodyComponent> bodyMap = ComponentMapper.getFor(BodyComponent.class);
    public static final ComponentMapper<SpriteComponent>spriteMap = ComponentMapper.getFor(SpriteComponent.class);
    public static final ComponentMapper<BulletCollisionComponent>bulletColMap    = ComponentMapper.getFor(BulletCollisionComponent.class);
    public static final ComponentMapper<PlayerCollisionComponent>playerColMap    = ComponentMapper.getFor(PlayerCollisionComponent.class);
    public static final ComponentMapper<TypeComponent>typeMap = ComponentMapper.getFor(TypeComponent.class);
    public static final ComponentMapper<ShootingComponent>shootingMap = ComponentMapper.getFor(ShootingComponent.class);
    public static final ComponentMapper<WeaponDataComponent>weaponMap = ComponentMapper.getFor(WeaponDataComponent.class);
    public static final ComponentMapper<AttachedComponent>attachedMap = ComponentMapper.getFor(AttachedComponent.class);


}