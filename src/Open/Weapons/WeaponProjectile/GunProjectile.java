package Open.Weapons.WeaponProjectile;

import Open.Weapons.Weapon;
import main.GameObject;
import main.Vec2;

public class GunProjectile extends WeaponEntity{

	public GunProjectile(GameObject gameObj, Weapon weapon, Vec2 direction, int x, int y) {
		super(gameObj, weapon, direction, x, y);
		this.width = 50;
		this.height = 50;
		
	}
	
	@Override
	public void updatePhysics() {
		position = position.add(velocity);
	}

}
