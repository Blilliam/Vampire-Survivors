package Open.Upgrades;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.Random;

import main.AppPanel;
import main.GameButton;
import main.GameObject;
import main.MouseInput;
import main.enums.GameState;
import main.enums.WeaponRarity;

public class Upgrades {
	WeaponRarity[] rarities;

	GameObject gameObj;

	// =========================
	// UPGRADE BOX DATA
	// =========================
	UpgradeBox[] totalBoxes;
	int[] currBoxIndex = new int[3];

	final int numberOfTotalBoxes = 8;
	final int numberOfcurrBoxes = 3;

	private boolean upgradeChosen = false; // new

	final int rectWidth = 300;
	final int rectHeight = 450;


	public Upgrades(GameObject gameObj) {

		rarities = new WeaponRarity[4];
		rarities[0] = WeaponRarity.BRONZE;
		rarities[1] = WeaponRarity.SILVER;
		rarities[2] = WeaponRarity.GOLD;
		rarities[3] = WeaponRarity.DIAMOND;

		this.gameObj = gameObj;

		// Create upgrade boxes
		totalBoxes = new UpgradeBox[numberOfTotalBoxes];
		for (int i = 0; i < numberOfTotalBoxes; i++) {
			totalBoxes[i] = new UpgradeBox(gameObj, i + 1, 0, 0);
		}

		randomCurrBox();
	}

	// =========================
	// UPDATE
	// =========================
	public void update() {

		// Only handle **one click per frame**
		if (MouseInput.mousePressed) {

			boolean didUpgrade = false;

			// Only handle boxes if we haven’t exited
			if (!didUpgrade) {
				for (int i = 0; i < numberOfcurrBoxes; i++) {
					UpgradeBox box = totalBoxes[currBoxIndex[i]];

					if (box != null && box.isHovering()) {
						if (gameObj.getPlayer().getTotalUpgradesAvailible() > 0) {
							gameObj.getPlayer().setTotalUpgradesAvailible(gameObj.getPlayer().getTotalUpgradesAvailible() - 1);
							box.upgrade();

							upgradeChosen = true;
							randomCurrBox();
							didUpgrade = true;

							gameObj.getPlayer().setExpToUpgrade((int)(gameObj.getPlayer().getExpToUpgrade() * 1.3));

							break; // stop after first valid box
						}
					}
				}
			}

			// CLEAR mouse click AFTER handling **one action**
			if (didUpgrade) {
				MouseInput.mousePressed = false;
			}
		}

		// Update box animations
		for (int i = 0; i < numberOfcurrBoxes; i++) {
			totalBoxes[currBoxIndex[i]].updateAnimation();
		}
	}

	// =========================
	// DRAW
	// =========================
	public void draw(Graphics2D g2) {

		g2.setFont(new Font("Arial", Font.BOLD, 24));
		FontMetrics fm = g2.getFontMetrics();

		// Background overlay
		g2.setColor(new Color(20, 20, 50, 200));
		g2.fillRect(0, 0, AppPanel.WIDTH, AppPanel.HEIGHT);

		int spacing = (AppPanel.WIDTH - rectWidth * numberOfcurrBoxes) / (numberOfcurrBoxes + 1);
		int y = (AppPanel.HEIGHT - rectHeight) / 2;

		// Title
		g2.setColor(Color.WHITE);
		g2.drawString("UPGRADES", 40, 50);

		// Upgrade boxes
		for (int i = 0; i < numberOfcurrBoxes; i++) {
			UpgradeBox box = totalBoxes[currBoxIndex[i]];
			int x = spacing + i * (rectWidth + spacing);

			box.x = x;
			box.y = y;
			box.w = rectWidth;
			box.h = rectHeight;

			box.draw(g2);
		}
	}

	// =========================
	// RANDOMIZE BOXES (FIXED)
	// =========================
	public void randomCurrBox() {

		Random rand = new Random();
		boolean[] used = new boolean[numberOfTotalBoxes];

		for (int i = 0; i < numberOfcurrBoxes; i++) {
			int r;
			do {
				r = rand.nextInt(numberOfTotalBoxes);
			} while (used[r]);

			used[r] = true;
			currBoxIndex[i] = r;
			totalBoxes[r].startAnimation();
		}
	}

	public boolean hasFinishedUpgrading() {
		return upgradeChosen;
	}

	public void reset() {
		upgradeChosen = false;
	}

}
