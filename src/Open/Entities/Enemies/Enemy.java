package Open.Entities.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Open.Entities.Entity;
import main.AppPanel;
import main.Assets;
import main.GameObject;

public class Enemy extends Entity {

	// anamation / imates
	private BufferedImage[] walkFrames;
	private BufferedImage[] deathFrames;

	private int frame = 0;
	private int frameCounter = 0;

	private int deathHoldTimer = 0;
	private boolean dying = false;

	private int deathX, deathY; // store position when dying

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

		// sets hp stuff
		if (walkFrames == null || walkFrames.length == 0)
			walkFrames = new BufferedImage[1];
		if (deathFrames == null || deathFrames.length == 0)
			deathFrames = new BufferedImage[1];
	}

	private void loadEnemy(int num) {
		// returns image array and changes some values
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
	 * 
	 * @param amount of damages
	 */
	public void damage(int amount) {
		if (dying)
			return;

		currHp -= amount;

		if (currHp <= 0) {
			die();
		}
	}

	/**
	 * starts death animation and freezes them in place
	 */
	private void die() {
		dying = true;
		frame = 0;
		frameCounter = 0;
		deathHoldTimer = 0;

		// Freeze position for death animation
		deathX = x;
		deathY = y;
	}

	/**
	 * updates teh enemy
	 */
	public void update() {
		frameCounter++;

		if (!dying) {
			followPlayer();

			if (frameCounter > 6) {
				frame = (frame + 1) % walkFrames.length;
				frameCounter = 0;
			}

		} else {
			// DEATH LOGIC
			if (frameCounter > 6) {
				if (frame < deathFrames.length - 1) {
					frame++; // Advance the animation
				} else {
					deathHoldTimer++; // Animation finished, now hold the corpse
				}
				frameCounter = 0;
			}

			if (deathHoldTimer > 10) { // Adjust this for how long the body stays
				isDead = true;
				gameObj.addExp(1, x, y); // change this later
				gameObj.getPlayer().addKills(1);
			}
		}
	}

	private void followPlayer() {
		double dx = gameObj.getPlayer().getX() - getX();
		double dy = gameObj.getPlayer().getY() - getY();

		double dist = Math.sqrt(dx * dx + dy * dy); // finds the distance

		if (dist > 0) { // if not on player
			double moveX = (dx / dist) * speed;
			double moveY = (dy / dist) * speed;

			// actually moving towrds player
			x += moveX * 0.5 + (Math.random() - 0.5) * 0.3;
			y += moveY * 0.5 + (Math.random() - 0.5) * 0.3;
		}
	}

	public void draw(Graphics2D g) {

		// Calculate screen position
		int screenX = x - gameObj.getCameraX() - width / 2;
		int screenY = y - gameObj.getCameraY() - height / 2;

		BufferedImage img;

		if (dying) {
			// Freeze position at death for drawing
			img = deathFrames[Math.min(frame, deathFrames.length - 1)];

			g.drawImage(img, deathX - gameObj.getPlayer().getX() + AppPanel.WIDTH / 2 - width / 2,
					deathY - gameObj.getPlayer().getY() + AppPanel.HEIGHT / 2 - height / 2, width + 50, height + 50,
					null);

		} else {
			// normal animation stuff
			img = walkFrames[Math.min(frame, walkFrames.length - 1)];

			g.drawImage(img, screenX, screenY, width, height, null);
		}
	}
}