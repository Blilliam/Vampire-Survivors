package Open.Artifacts.Common;

import Open.Artifacts.Artifact;
import main.Assets;
import main.GameObject;

public class Battery extends Artifact {
    public Battery(GameObject gameObj) {
        super(gameObj);
        this.name = "Battery";
        this.icon = Assets.batteryIcon;
    }

    @Override
    public double getPercentAttackSpeed() {
        return 0.15 * count;
    }
}