package Open.Weapons.WeaponProjectile;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import Open.Weapons.Weapon;
import main.AppPanel;
import main.GameObject;
import main.Vec2;
import main.enums.WeaponUpgrades;

public class SwordProjectile extends WeaponEntity {

    private final int MAX_LIFETIME = 12; 

    public SwordProjectile(GameObject gameObj, Weapon weapon, Vec2 direction, int x, int y) {
        super(gameObj, weapon, direction, x, y);
        
        // 1. Calculate size based on weapon stats
        this.width = (int)(300 * weapon.getStats().get(WeaponUpgrades.AttackSize));
        this.height = (int)(160 * weapon.getStats().get(WeaponUpgrades.AttackSize));
        
        this.velocity = new Vec2(0, 0); 
        this.angle = Math.atan2(direction.getY(), direction.getX());
        
        // 2. Immediate position sync to prevent 1-frame "teleport" borks
        syncHitboxToPlayer();
    }

    private void syncHitboxToPlayer() {
        // CENTER the hitbox on the player's current position
        // If we don't subtract half width/height, the hitbox is offset to the bottom-right
        this.position.setX(gameObj.getPlayer().getX() - (this.width / 2.0));
        this.position.setY(gameObj.getPlayer().getY() - (this.height / 2.0));

        // Update the actual integer x/y used by the Entity collision system
        this.x = (int) position.getX();
        this.y = (int) position.getY();
    }

    @Override
    protected void updatePhysics() {
        // Keep the slash attached to the player every frame
        syncHitboxToPlayer();

        duration++;
        if (duration > MAX_LIFETIME) {
            isDead = true;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        // Draw the impact animation if it exists
        drawImpact(g);

        if (sprite == null) {
            drawFallbackArc(g);
            return;
        }

        // --- Visual Positioning ---
        // We calculate screen coordinates based on the player's center
        int screenX = AppPanel.WIDTH / 2;
        int screenY = AppPanel.HEIGHT / 2;

        AffineTransform oldTransform = g.getTransform();

        // Move to the player's center on screen
        g.translate(screenX, screenY);
        g.rotate(this.angle);

        // Visual Effects: Scaling and Fading
        double scaleProgress = (double)duration / MAX_LIFETIME;
        double currentSizeScale = 1.0 + (0.3 * scaleProgress); 
        float alpha = Math.max(0, 1.0f - (float)scaleProgress);
        
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        // Draw centered on the rotation point
        int finalW = (int)(width * currentSizeScale);
        int finalH = (int)(height * currentSizeScale);
        
        g.drawImage(sprite, -finalW / 2, -finalH / 2, finalW, finalH, null);

        // Clean up
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g.setTransform(oldTransform);

        // DEBUG: Uncomment this to see the actual hitbox if it still feels "borked"
        /*
        g.setColor(Color.RED);
        g.drawRect(x - gameObj.getPlayer().getX() + AppPanel.WIDTH/2, 
                   y - gameObj.getPlayer().getY() + AppPanel.HEIGHT/2, 
                   width, height);
        */
    }

    private void drawFallbackArc(Graphics2D g) {
        int screenX = AppPanel.WIDTH / 2;
        int screenY = AppPanel.HEIGHT / 2;

        AffineTransform oldTransform = g.getTransform();
        g.translate(screenX, screenY);
        g.rotate(this.angle);

        double prog = (double)duration / MAX_LIFETIME;
        float alpha = Math.max(0, 1.0f - (float)prog);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        g.setColor(Color.WHITE);
        g.fillArc(-(width / 2), -(height / 2), width, height, -60, 120);

        g.setTransform(oldTransform);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }
}