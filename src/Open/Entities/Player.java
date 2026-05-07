package Open.Entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EnumMap;

import Open.Artifacts.ArtifactManager;
import Open.Artifacts.Common.ChunkyOats;
import Open.Entities.Enemies.Enemy;
import Open.Weapons.FireStaffWeapon;
import Open.Weapons.Weapon;
import main.Animation;
import main.AppPanel;
import main.Assets;
import main.GameObject;
import main.enums.WeaponTypes;

public class Player extends Entity {
	private ArtifactManager artifactManager;
	private EnumMap<WeaponTypes, Weapon> weapons;

	private int baseMaxHp;

	private int kills;

	private int expNeededToUpgrade = 10;
	private int totalUpgradesAvailible = 1;
	private int currExp;

	private int gold;

	private int invincibilityFrames;
	private final int HIT_DELAY = 30; // ~0.5 sec at 60fps

	private boolean isRight;

	private final int MAX_WEAPONS = 4;

	// Animation
	private BufferedImage[] walkFrames;
	private Animation walkAnim;
	private int frameWidth = 192, frameHeight = 192;

	public Player(GameObject gameObj) {
		super(gameObj);

		setArtifactManager(new ArtifactManager(gameObj));
		getArtifactManager().addArtifact(new ChunkyOats(gameObj));

		// weapons

		weapons = new EnumMap<WeaponTypes, Weapon>(WeaponTypes.class);

		weapons.put(WeaponTypes.FireStaff, new FireStaffWeapon(gameObj));

		baseMaxHp = 10;
		currHp = getMaxHp();

		x = gameObj.getMap().HEIGHT / 2;
		y = gameObj.getMap().WIDTH / 2;

		speed = 6;

		isRight = true;

		height = 70;
		width = 70;

		invincibilityFrames = 0;

		currExp = 100;

		// Load walk frames
		int frameCount = 4;
		walkFrames = new BufferedImage[frameCount];
		for (int i = 0; i < frameCount; i++) {
			walkFrames[i] = Assets.playerSheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
		}
		walkAnim = new Animation(walkFrames, 100);
	}

	public void update() {
		if (currHp <= 0) {
			isDead = true;
			gameObj.setState(gameObj.getStateDead());
		}

		artifactManager.onUpdate();

		updateOpenMovement();

		if (invincibilityFrames > 0) {
			invincibilityFrames--;
		}

		// update weapons
		for (Weapon w : weapons.values()) {
			w.update();
		}

		// leveling
		if (currExp >= expNeededToUpgrade) {
			gameObj.setState(gameObj.getStateUpgrade());
			gameObj.getUpgrades().shuffleUpgrades();
			currExp -= expNeededToUpgrade;
			expNeededToUpgrade *= 1.3;
		}
	}

	public void updateOpenMovement() {

		if (gameObj.getKeyH().isMoving)
			walkAnim.update();
		else
			walkAnim.setFrame(3);

		double dx = 0;
		double dy = 0;

		if (gameObj.getKeyH().up)
			dy -= 1;
		if (gameObj.getKeyH().down)
			dy += 1;
		if (gameObj.getKeyH().left) {
			dx -= 1;
			isRight = false;
		}
		if (gameObj.getKeyH().right) {
			dx += 1;
			isRight = true;
		}

		double length = Math.sqrt(dx * dx + dy * dy);

		if (length > 0) {
			dx /= length;
			dy /= length;

			x += dx * speed;
			y += dy * speed;
		}
	}

	public void damage(int amount) {
		if (invincibilityFrames <= 0) {
			currHp -= amount;
			currHp = Math.max(currHp, 0); // prevent negative HP
			invincibilityFrames = HIT_DELAY;
		}
	}

	public boolean isInvincible() {
		return invincibilityFrames > 0;
	}

	public void draw(Graphics2D g2) {
		// 1. ANCHOR: Offset the screen center so the MIDDLE of the box is the center
		int screenX = (int) ((AppPanel.WIDTH / 2) - (width / 2));
		int screenY = (int) ((AppPanel.HEIGHT / 2) - (height / 2));

		// 2. VISUALS: Scale the art
		int visualW = (int) (width * 1.5);
		int visualH = (int) (height * 1.5);

		// 3. CENTERING ART: Offset the art so it centers on the box
		int drawX = screenX - (visualW - width) / 2;
		int drawY = screenY - (visualH - height) / 2;

		// --- Draw Sprite ---
		if (!(isInvincible() && invincibilityFrames % 6 < 3)) {
			if (isRight) {
				g2.drawImage(walkAnim.getFrame(), drawX, drawY, visualW, visualH, null);
			} else {
				g2.drawImage(walkAnim.getFrame(), drawX + visualW, drawY, -visualW, visualH, null);
			}
		}

		// --- DEBUG: THE RED BOX ---
//		g2.setColor(Color.RED);
//		g2.drawRect(screenX, screenY, width, height);

		// --- UI ---
		artifactManager.draw(g2);
		drawXPBar(g2);
		drawHpBar(g2);
	}

	private void drawXPBar(Graphics2D g2) {
		int barWidth = AppPanel.WIDTH;
		int barHeight = 30;
		int x = 0;
		int y = 0;

		float percent = Math.min(1.0f, (float) currExp / expNeededToUpgrade);

		g2.setColor(new Color(50, 50, 50, 180));
		g2.fillRect(x, y, barWidth, barHeight);

		g2.setColor(new Color(0, 200, 255));
		g2.fillRect(x, y, (int) (barWidth * percent), barHeight);

		g2.setColor(Color.WHITE);
		g2.drawRect(x, y, barWidth, barHeight);

		g2.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
		String text = "Exp: " + currExp + " / " + expNeededToUpgrade;
		int textWidth = g2.getFontMetrics().stringWidth(text);
		g2.drawString(text, x + (barWidth - textWidth) / 2, y + barHeight - 5);
	}

	private void drawHpBar(Graphics2D g2) {
		int barWidth = 200;
		int barHeight = 30;
		int x = (AppPanel.WIDTH - barWidth) / 2;
		int y = AppPanel.HEIGHT - barHeight - 100;

		float percent = Math.min(1.0f, (float) currHp / getMaxHp());

		g2.setColor(new Color(50, 50, 50, 180));
		g2.fillRect(x, y, barWidth, barHeight);

		g2.setColor(new Color(255, 0, 0));
		g2.fillRect(x, y, (int) (barWidth * percent), barHeight);

		g2.setColor(Color.WHITE);
		g2.drawRect(x, y, barWidth, barHeight);

		g2.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
		String text = "Hp: " + currHp + " / " + getMaxHp();
		int textWidth = g2.getFontMetrics().stringWidth(text);
		g2.drawString(text, x + (barWidth - textWidth) / 2, y + barHeight - 8);
	}

	public Enemy closestEnemy(Double double1) {
		ArrayList<Enemy> enemies = gameObj.getEnemies();
		double minDistance = Double.MAX_VALUE;
		Enemy closestEnemy = null;

		for (Enemy e : enemies) {
			int dist = Entity.getDistance(this, e);
			if (dist < double1 && !e.isDying() && dist < minDistance) {
				minDistance = dist;
				closestEnemy = e;
			}
		}

		return closestEnemy;
	}

	public EnumMap<WeaponTypes, Weapon> getWeapons() {
		return weapons;
	}

//	public Set<WeaponTypes> getWeaponSet() {
//		Set<WeaponTypes> weaponSet = new HashSet<WeaponTypes>();
//		
//		for (Weapon w: weapons) {
//			weaponSet.add(w.getWeaponType());
//		}
//		
//		return weaponSet;
//	}

	public int getMaxHp() {
		return (int) ((baseMaxHp + getArtifactManager().getFlatHealth())
				* (1 + getArtifactManager().getPercentHealth()));
	}

	public int getTotalUpgradesAvailible() {
		return totalUpgradesAvailible;
	}

	public int getExpToUpgrade() {
		return expNeededToUpgrade;
	}

	public void addKills(int count) {
		kills += count;
	}

	public void addExp(int i) {
		currExp += (i *  (1 + artifactManager.getPercentBonusExp()));
	}

	public int getKills() {
		return kills;
	}

	public void addWeapon(WeaponTypes type, Weapon w) {
		weapons.put(type, w);
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getMAX_WEAPONS() {
		return MAX_WEAPONS;
	}

	public ArtifactManager getArtifactManager() {
		return artifactManager;
	}

	public void setArtifactManager(ArtifactManager artifactManager) {
		this.artifactManager = artifactManager;
	}
}