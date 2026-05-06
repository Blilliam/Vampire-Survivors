package Open.Artifacts.Common;

import Open.Artifacts.Artifact;
import main.Assets;
import main.GameObject;

public class Key extends Artifact {
    public Key(GameObject gameObj) {
        super(gameObj);
        this.name = "Key";
        this.icon = Assets.keyIcon;
    }

    @Override
    public double getPercentFreeChest() {
        // multiplier = 0.10
        return 1.0 - (1.0 / (1.0 + (0.10 * count)));
    }
}