package Open.Weapons;

import Open.Entities.Enemies.Enemy;
import Open.Weapons.WeaponProjectile.SwordProjectile;
import main.GameObject;
import main.Vec2;
import main.enums.WeaponTypes;

public class SwordWeapon extends Weapon {

    public SwordWeapon(GameObject gameObj) {
        super(gameObj, WeaponTypes.Sword);
        this.atk = 15;
        this.speed = 0;       // Melee weapons don't "fly"
        this.atkDelay = 35;   // Very fast slashes
        this.projectileCount = 1;
        this.size = 1.5;      // Large reach
        //this.sprite = Assets.katanaSlashSprite; // A crescent moon slash sprite
    }

    @Override
    public void update() {
        if (delayCounter > 0) {
            delayCounter--;
        } else {
            // Find the closest enemy within a short range
            Enemy target = gameObj.getPlayer().closestEnemy(200);
            
            if (target != null) {
                spawnSlash(target);
                delayCounter = atkDelay;
            }
        }
    }

    private void spawnSlash(Enemy target) {
        // Calculate direction toward the enemy
        Vec2 dir = new Vec2(target.getX() - gameObj.getPlayer().getX(), 
                            target.getY() - gameObj.getPlayer().getY());
        
        // Spawn the slash at the player's position
        gameObj.addProjectiles(new SwordProjectile(gameObj, this, dir, 
                               gameObj.getPlayer().getX(), gameObj.getPlayer().getY()));
    }
}