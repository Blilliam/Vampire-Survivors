package Open.Entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Open.Artifacts.Artifact;
import Open.Entities.Enemies.Enemy;
import Open.Weapons.AuraWeapon;
import Open.Weapons.BananaWeapon;
import Open.Weapons.GunWeapon;
import Open.Weapons.Weapon;
import main.Animation;
import main.AppPanel;
import main.Assets;
import main.GameObject;

public class Player extends Entity {
	private ArrayList<Artifact> artifacts = new ArrayList<>();
	private ArrayList<Weapon> weapons;

	private int kills;

	private int expNeededToUpgrade = 10;
	private int totalUpgradesAvailible = 1;
	private int currExp;
	
	private int gold;

	private int invincibilityFrames;
	private final int HIT_DELAY = 30; // ~0.5 sec at 60fps

	private boolean isRight;

	// Animation
	private BufferedImage[] walkFrames;
	private Animation walkAnim;
	private int frameWidth = 192, frameHeight = 192;

	public Player(GameObject gameObj) {
		super(gameObj);

		// weapons
		
		weapons = new ArrayList<Weapon>();

		weapons.add(new BananaWeapon(gameObj));
		weapons.add(new GunWeapon(gameObj));
		// weapons[1] = new GunWeapon(gameObj);
		// weapons[2] = new AuraWeapon(gameObj);

		maxHp = 10;
		currHp = maxHp;

		x = gameObj.getMap().HEIGHT / 2;
		y = gameObj.getMap().WIDTH / 2;

		speed = 6;

		isRight = true;

		height = 100;
		width = 100;

		invincibilityFrames = 0;
		
		//temp
		//currExp = 10;
		currExp = 0;

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

		updateOpenMovement();

		if (invincibilityFrames > 0) {
			invincibilityFrames--;
		}

		// update weapons
		for (Weapon w : weapons) {
			w.update();
		}

		// leveling
		if (currExp >= expNeededToUpgrade) {
			gameObj.setState(gameObj.getStateUpgrade());
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
		int drawX = AppPanel.WIDTH / 2 - 50;
		int drawY = AppPanel.HEIGHT / 2 - 50;

		if (isInvincible() && invincibilityFrames % 6 < 3) {
			return;
		}

		if (isRight)
			g2.drawImage(walkAnim.getFrame(), drawX, drawY, width, height, null);
		else
			g2.drawImage(walkAnim.getFrame(), drawX + width, drawY, -100, height, null);

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

		float percent = Math.min(1.0f, (float) currHp / maxHp);

		g2.setColor(new Color(50, 50, 50, 180));
		g2.fillRect(x, y, barWidth, barHeight);

		g2.setColor(new Color(255, 0, 0));
		g2.fillRect(x, y, (int) (barWidth * percent), barHeight);

		g2.setColor(Color.WHITE);
		g2.drawRect(x, y, barWidth, barHeight);

		g2.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
		String text = "Hp: " + currHp + " / " + maxHp;
		int textWidth = g2.getFontMetrics().stringWidth(text);
		g2.drawString(text, x + (barWidth - textWidth) / 2, y + barHeight - 8);
	}

	public Enemy closestEnemy(int range) {
		ArrayList<Enemy> enemies = gameObj.getEnemies();
		float minDistance = Integer.MAX_VALUE;
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

	public ArrayList<Weapon> getWeapons() {
		return weapons;
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
		currExp += i;
	}

	public int getKills() {
		return kills;
	}
	public void addWeapon(Weapon w) {
		weapons.add(w);
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}
}