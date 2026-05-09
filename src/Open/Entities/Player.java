package Open.Entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import Open.Artifacts.ArtifactManager;
import Open.Artifacts.Common.ChunkyOats;
import Open.Entities.Enemies.Enemy;
import Open.Upgrades.Book;
import Open.Weapons.SwordWeapon;
import Open.Weapons.Weapon;
import main.Animation;
import main.AppPanel;
import main.Assets;
import main.GameObject;
import main.enums.WeaponTypes;

public class Player extends Entity {
	private ArtifactManager artifactManager;
	private EnumMap<WeaponTypes, Weapon> weapons;

	// --- Book and Passive System ---
	private Map<String, Book> ownedBooks = new HashMap<>();
	private final int MAX_BOOKS = 4;

	private int baseMaxHp;
	private int kills;
	private int expNeededToUpgrade = 10;
	private int currExp;
	private int gold;
	private int invincibilityFrames;
	private final int HIT_DELAY = 30;

	private boolean isRight;
	private final int MAX_WEAPONS = 4;

	private BufferedImage[] walkFrames;
	private Animation walkAnim;
	private int frameWidth = 192, frameHeight = 192;

	public Player(GameObject gameObj) {
		super(gameObj);
		setArtifactManager(new ArtifactManager(gameObj));
		getArtifactManager().addArtifact(new ChunkyOats(gameObj));

		weapons = new EnumMap<WeaponTypes, Weapon>(WeaponTypes.class);
		weapons.put(WeaponTypes.Sword, new SwordWeapon(gameObj));

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

		for (Weapon w : weapons.values()) {
			w.update();
		}

		if (currExp >= expNeededToUpgrade) {
			gameObj.getUpgrades().shuffleUpgrades();
			gameObj.setState(gameObj.getStateUpgrade());
			currExp -= expNeededToUpgrade;
			expNeededToUpgrade *= 1.3;
		}
	}

	// --- Book Integration Logic ---
	public void addOrUpgradeBook(Book newBook) {
		// If we already have it, we replace it with the upgraded version (higher value)
		ownedBooks.put(newBook.getName(), newBook);

		// Special case: Max HP Book provides an instant heal for the amount gained
		if (newBook.getName().equals("Max HP Book")) {
			currHp += (int) newBook.getValue();
		}
	}

	public double getStatBonus(String bookName) {
		Book b = ownedBooks.get(bookName);
		if (b != null) {
			return b.getValue();
		}
		return 0.0;
	}

	@Override
	public int getMaxHp() {
		double bookBonus = getStatBonus("Max HP Book");
		return (int) ((baseMaxHp + getArtifactManager().getFlatHealth() + bookBonus)
				* (1 + getArtifactManager().getPercentHealth()));
	}

	public void addExp(int i) {
		double bookBonus = getStatBonus("EXP Book") / 100.0;
		currExp += (i * (1 + artifactManager.getPercentBonusExp() + bookBonus));
	}

	// --- Original Movement and Combat Methods ---
	public void updateOpenMovement() {
		if (gameObj.getKeyH().isMoving)
			walkAnim.update();
		else
			walkAnim.setFrame(3);

		double dx = 0, dy = 0;
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
			currHp = Math.max(currHp, 0);
			invincibilityFrames = HIT_DELAY;
		}
	}

	public boolean isInvincible() {
		return invincibilityFrames > 0;
	}

	public void draw(Graphics2D g2) {
		int screenX = (int) ((AppPanel.WIDTH / 2) - (width / 2));
		int screenY = (int) ((AppPanel.HEIGHT / 2) - (height / 2));
		int visualW = (int) (width * 1.5);
		int visualH = (int) (height * 1.5);
		int drawX = screenX - (visualW - width) / 2;
		int drawY = screenY - (visualH - height) / 2;

		if (!(isInvincible() && invincibilityFrames % 6 < 3)) {
			if (isRight) {
				g2.drawImage(walkAnim.getFrame(), drawX, drawY, visualW, visualH, null);
			} else {
				g2.drawImage(walkAnim.getFrame(), drawX + visualW, drawY, -visualW, visualH, null);
			}
		}
		artifactManager.draw(g2);
		drawXPBar(g2);
		drawHpBar(g2);
	}

	private void drawXPBar(Graphics2D g2) {
		int barWidth = AppPanel.WIDTH;
		int barHeight = 30;
		float percent = Math.min(1.0f, (float) currExp / expNeededToUpgrade);
		g2.setColor(new Color(50, 50, 50, 180));
		g2.fillRect(0, 0, barWidth, barHeight);
		g2.setColor(new Color(0, 200, 255));
		g2.fillRect(0, 0, (int) (barWidth * percent), barHeight);
		g2.setColor(Color.WHITE);
		g2.drawRect(0, 0, barWidth, barHeight);
		g2.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
		String text = "Exp: " + currExp + " / " + expNeededToUpgrade;
		g2.drawString(text, (barWidth - g2.getFontMetrics().stringWidth(text)) / 2, barHeight - 5);
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
		g2.drawString(text, x + (barWidth - g2.getFontMetrics().stringWidth(text)) / 2, y + barHeight - 8);
	}

	public Enemy closestEnemy(Double range) {
		ArrayList<Enemy> enemies = gameObj.getEnemies();
		double minDistance = Double.MAX_VALUE;
		Enemy closestEnemy = null;
		for (Enemy e : enemies) {
			int dist = Entity.getDistance(this, e);
			if (dist < range && !e.isDying() && dist < minDistance) {
				minDistance = dist;
				closestEnemy = e;
			}
		}
		return closestEnemy;
	}

	// --- Getters and Setters ---
	public Map<String, Book> getOwnedBooks() {
		return ownedBooks;
	}

	public int getMAX_BOOKS() {
		return MAX_BOOKS;
	}

	public EnumMap<WeaponTypes, Weapon> getWeapons() {
		return weapons;
	}

	public void addWeapon(WeaponTypes type, Weapon w) {
		weapons.put(type, w);
	}

	public int getMAX_WEAPONS() {
		return MAX_WEAPONS;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getKills() {
		return kills;
	}

	public void addKills(int count) {
		kills += count;
	}

	public ArtifactManager getArtifactManager() {
		return artifactManager;
	}

	public void setArtifactManager(ArtifactManager am) {
		this.artifactManager = am;
	}
}