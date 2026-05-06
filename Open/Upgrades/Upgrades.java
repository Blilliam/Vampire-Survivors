package Open.Upgrades;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import Open.Weapons.AuraWeapon;
import Open.Weapons.BananaWeapon;
import Open.Weapons.BoneWeapon;
import Open.Weapons.FireStaffWeapon;
import Open.Weapons.PewPewWeapon;
import Open.Weapons.SwordWeapon;
import Open.Weapons.Weapon;
import main.AppPanel;
import main.GameButton;
import main.GameObject;
import main.enums.WeaponRarity;
import main.enums.WeaponTypes;
import main.enums.WeaponUpgrades;

public class Upgrades {
	private GameObject gameObj;
	private GameButton[] boxes = new GameButton[3];
	private EnumMap<WeaponTypes, Weapon> allWeapons;
	private EnumMap<WeaponUpgrades, Double> baseUpgradeValue;
	private final int rectWidth = 900;
	private final int rectHeight = 200;

	public Upgrades(GameObject gameObj) {
		this.gameObj = gameObj;
		allWeapons = new EnumMap<>(WeaponTypes.class);
		allWeapons.put(WeaponTypes.Aura, new AuraWeapon(gameObj));
		allWeapons.put(WeaponTypes.Banana, new BananaWeapon(gameObj));
		allWeapons.put(WeaponTypes.Bone, new BoneWeapon(gameObj));
		allWeapons.put(WeaponTypes.FireStaff, new FireStaffWeapon(gameObj));
		allWeapons.put(WeaponTypes.PewPew, new PewPewWeapon(gameObj));
		allWeapons.put(WeaponTypes.Sword, new SwordWeapon(gameObj));
	}

	public void shuffleUpgrades() {
		Random rand = new Random();
		// Get the actual weapon instances the player owns
		EnumMap<WeaponTypes, Weapon> playerWeapons = gameObj.getPlayer().getWeapons();
		Set<WeaponTypes> ownedTypes = playerWeapons.keySet();

		List<WeaponTypes> availableToBuy = new ArrayList<>();
		for (WeaponTypes type : allWeapons.keySet()) {
			if (!ownedTypes.contains(type))
				availableToBuy.add(type);
		}

		for (int i = 0; i < boxes.length; i++) {
			int yPos = 100 + (i * 300);
			boolean canBuy = playerWeapons.size() < gameObj.getPlayer().getMAX_WEAPONS() && !availableToBuy.isEmpty();
			boolean canUpgrade = !ownedTypes.isEmpty();

			if (canBuy && (rand.nextBoolean() || !canUpgrade)) {
				// Logic for buying new weapon...
				WeaponTypes type = availableToBuy.get(rand.nextInt(availableToBuy.size()));
				final WeaponTypes finalType = type;
				boxes[i] = new GameButton(AppPanel.WIDTH / 2 - rectWidth / 2, yPos, rectWidth, rectHeight,
						"NEW: " + type, () -> {
							gameObj.getPlayer().addWeapon(finalType, allWeapons.get(finalType));
							finishUpgrade();
						});
			} else if (canUpgrade) {
				// Logic for upgrading OWNED weapons
				List<WeaponTypes> ownedList = new ArrayList<>(ownedTypes);
				WeaponTypes typeToUpgrade = ownedList.get(rand.nextInt(ownedList.size()));

				// CRITICAL FIX: Use the weapon instance from the player's inventory
				Weapon actualWeaponInstance = playerWeapons.get(typeToUpgrade);

				WeaponUpgrades stat = getRandomStatForWeapon(actualWeaponInstance);
				WeaponRarity rarity = WeaponRarity.values()[rand.nextInt(WeaponRarity.values().length)];

				final Weapon finalWeapon = actualWeaponInstance;
				final WeaponUpgrades finalStat = stat;
				final WeaponRarity finalRarity = rarity;

				boxes[i] = new GameButton(AppPanel.WIDTH / 2 - rectWidth / 2, yPos, rectWidth, rectHeight,
						finalRarity + " " + finalStat + " : " + typeToUpgrade, () -> {
							applyUpgradeMath(finalWeapon, finalStat, finalRarity);
							finishUpgrade();
						});
			}
		}
	}

	private WeaponUpgrades getRandomStatForWeapon(Weapon w) {
		// Get the keys directly from the specific weapon's stats map
		Set<WeaponUpgrades> availableStats = w.getStats().keySet();

		// Convert to array to pick a random one
		WeaponUpgrades[] statArray = availableStats.toArray(new WeaponUpgrades[0]);

		if (statArray.length == 0)
			return WeaponUpgrades.AttackDamage; // Fallback

		return statArray[new Random().nextInt(statArray.length)];
	}

	private void applyUpgradeMath(Weapon w, WeaponUpgrades stat, WeaponRarity rarity) {
		double current = w.getStats().getOrDefault(stat, 0.0);
		switch (stat) {
		case AttackDamage -> w.getStats().put(stat, current + (5.0 * rarity.multiplier));
		case AttackSpeed -> w.getStats().put(stat, current * (1.0 - rarity.speedReduction));
		case AttackSize -> w.getStats().put(stat, current + rarity.sizeIncrease);
		default -> w.getStats().put(stat, current + rarity.multiplier);
		}
	}

	private void finishUpgrade() {
		gameObj.setState(gameObj.getStateOpen());
	}

	public void update() {
		for (GameButton b : boxes)
			if (b != null)
				b.update();
	}

	public void draw(Graphics2D g) {
		for (GameButton b : boxes)
			if (b != null)
				b.draw(g);
	}
}