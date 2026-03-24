package Open.Entities;

import java.awt.Color;
import java.awt.Graphics2D;

import main.AppPanel;
import main.GameObject;

public abstract class Entity { // main parent class of all moving objects on screen (weapon effects, player, enemies)

	public int x, y; // world coordinates

	//width and heigh
	public int width;
	public int height;

	//if youre dead
	public boolean isDead;

	public int speed; // pixels per update
	
	public int maxHp; // maximum health
	public int currHp; // current health

	public GameObject gameObj;

	public Entity(GameObject gameObj) {
		this.gameObj = gameObj;
	}

	//abscract/required methods to be an entity
	public abstract void update();

	// Draw relative to player
	public abstract void draw(Graphics2D g);

	//colision of 2 entities
	public static boolean rectCollision(Entity e1, Entity e2) {
		return e1.x < e2.x + e2.width && e1.x + e1.width > e2.x && e1.y < e2.y + e2.height && e1.y + e1.height > e2.y;
	}
	
	public static double getDistance(Entity e1, Entity e2) {
		double e1x = e1.getX() + e1.getWidth();
        double e1y = e1.getY() + e1.getHeight();

        double e2x = e2.getX() + e2.getWidth();
        double e2y = e2.getY() + e2.getHeight();

        double dxToEnemy = e1x - e2x;
        double dyToEnemy = e1y - e2y;

        return Math.sqrt(dxToEnemy * dxToEnemy + dyToEnemy * dyToEnemy);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}

	public int getCurrHp() {
		return currHp;
	}

	public void setCurrHp(int currHp) {
		this.currHp = currHp;
	}
	
	
}
