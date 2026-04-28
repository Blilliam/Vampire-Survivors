package Open.Upgrades;

import java.awt.Graphics2D;
import java.util.EnumSet;
import java.util.Set;

import Open.Weapons.AuraWeapon;
import Open.Weapons.BananaWeapon;
import Open.Weapons.BoneWeapon;
import Open.Weapons.FireStaffWeapon;
import Open.Weapons.PewPewWeapon;
import Open.Weapons.SwordWeapon;
import main.AppPanel;
import main.GameButton;
import main.GameObject;
import main.enums.WeaponRarity;
import main.enums.WeaponTypes;

public class Upgrades {
	WeaponRarity[] rarities;

	GameObject gameObj;

	private int numberOfcurrBoxes = 3;
	
	private GameButton[] boxes;

	private boolean chosen;
	
	private Set<WeaponTypes> allWeapons;
	
	private Set<WeaponTypes> notOwnedWeapons;

	private final int rectWidth = 900;
	private final int rectHeight = 200;
	
	


	public Upgrades(GameObject gameObj) {
		this.gameObj = gameObj;
		
		allWeapons = EnumSet.allOf(WeaponTypes.class);
		
//		rarities = new WeaponRarity[4];
//		rarities[0] = WeaponRarity.BRONZE;
//		rarities[1] = WeaponRarity.SILVER;
//		rarities[2] = WeaponRarity.GOLD;
//		rarities[3] = WeaponRarity.DIAMOND;
		
		this.boxes = new GameButton[3];
		
		boxes[0] = new GameButton(AppPanel.WIDTH/2 - rectWidth/2, 100, rectWidth, rectHeight, "NEW: Aura", this::dosmth);
		boxes[1] = new GameButton(AppPanel.WIDTH/2 - rectWidth/2, 400, rectWidth, rectHeight, "upgrade2", this::dosmth);
		boxes[2] = new GameButton(AppPanel.WIDTH/2 - rectWidth/2, 700, rectWidth, rectHeight, "upgrade3", this::dosmth);
//		shuffleUpgrades();
	}
	
	public UpgradeOption shuffleUpgrades() {
		UpgradeOption output = new UpgradeOption(gameObj);
		notOwnedWeapons = EnumSet.allOf(WeaponTypes.class);
		notOwnedWeapons.remove(gameObj.getPlayer().getWeaponSet());

		for (int i = 0; i < boxes.length; i ++) {
			double prob = 0.5;
			if (gameObj.getPlayer().getMAX_WEAPONS() < gameObj.getPlayer().getWeapons().size()) {
				prob = 0;
			}
			if (gameObj.getPlayer().getWeapons().size() == 0) {
				prob = 1;
			}
			if (Math.random() < prob) {
				//make weapon
				WeaponTypes randomWeaponType = getRandomWeaponType(allWeapons);
				addWeaponToPlayer(randomWeaponType);
			} else {
				//upgrade
			}
			
//			if (weapons.size() < 3 && !(weapons.contains(randomWeapon))) {
//				boxes[i] = new GameButton(AppPanel.WIDTH/2,  i * 300, 100, 100, "upgrade1", this::dosmth);
//			}
		}
		return output;
	}
	
	public void addWeaponToPlayer(WeaponTypes type) {
		notOwnedWeapons.remove(type);
		switch (type) {
		case WeaponTypes.Aura:
			gameObj.getPlayer().addWeapon(new AuraWeapon(gameObj));
			break;
		case WeaponTypes.Banana:
			gameObj.getPlayer().addWeapon(new BananaWeapon(gameObj));
			break;
		case WeaponTypes.Bone:
			gameObj.getPlayer().addWeapon(new BoneWeapon(gameObj));
			break;
		case WeaponTypes.FireStaff:
			gameObj.getPlayer().addWeapon(new FireStaffWeapon(gameObj));
			break;
		case WeaponTypes.Sword:
			gameObj.getPlayer().addWeapon(new SwordWeapon(gameObj));
			break;
		case WeaponTypes.PewPew:
			gameObj.getPlayer().addWeapon(new PewPewWeapon(gameObj));
			break;
		default:
			return;
		}
	}
	
	public WeaponTypes getRandomWeaponType(Set<WeaponTypes> allWeapons) {
		int randIndex = (int)(Math.random() * allWeapons.size());
		
		int i = 0;
		for (WeaponTypes type : allWeapons) {
			if (i == randIndex) {
				return type;
			}
			i++;
		}
		return null;
	}
	
	public void draw(Graphics2D g) {
		for (GameButton b: boxes) {
			b.draw(g);
		}
	}
	public void dosmth() {
		gameObj.getPlayer().addWeapon(new AuraWeapon(gameObj));
		gameObj.setState(gameObj.getStateOpen());
	}

	public void update() {
		for (GameButton b: boxes) {
			b.update();
		}
		
	}
}
