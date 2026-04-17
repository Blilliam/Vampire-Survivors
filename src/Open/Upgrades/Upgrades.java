package Open.Upgrades;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import main.AppPanel;
import main.GameObject;
import main.MouseInput;
import main.enums.WeaponRarity;

public class Upgrades {
	WeaponRarity[] rarities;

	GameObject gameObj;

	private int numberOfcurrBoxes = 3;
	
	private UpgradeBox[] boxes;

	private boolean chosen;

	private final int rectWidth = 900;
	private final int rectHeight = 200;


	public Upgrades(GameObject gameObj) {

		rarities = new WeaponRarity[4];
		rarities[0] = WeaponRarity.BRONZE;
		rarities[1] = WeaponRarity.SILVER;
		rarities[2] = WeaponRarity.GOLD;
		rarities[3] = WeaponRarity.DIAMOND;

		this.gameObj = gameObj;
		
		this.boxes = new UpgradeBox[3];
	}

	// =========================
	// UPDATE
	// =========================
	public void update() {

		gameObj.getMouseHandler();
		// Only handle **one click per frame**
		if (MouseInput.isMousePressed()) {

			boolean didUpgrade = false;

			// Only handle boxes if we haven’t exited
			if (!didUpgrade) {
				for (int i = 0; i < numberOfcurrBoxes; i++) {
					UpgradeBox box = new UpgradeBox(gameObj, rarities[(int) (Math.random() * rarities.length) + 1], gameObj.getPlayer().getWeapons()[(int)(Math.random()*gameObj.getPlayer().getWeapons().length) + 1], 200, i * 300 + 300);
					boxes[i] = box;
					if (box != null && box.isHovering()) {
						if (gameObj.getPlayer().getTotalUpgradesAvailible() > 0) {
							//gameObj.getPlayer().setTotalUpgradesAvailible(gameObj.getPlayer().getTotalUpgradesAvailible() - 1);
							box.upgrade();

							chosen = true;
							randomCurrBox();
							didUpgrade = true;

							//gameObj.getPlayer().setExpToUpgrade((int)(gameObj.getPlayer().getExpToUpgrade() * 1.3));

							break; // stop after first valid box
						}
					}
				}
			}

			// CLEAR mouse click AFTER handling **one action**
			if (didUpgrade) {
				MouseInput.setMousePressed(false);
			}
		}

		// Update box animations
		for (int i = 0; i < numberOfcurrBoxes; i++) {
			//totalBoxes[currBoxIndex[i]].updateAnimation();
		}
	}

	// DRAW
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
			UpgradeBox box = boxes[i];
			int x = spacing + i * (rectWidth + spacing);

			box.x = x;
			box.y = y;
			box.w = rectWidth;
			box.h = rectHeight;

			box.draw(g2);
		}
	}


	// RANDOMIZE BOXES 
	public void randomCurrBox() {
//		Random rand = new Random();
//		boolean[] used = new boolean[numberOfTotalBoxes];
//
//		for (int i = 0; i < numberOfcurrBoxes; i++) {
//			int r;
//			do {
//				r = rand.nextInt(numberOfTotalBoxes);
//			} while (used[r]);
//
//			used[r] = true;
//			currBoxIndex[i] = r;
//			totalBoxes[r].startAnimation();
//		}
	}

	public boolean hasFinishedUpgrading() {
		return chosen;
	}

	public void reset() {
		chosen = false;
	}

}
