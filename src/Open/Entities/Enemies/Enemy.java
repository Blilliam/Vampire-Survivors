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

	// animation / images
	private BufferedImage[] walkFrames;
	private BufferedImage[] deathFrames;

	private int frame = 0;
	private int frameCounter = 0;

	private int deathHoldTimer = 0;
	private boolean dying = false;

	private int deathX, deathY; // store position when dying

	private int damageFlashTimer = 0;
	private final int FLASH_DURATION = 6;

	public Enemy(GameObject gameObj, int x, int y, int teir) {
		super(gameObj);

		maxHp = 10;

		// random image
		int type = (int) (Math.random() * 5) + 1;
		loadEnemy(type);

		this.x = x;
		this.y = y;

		width = 100;
		height = 100;
		speed = 4;

		currHp = maxHp;

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
			speed--;
			maxHp += 5;
		}
		case 2 -> {
			walkFrames = Assets.skeletonWalk;
			deathFrames = Assets.skeletonDeath;
			speed--;
		}
		case 3 -> {
			walkFrames = Assets.mudmanWalk;
			deathFrames = Assets.mudmanDeath;
			maxHp += 10;
		}
		case 4 -> {
			walkFrames = Assets.batWalk;
			deathFrames = Assets.batDeath;
			speed += 2;
			maxHp -= 3;
		}
		case 5 -> {
			walkFrames = Assets.glowingBatWalk;
			deathFrames = Assets.glowingBatDeath;
			speed++;
			maxHp += 20;
		}
		}
	}

	/**
	 * damages enemy
	 */
	public void damage(double atk) {
		if (isDying())
			return;

		currHp -= atk;

		damageFlashTimer = FLASH_DURATION;

		if (currHp <= 0) {
			currHp = 0;
			die();
		}
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

	public void update() {
		frameCounter++;

		if (damageFlashTimer > 0) {
			damageFlashTimer--;
		}

		if (!isDying()) {
			followPlayer();

			if (frameCounter > 6) {
				frame = (frame + 1) % walkFrames.length;
				frameCounter = 0;
			}

			if (Entity.rectCollision(this, gameObj.getPlayer())) {
				gameObj.getPlayer().damadge(1);
			}

		} else {
			if (frameCounter > 6) {
				if (frame < deathFrames.length - 1) {
					frame++;
				} else {
					deathHoldTimer++;
				}
				frameCounter = 0;
			}

			if (deathHoldTimer > 10) {
				isDead = true;
				
			}
		}
	}

	private void followPlayer() {
		double dx = gameObj.getPlayer().getX() - getX();
		double dy = gameObj.getPlayer().getY() - getY();

		double dist = Math.sqrt(dx * dx + dy * dy);

		if (dist > 0) {
			double moveX = (dx / dist) * speed;
			double moveY = (dy / dist) * speed;

			x += moveX * 0.5 + (Math.random() - 0.5) * 0.3;
			y += moveY * 0.5 + (Math.random() - 0.5) * 0.3;
		}
	}

	public void draw(Graphics2D g) {

		BufferedImage img;
		int drawX, drawY, drawW, drawH;

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

		// draw base image
		g.drawImage(img, drawX, drawY, drawW, drawH, null);

		if (damageFlashTimer > 0) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.5f));
			g.setColor(Color.RED);
			g.fillRect(drawX, drawY, drawW, drawH);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		}
	}

	public boolean isDying() {
		return dying;
	}

	public void setDying(boolean dying) {
		this.dying = dying;
	}
}