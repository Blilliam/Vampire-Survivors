package Open.Weapons;

import java.awt.Graphics2D;

import Open.Weapons.WeaponProjectile.AuraProjectile;
import Open.Weapons.WeaponProjectile.BananaProjectile;
import main.Assets;
import main.GameObject;
import main.Vec2;
import main.enums.WeaponTypes;
import main.enums.WeaponUpgrades;

public class AuraWeapon extends Weapon{
	boolean created;

	public AuraWeapon(GameObject gameObj) {
		super(gameObj, WeaponTypes.Aura);
		stats.put(WeaponUpgrades.AttackDamage, (double) 3);
		stats.put(WeaponUpgrades.ProjectileCount, (double) 1);
		stats.put(WeaponUpgrades.AttackSize, (double) 1);
		stats.put(WeaponUpgrades.AttackSpeed, (double) 20);
		stats.put(WeaponUpgrades.Range, (double) 500);
		stats.put(WeaponUpgrades.CriticalDamage, (double) 2);
		stats.put(WeaponUpgrades.CriticalChance, (double) 0.1);
		this.icon = Assets.AuraIcon;
		created = false;
	}
	
	public void update() {
		if (created == false) {
			gameObj.addProjectiles(new AuraProjectile(gameObj, this));
			created = true;
		}
	}

}