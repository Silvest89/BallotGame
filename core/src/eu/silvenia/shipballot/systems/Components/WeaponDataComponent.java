package eu.silvenia.shipballot.systems.Components;

import com.badlogic.ashley.core.Component;
import eu.silvenia.shipballot.weapons.Weapon;

/**
 * Created by Johnnie Ho on 29-6-2015.
 */
public class WeaponDataComponent extends Component{
    private long reloadTime;

    public long reloadTimer = 0;
    public boolean canFire = false;

    private float weaponSpeed;
    private Weapon.WeaponType weaponType;

    private long weaponDamage;

    public WeaponDataComponent(Weapon.WeaponType weaponType){
        this.weaponType = weaponType;
        switch(weaponType){
            case PISTOL:{
                reloadTime = 2000;
                weaponSpeed = 7f;
                weaponDamage = 10;
                break;
            }
            case RIFLE:{
                reloadTime = 3500;
                weaponSpeed = 10f;
                weaponDamage = 25;
                break;
            }
            case SHOTGUN:{
                reloadTime = 3000;
                weaponSpeed = 5.5f;
                weaponDamage = 20;
                break;
            }
        }
    }

    public long getReloadTime() {
        return reloadTime;
    }

    public void setReloadTime(long reloadTime) {
        this.reloadTime = reloadTime;
    }

    public boolean isCanFire() {
        return canFire;
    }

    public void setCanFire(boolean canFire) {
        this.canFire = canFire;
    }

    public float getWeaponSpeed() {
        return weaponSpeed;
    }

    public void setWeaponSpeed(float weaponSpeed) {
        this.weaponSpeed = weaponSpeed;
    }

    public Weapon.WeaponType getWeaponType() {
        return weaponType;
    }

    public long getWeaponDamage() {
        return weaponDamage;
    }
}
