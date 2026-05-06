package Open.Weapons;

import Open.Weapons.WeaponProjectile.BananaProjectile;
import main.Assets;
import main.GameObject;
import main.Vec2;
import main.enums.WeaponTypes;
import main.enums.WeaponUpgrades;

public class BananaWeapon extends Weapon{

	public BananaWeapon(GameObject gameObj) {
		super(gameObj, WeaponTypes.Banana);
		setSprite(Assets.ProjectileBanana);
		this.icon = Assets.BananaIcon;
		stats.put(WeaponUpgrades.AttackDamage, (double) 10); // 3
		stats.put(WeaponUpgrades.ProjectileCount, (double) 3);
		stats.put(WeaponUpgrades.AttackSize, (double) 1);
		stats.put(WeaponUpgrades.AttackSpeed, (double) 60);
		stats.put(WeaponUpgrades.ProjectileSpeed, (double) 5);
		stats.put(WeaponUpgrades.Range, (double) 500);
		stats.put(WeaponUpgrades.Duration, (double) 200);
		stats.put(WeaponUpgrades.ProjectileRotationSpeed, (double) 0.2);
		stats.put(WeaponUpgrades.CriticalDamage, (double) 2);
		stats.put(WeaponUpgrades.CriticalChance, (double) 0.1);
		
		

		delayCounter = 0.0;
	}
	

	public void update() {
		if (stats.get(WeaponUpgrades.AttackSpeed) * (1 - gameObj.getPlayer().getArtifactManager().getPercentAttackSpeed()) < delayCounter) {
			if (gameObj.getPlayer().closestEnemy((getStats().get(WeaponUpgrades.Range))) != null) {
				for (int i = 0; i < stats.get(WeaponUpgrades.ProjectileCount); i ++) {
					Vec2 direction = Vec2.between(gameObj.getPlayer(), gameObj.getPlayer().closestEnemy(getStats().get(WeaponUpgrades.Range)));
					gameObj.addProjectiles(new BananaProjectile(gameObj, this, direction, gameObj.getPlayer().getX(), gameObj.getPlayer().getY()));
				}
				delayCounter = 0.0;
			}
			
		}
		
		delayCounter++;
	}
	
}