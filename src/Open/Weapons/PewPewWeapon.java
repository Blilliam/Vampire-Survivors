package Open.Weapons;

import Open.Entities.Enemies.Enemy;
import Open.Weapons.WeaponProjectile.BouncingProjectile;
import main.Assets;
import main.GameObject;
import main.Vec2;
import main.enums.WeaponTypes;

public class PewPewWeapon extends Weapon {
    public PewPewWeapon(GameObject gameObj) {
        super(gameObj, WeaponTypes.PewPew);
        this.atk = 8;
        this.speed = 12;      // Fast bullets
        this.atkDelay = 40;   // Fast fire rate
        this.projectileBounces = 2; // Diamond might increase this
        this.sprite = Assets.ProjectileBullet;
    }

    @Override
    public void update() {
        if (delayCounter > 0) {
            delayCounter--;
        } else {
            Enemy target = gameObj.getPlayer().closestEnemy(500);
            if (target != null) {
                spawn(target);
                delayCounter = atkDelay;
            }
        }
    }

    private void spawn(Enemy target) {
        Vec2 dir = new Vec2(target.getX() - gameObj.getPlayer().getX(), 
                            target.getY() - gameObj.getPlayer().getY());
        gameObj.addProjectiles(new BouncingProjectile(gameObj, this, dir, 
                                gameObj.getPlayer().getX(), gameObj.getPlayer().getY()));
    }
}