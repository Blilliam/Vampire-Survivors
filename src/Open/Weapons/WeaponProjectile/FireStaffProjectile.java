package Open.Weapons.WeaponProjectile;

import Open.Weapons.Weapon;
import main.GameObject;
import main.Vec2;

public class FireStaffProjectile extends WeaponEntity {
    public FireStaffProjectile(GameObject gameObj, Weapon weapon, Vec2 direction, int x, int y) {
        super(gameObj, weapon, direction, x, y);
        this.width = (int)(40 * weapon.getSize());
        this.height = (int)(40 * weapon.getSize());
    }

    @Override
    protected void updatePhysics() {
        position.add(velocity);
        // Fireballs die after traveling a certain distance
        duration++;
        if (duration > 100) isDead = true;
    }
}