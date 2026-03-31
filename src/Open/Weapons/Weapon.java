package Open.Weapons;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.GameObject;


public abstract class Weapon {
	public double atk;
	public double projectileCount;
	public double rotationSpeed;
	public int speed;
	public BufferedImage sprite;
	public GameObject gameObj;
	
	public int atkDelay;
	public int delayCounter;

	
	
	public Weapon(GameObject gameObj) {
		this.gameObj = gameObj;
	}

	public void draw(Graphics2D g2) {
		
	}
	
	public void update() {
		
	}
	
}
