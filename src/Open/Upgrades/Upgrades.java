package Open.Upgrades;

import java.awt.Graphics2D;
import java.util.ArrayList;

import Open.Weapons.AuraWeapon;
import Open.Weapons.BananaWeapon;
import Open.Weapons.Weapon;
import main.AppPanel;
import main.GameButton;
import main.GameObject;
import main.enums.WeaponRarity;

public class Upgrades {
	WeaponRarity[] rarities;

	GameObject gameObj;

	private int numberOfcurrBoxes = 3;
	
	private GameButton[] boxes;
	
	private ArrayList<Weapon> allWeapons;

	private boolean chosen;

	private final int rectWidth = 900;
	private final int rectHeight = 200;


	public Upgrades(GameObject gameObj) {
		this.gameObj = gameObj;
		
		allWeapons = new ArrayList<Weapon>(5);
		
//		rarities = new WeaponRarity[4];
//		rarities[0] = WeaponRarity.BRONZE;
//		rarities[1] = WeaponRarity.SILVER;
//		rarities[2] = WeaponRarity.GOLD;
//		rarities[3] = WeaponRarity.DIAMOND;

		allWeapons.add(new BananaWeapon(gameObj));
		
		this.boxes = new GameButton[3];
		
//		boxes[0] = new GameButton(100, 300, 100, 100, "upgrade1", this::dosmth);
//		boxes[1] = new GameButton(100, 500, 100, 100, "upgrade2", this::dosmth);
//		boxes[2] = new GameButton(100, 700, 100, 100, "upgrade3", this::dosmth);
		shuffleUpgrades();
	}
	
	public void shuffleUpgrades() {
		ArrayList<Weapon> weapons = gameObj.getPlayer().getWeapons();
		for (int i = 0; i < boxes.length; i ++) {
			Weapon randomWeapon = allWeapons.get((int)(Math.random() * allWeapons.size() - 1));
			if (weapons.size() < 3 && !(weapons.contains(randomWeapon))) {
				boxes[i] = new GameButton(AppPanel.WIDTH/2,  i * 300, 100, 100, "upgrade1", this::dosmth);
			}
		}
	}
	
	public void draw(Graphics2D g) {
		for (GameButton b: boxes) {
			b.draw(g);
		}
	}
	public void dosmth() {
		gameObj.getPlayer().addWeapon(new AuraWeapon(gameObj));
	}

	public void update() {
		for (GameButton b: boxes) {
			b.update();
		}
		
	}
}
