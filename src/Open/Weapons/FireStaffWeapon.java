package Open.Weapons;

import main.Assets;
import main.GameObject;

public class FireStaffWeapon extends GunWeapon{

	public FireStaffWeapon(GameObject gameObj) {
		super(gameObj);
		setAtk(10);
		setSpeed(5);
		//setSprite(Assets.);
		this.icon = Assets.FireStaffIcon;
	}

}
