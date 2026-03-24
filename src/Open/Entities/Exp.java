package Open.Entities;


import java.awt.Color;
import java.awt.Graphics2D;

import main.GameObject;

public class Exp extends Entity {
    private int value;
    private static double valueMult = 1;
    
    private double maxSpeed = 10;
    private double accelStrength = 0.5;
    
    private double magnetRange = 450; 
    
    private int radius;
    
    private int dx;
    private int dy;



    // Rainbow animation
    private float hue = 0f;          // 0 → 1
    private static final float HUE_SPEED = 0.005f;

    public Exp(GameObject gameObj, int value, int x, int y) {
    	super(gameObj);
        this.value = (int) Math.ceil(value * valueMult);

        radius = (int) (2 + this.value * 1.3);

        dx = 0;
        dy = 0;

        setX(x);
        setY(y);
    }
    
    

    public void draw(Graphics2D g2) {
        // Advance rainbow color
        hue += HUE_SPEED;
        if (hue > 1f) hue = 0f;

        // Convert HSV → RGB
        Color rainbow = Color.getHSBColor(hue, 1f, 1f);
        g2.setColor(rainbow);

        // Draw coin
        g2.fillOval((int) getX(), (int) getY(), radius * 2, radius * 2);
    }
    
    public void update() {
        double px = gameObj.getPlayer().getX() + gameObj.getPlayer().getWidth();
        double py = gameObj.getPlayer().getY() + gameObj.getPlayer().getHeight();

        double cx = getX() + radius;
        double cy = getY() + radius;

        double dxToPlayer = px - cx;
        double dyToPlayer = py - cy;

        double distance = Math.sqrt(dxToPlayer * dxToPlayer + dyToPlayer * dyToPlayer);

        if (distance < 0.1) return;
        if (distance > magnetRange) return;

        double dirX = dxToPlayer / distance;
        double dirY = dyToPlayer / distance;

        double t = 1.0 - (distance / magnetRange);
        t = Math.max(0, Math.min(1, t));
        t = t * t; // ease-in

        double desiredSpeed = maxSpeed * t;
        double desiredVX = dirX * desiredSpeed;
        double desiredVY = dirY * desiredSpeed;

        double steerX = desiredVX - dx;
        double steerY = desiredVY - dy;

        double steerMag = Math.sqrt(steerX * steerX + steerY * steerY);
        if (steerMag > accelStrength) {
            steerX = (steerX / steerMag) * accelStrength;
            steerY = (steerY / steerMag) * accelStrength;
        }

        dx += steerX;
        dy += steerY;

        setX(getX() + dx);
        setY(getY() + dy);
    }
    
    public int getRadius() {
    	return radius;
    }
    
    public void setRadius(int newRadius) {
    	radius = newRadius;
    }

}


