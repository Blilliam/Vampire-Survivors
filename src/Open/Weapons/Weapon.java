package Open.Weapons;

import java.awt.image.BufferedImage;

import main.GameObject;
import main.enums.WeaponTypes;


public abstract class Weapon {
	
	protected BufferedImage sprite;
	protected GameObject gameObj;
	protected BufferedImage icon;
	
	private WeaponTypes weaponType;
	
	protected int atkDelay;
	protected int delayCounter;
	protected double projectileBounces;
	private int range;
	private double maxDuration;
	protected double size;
	private double critRate;
	private double critDmg;
	protected double atk;
	protected double projectileCount;
	protected double rotationSpeed;
	protected int speed;

	
	
	public Weapon(GameObject gameObj, WeaponTypes type) {
		this.gameObj = gameObj;
		this.setWeaponType(type);
		size = 1;
		critDmg = 1;
	}
	
	public int getDmg() {
		double dmgMult = 1;
		dmgMult += (int)(critRate/1);
		double baseCrit = critRate%1;
		if (Math.random() <= critRate) {
			dmgMult += 1;
		}
		return (int) (atk * dmgMult);
	}
	
	public abstract void update();

	public double getProjectileBounces() {
		return projectileBounces;
	}

	public void setProjectileBounces(double projectileBounces) {
		this.projectileBounces = projectileBounces;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public BufferedImage getSprite() {
		return sprite;
	}

	public void setSprite(BufferedImage sprite) {
		this.sprite = sprite;
	}

	public double getAtk() {
		return atk;
	}

	public void setAtk(double atk) {
		this.atk = atk;
	}

	public double getMaxDuration() {
		return maxDuration;
	}

	public void setMaxDuration(double maxDuration) {
		this.maxDuration = maxDuration;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public double getSize() {
		// TODO Auto-generated method stub
		return size;
	}
	
	public void setSize(double newsize) {
		size = newsize;
	}

	public WeaponTypes getWeaponType() {
		return weaponType;
	}

	public void setWeaponType(WeaponTypes weaponType) {
		this.weaponType = weaponType;
	}
	
}
