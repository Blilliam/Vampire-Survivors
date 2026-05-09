package Open.Entities.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.AlphaComposite;
import java.awt.Color;
import Open.Entities.Entity;
import main.AppPanel;
import main.Assets;
import main.GameObject;

public class Enemy extends Entity {

	private BufferedImage[] walkFrames;
	private BufferedImage[] deathFrames;

	private int frame = 0;
	private int frameCounter = 0;
	private int deathHoldTimer = 0;
	private boolean dying = false;

	// Spawn Animation Variables
	private int spawnInTimer = 60; // 1 second at 60fps
	private final int MAX_SPAWN_TIME = 60;

	private int deathX, deathY;
	private int damageFlashTimer = 0;
	private final int FLASH_DURATION = 6;
	private int atk;

	public Enemy(GameObject gameObj, int x, int y, int type, double statMultiplier) {
		super(gameObj);
		this.x = x;
		this.y = y;
		this.width = 100;
		this.height = 100;

		loadEnemy(type);

		// Apply Scaling
		this.maxHp = (int) (this.maxHp * statMultiplier);
		this.atk = (int) (this.atk * statMultiplier);
		this.speed = (int) (this.speed * (1.0 + (statMultiplier - 1.0) * 0.2));

		this.currHp = maxHp;

		if (walkFrames == null || walkFrames.length == 0)
			walkFrames = new BufferedImage[1];
		if (deathFrames == null || deathFrames.length == 0)
			deathFrames = new BufferedImage[1];
	}

	private void loadEnemy(int num) {
		switch (num) {
		case 1 -> {
			walkFrames = Assets.zombieWalk;
			deathFrames = Assets.zombieDeath;
			speed = 5;
			maxHp = 15;
			atk = 10;
		}
		case 2 -> {
			walkFrames = Assets.skeletonWalk;
			deathFrames = Assets.skeletonDeath;
			speed = 4;
			maxHp = 12;
			atk = 12;
		}
		case 3 -> {
			walkFrames = Assets.mudmanWalk;
			deathFrames = Assets.mudmanDeath;
			speed = 3;
			maxHp = 40;
			atk = 20;
		}
		case 4 -> {
			walkFrames = Assets.batWalk;
			deathFrames = Assets.batDeath;
			speed = 7;
			maxHp = 8;
			atk = 5;
		}
		case 5 -> {
			walkFrames = Assets.glowingBatWalk;
			deathFrames = Assets.glowingBatDeath;
			speed = 8;
			maxHp = 35;
			atk = 25;
		}
		}
	}

	public void update() {
		// 1. Handle Spawning State
		if (spawnInTimer > 0) {
			spawnInTimer--;
			return; // Don't move or attack while spawning
		}

		// 2. Normal Update Logic
		frameCounter++;
		if (damageFlashTimer > 0)
			damageFlashTimer--;

		if (!isDying()) {
			followPlayer();
			if (frameCounter > 6) {
				frame = (frame + 1) % walkFrames.length;
				frameCounter = 0;
			}
			if (Entity.rectCollision(this, gameObj.getPlayer())) {
				gameObj.getPlayer().damage(atk);
			}
		} else {
			updateDeathAnimation();
		}
	}

	private void updateDeathAnimation() {
		if (frameCounter > 6) {
			if (frame < deathFrames.length - 1)
				frame++;
			else
				deathHoldTimer++;
			frameCounter = 0;
		}
		if (deathHoldTimer > 10)
			isDead = true;
	}

	public void draw(Graphics2D g) {
		BufferedImage img;
		int drawX, drawY, drawW, drawH;

		// Position Logic
		if (isDying()) {
			img = deathFrames[Math.min(frame, deathFrames.length - 1)];
			drawX = deathX - gameObj.getPlayer().getX() + AppPanel.WIDTH / 2 - width / 2;
			drawY = deathY - gameObj.getPlayer().getY() + AppPanel.HEIGHT / 2 - height / 2;
			drawW = width + 50;
			drawH = height + 50;
		} else {
			img = walkFrames[Math.min(frame, walkFrames.length - 1)];
			drawX = x - gameObj.getCameraX() - (width - 20) / 2;
			drawY = y - gameObj.getCameraY() - (height - 40) / 2;
			drawW = width;
			drawH = height;
		}

		if (img == null)
			return;

		// --- SPAWN ANIMATION EFFECT ---
		if (spawnInTimer > 0) {
			float percent = 1.0f - ((float) spawnInTimer / MAX_SPAWN_TIME);

			// Fading in
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, percent));

			// "Growing" or "Rising" effect
			int spawnW = (int) (drawW * percent);
			int spawnH = (int) (drawH * percent);
			int centerX = drawX + (drawW / 2);
			int centerY = drawY + (drawH / 2);

			g.drawImage(img, centerX - (spawnW / 2), centerY - (spawnH / 2), spawnW, spawnH, null);

			// Reset composite for other draws
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
			return; // Skip rest of draw
		}

		// --- NORMAL DRAWING (Flash and Health) ---
		if (damageFlashTimer > 0) {
			drawFlash(g, img, drawX, drawY, drawW, drawH);
		} else {
			g.drawImage(img, drawX, drawY, drawW, drawH, null);
		}

		if (!isDying() && currHp < maxHp)
			drawHealthBar(g, drawX, drawY);
	}

	private void drawFlash(Graphics2D g, BufferedImage img, int x, int y, int w, int h) {
		BufferedImage tintImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D gTint = tintImage.createGraphics();
		gTint.drawImage(img, 0, 0, null);
		gTint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.7f));
		gTint.setColor(Color.RED);
		gTint.fillRect(0, 0, img.getWidth(), img.getHeight());
		gTint.dispose();
		g.drawImage(tintImage, x, y, w, h, null);
	}

	private void drawHealthBar(Graphics2D g, int screenX, int screenY) {
		int barWidth = 40, barHeight = 5, barOffset = 10;
		int xPos = screenX + (width / 2) - (barWidth / 2);
		int yPos = screenY - barOffset;
		g.setColor(Color.BLACK);
		g.fillRect(xPos - 1, yPos - 1, barWidth + 2, barHeight + 2);
		g.setColor(Color.RED);
		g.fillRect(xPos, yPos, barWidth, barHeight);
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, (int) (barWidth * ((double) currHp / maxHp)), barHeight);
	}

	public void damage(double atk) {
		if (isDying() || spawnInTimer > 0)
			return; // Invulnerable while spawning
		currHp -= atk;
		damageFlashTimer = FLASH_DURATION;
		if (currHp <= 0)
			die();
	}

	private void die() {
		setDying(true);
		frame = 0;
		frameCounter = 0;
		deathHoldTimer = 0;
		gameObj.addExp(1, x, y);
		gameObj.getPlayer().addKills(1);
		deathX = x;
		deathY = y;
	}

	private void followPlayer() {
		double dx = gameObj.getPlayer().getX() - getX();
		double dy = gameObj.getPlayer().getY() - getY();
		double dist = Math.sqrt(dx * dx + dy * dy);
		if (dist > 0) {
			x += (dx / dist) * speed * 0.5 + (Math.random() - 0.5) * 0.3;
			y += (dy / dist) * speed * 0.5 + (Math.random() - 0.5) * 0.3;
		}
	}

	public boolean isDying() {
		return dying;
	}

	public void setDying(boolean dying) {
		this.dying = dying;
	}
}