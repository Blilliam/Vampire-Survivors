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
				gameObj.getPlayer().damage(10);
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

	    // 1. Determine which frame to use and where to put it
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

	        // Draw health bar (Done before the flash so the bar doesn't turn red)
	        if (currHp < maxHp && currHp > 0) {
	            drawHealthBar(g, drawX, drawY);
	        }
	    }

	    // 2. Handle the Rendering
	    if (damageFlashTimer > 0 && img != null) {
	        // Create a temporary image the size of the original frame
	        BufferedImage tintImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
	        Graphics2D gTint = tintImage.createGraphics();

	        // Draw the enemy onto the temp image
	        gTint.drawImage(img, 0, 0, null);

	        // Apply red tint ONLY to where the enemy's pixels are (SRC_ATOP)
	        gTint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.7f));
	        gTint.setColor(Color.RED);
	        gTint.fillRect(0, 0, img.getWidth(), img.getHeight());
	        
	        gTint.dispose();

	        // Draw the tinted result to the screen
	        g.drawImage(tintImage, drawX, drawY, drawW, drawH, null);
	    } else if (img != null) {
	        // Draw the normal image if not flashing
	        g.drawImage(img, drawX, drawY, drawW, drawH, null);
	    }
	}

	private void drawHealthBar(Graphics2D g, int screenX, int screenY) {
		int barWidth = 40;
		int barHeight = 5;
		int barOffset = 10; // Pixels above the enemy

		// Position the bar above the enemy's head
		int xPos = screenX + (width / 2) - (barWidth / 2);
		int yPos = screenY - barOffset;

		// 1. Black Outline
		g.setColor(Color.BLACK);
		g.fillRect(xPos - 1, yPos - 1, barWidth + 2, barHeight + 2);

		// 2. Red Background (Empty health)
		g.setColor(Color.RED);
		g.fillRect(xPos, yPos, barWidth, barHeight);

		// 3. Green Bar (Current health)
		double hpPercent = (double) currHp / maxHp;
		int hpWidth = (int) (barWidth * hpPercent);

		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, hpWidth, barHeight);
	}

	public boolean isDying() {
		return dying;
	}

	public void setDying(boolean dying) {
		this.dying = dying;
	}
}