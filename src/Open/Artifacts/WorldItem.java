package Open.Artifacts;

import java.awt.Graphics2D;
import Open.Entities.Entity;
import main.GameObject;

public class WorldItem extends Entity {
    private Artifact artifact;

    public WorldItem(GameObject gameObj, Artifact artifact, int x, int y) {
        super(gameObj);
        this.artifact = artifact;
        this.x = x;
        this.y = y;
        this.width = 32;  // Standard pickup size
        this.height = 32;
    }

    public void update() {
        // Collision check with player
        if (Entity.rectCollision(this, gameObj.getPlayer())) {
            // Give the actual artifact data to the player's manager
            gameObj.getPlayer().getArtifactManager().addArtifact(artifact);
            
            // Remove this physical wrapper from the ground list
            gameObj.getGroundItems().remove(this);
        }
    }

    public void draw(Graphics2D g) {
        if (gameObj.isOnScreen(x, y, width, height)) {
            int drawX = x - gameObj.getCameraX() - width / 2;
            int drawY = y - gameObj.getCameraY() - height / 2;
            
            // Draw the icon defined in the ChunkyOats / Artifact class
            g.drawImage(artifact.icon, drawX, drawY, width, height, null);
        }
    }
}