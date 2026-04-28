package Open.Upgrades;

import main.GameObject;
import main.enums.WeaponTypes;

public class UpgradeOption {
	GameObject gameObj;
	WeaponTypes type;
	double upgradeWeaponType;
	public UpgradeOption(GameObject gameObj) {
		this.gameObj = gameObj;
	}
}
