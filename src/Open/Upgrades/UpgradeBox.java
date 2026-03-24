package Open.Upgrades;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GameObject;
import main.MouseInput;

public class UpgradeBox {

    public int type;
    private final GameObject gameObj;

    public int x, y, w = 150, h = 100;

    // Animation variables
    public float animationProgress = 1f; // 1 = fully visible, 0 = invisible
    public int startY = 0;                // starting Y for slide-in

    public UpgradeBox(GameObject gameObj, int type, int x, int y) {
        this.gameObj = gameObj;
        this.type = type;
        this.x = x;
        this.y = y;
        this.startY = y - 50; // start slightly above
        this.animationProgress = 1f;
    }

    public boolean isHovering() {
        return MouseInput.mouseX >= x && MouseInput.mouseX <= x + w &&
               MouseInput.mouseY >= y && MouseInput.mouseY <= y + h;
    }

    public void updateAnimation() {
        if (animationProgress < 1f) {
            animationProgress += 0.1f; // adjust speed here
            if (animationProgress > 1f) animationProgress = 1f;
        }
    }

    public void draw(Graphics2D g2) {
        // Calculate Y with slide animation
        int drawY = (int)(startY + (y - startY) * animationProgress);

        // Base color
        g2.setColor(new Color(60, 60, 60));
        g2.fillRect(x, drawY, w, h);

        // Hover overlay
        if (isHovering()) {
            g2.setColor(new Color(0, 0, 0, 70));
            g2.fillRect(x, drawY, w, h);
        }

        // Border
        g2.setColor(new Color(0, 120, 255));
        g2.drawRect(x, drawY, w, h);

        // Description text
        g2.setColor(Color.WHITE);
        String[] lines = description().split("\n");
        int totalH = lines.length * g2.getFontMetrics().getHeight();
        int textY = drawY + (h - totalH) / 2 + g2.getFontMetrics().getAscent();
        for (String line : lines) {
            int textX = x + (w - g2.getFontMetrics().stringWidth(line)) / 2;
            g2.drawString(line, textX, textY);
            textY += g2.getFontMetrics().getHeight();
        }
    }

    public void startAnimation() {
        animationProgress = 0f;
        startY = y - 50; // slide in from above
    }

    public void upgrade() {
//        switch (type) {
//            case 1:
//            	Bullet.dmg++;
//            	break;
//            case 2:
//            	gameObj.player1.maxSpeed *= 1.1;
//            	gameObj.player1.boostMaxSpeed *= 1.1;
//            	break;
//            case 3:
//            	gameObj.player1.bulletTeir++;
//            	break;
//            case 4:
//            	gameObj.player1.delay *= 0.85;
//            	break;
//            case 5:
//            	gameObj.player1.health += 3;
//            	break;
//            case 6:
//            	Bullet.speed *= 1.2;
//            	break;
//            case 7:
//            	Exp.valueMult *= 1.5;
//            	break;
//            case 8:
//            	Bullet.dropRate *= 0.8;
//            	break;
//        }
       
    }

    public String description() {
        return switch (type) {
            case 1 -> "Bullet Damage\n+1 damage";
            case 2 -> "Player Speed\n+10% movement";
            case 3 -> "Bullet Amount\n+1 bullet teir";
            case 4 -> "Attack Speed\n15% faster fire";
            case 5 -> "Player Health\n+3 HP";
            case 6 -> "Bullet Speed\n+20% bullet speed";
            case 7 -> "Coin value\n+25% coins gained";
            case 8 -> "Coin drop rate\n+20% coins drop rate";
            default -> "Non-existent box";
        };
    }
}
