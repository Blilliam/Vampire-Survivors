package Open.Weapons;

import Open.Entities.Enemies.Enemy;
import Open.Weapons.WeaponProjectile.FireStaffProjectile;
import main.Assets;
import main.GameObject;
import main.Vec2;

public class FireStaffWeapon extends Weapon {
    public FireStaffWeapon(GameObject gameObj) {
        super(gameObj);
        this.atk = 10;
        this.speed = 8;
        this.atkDelay = 60; // 1 second
        this.projectileCount = 1;
        this.size = 1.0;
        //this.sprite = Assets.boomStickProjectile; // fireball sprite
    }

    @Override
    public void update() {
        if (delayCounter > 0) {
            delayCounter--;
        } else {
            Enemy target = gameObj.getPlayer().closestEnemy(600);
            if (target != null) {
                spawnProjectile(target);
                delayCounter = atkDelay;
            }
        }
    }

    private void spawnProjectile(Enemy target) {
        Vec2 dir = new Vec2(target.getX() - gameObj.getPlayer().getX(), 
                            target.getY() - gameObj.getPlayer().getY());
        
        gameObj.addProjectiles(new FireStaffProjectile(gameObj, this, dir, 
                               gameObj.getPlayer().getX(), gameObj.getPlayer().getY()));
    }
}