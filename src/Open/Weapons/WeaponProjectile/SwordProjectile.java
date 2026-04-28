package Open.Weapons.WeaponProjectile;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import Open.Weapons.Weapon;
import main.AppPanel;
import main.GameObject;
import main.Vec2;

public class SwordProjectile extends WeaponEntity {

    private final int MAX_LIFETIME = 12; // Adjusted for a slightly slower draw

    public SwordProjectile(GameObject gameObj, Weapon weapon, Vec2 direction, int x, int y) {
        super(gameObj, weapon, direction, x, y);
        
        // Base size, scaled by weapon stats
        this.width = (int)(150 * weapon.getSize());
        this.height = (int)(80 * weapon.getSize());
        
        // Standard melee setup: velocity is (0,0), it doesn't fly.
        this.velocity = new Vec2(0, 0); 
        
        // Calculate the angle based on target direction once at spawn.
        // If your sprite points to the right by default, this works.
        this.angle = Math.atan2(direction.getY(), direction.getX());
    }

    @Override
    protected void updatePhysics() {
        // 1. Keep the slash attached to the player (the center of the sweep)
        this.position.setX(gameObj.getPlayer().getX());
        this.position.setY(gameObj.getPlayer().getY());

        // 2. Count lifespan
        duration++;
        if (duration > MAX_LIFETIME) {
            isDead = true;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        // If your sprite isn't loaded or you want a "no-art" fallback
        if (sprite == null) {
            drawFallbackArc(g);
            return;
        }

        // --- Standard Melee Draw Logic ---
        
        // Calculate screen coordinates, centering the hitbox
        int screenX = x - gameObj.getPlayer().getX() + AppPanel.WIDTH / 2;
        int screenY = y - gameObj.getPlayer().getY() + AppPanel.HEIGHT / 2;

        AffineTransform oldTransform = g.getTransform();

        // Move to rotation point
        g.translate(screenX, screenY);
        
        // Apply target rotation
        g.rotate(this.angle);

        // --- MEGABONK VISUAL EFFECTS ---

        // 1. Dynamic Scaling (Starts small, expands out slightly)
        double scaleProgress = (double)duration / MAX_LIFETIME;
        double currentSizeScale = 1.0 + (0.3 * scaleProgress); // Grows 30% larger
        
        // 2. Fading Out (Gets more transparent over its life)
        float alpha = 1.0f - (float)scaleProgress;
        if (alpha < 0) alpha = 0;
        
        // Apply Transparency
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        // 3. Draw Image (Centered on origin)
        // We multiply the final width by our expansion scale
        g.drawImage(sprite, 
                   -(int)(width * currentSizeScale / 2.0), 
                   -(int)(height * currentSizeScale / 2.0), 
                   (int)(width * currentSizeScale), 
                   (int)(height * currentSizeScale), 
                   null);

        // Reset Composites and Transformations so we don't break subsequent draws
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g.setTransform(oldTransform);
    }

    /**
     * Fallback in case Assets.katanaSlashSprite is null.
     * Draws a primitive white/blue arc.
     */
    private void drawFallbackArc(Graphics2D g) {
        int screenX = x - gameObj.getPlayer().getX() + AppPanel.WIDTH / 2;
        int screenY = y - gameObj.getPlayer().getY() + AppPanel.HEIGHT / 2;

        AffineTransform oldTransform = g.getTransform();
        g.translate(screenX, screenY);
        g.rotate(this.angle);

        // Progress variables (0.0 to 1.0)
        double prog = (double)duration / MAX_LIFETIME;

        // Draw a quick "Flash" white inner arc
        if (prog < 0.5) {
            g.setColor(new Color(255, 255, 255, 200));
            g.fillArc(-(width / 2), -(height / 2), width, height, -60, 120);
        } else {
            // Draw a blue outer "residue" arc that fades
            float alpha = 1.0f - (float)((prog - 0.5) * 2.0);
            if (alpha < 0) alpha = 0;
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g.setColor(new Color(100, 200, 255));
            g.drawArc(-(width / 2), -(height / 2), width, height, -60, 120);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }

        g.setTransform(oldTransform);
    }
}