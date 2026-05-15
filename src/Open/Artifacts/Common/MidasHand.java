package Open.Artifacts.Common;

import Open.Artifacts.Artifact;
import main.Assets;
import main.GameObject;

public class MidasHand extends Artifact { // still not implemented
    public MidasHand(GameObject gameObj) {
        super(gameObj);
        this.name = "Midas Hand";
        this.icon = Assets.CommonMidasIcon;
    }

    // Custom method to be called by your gold logic
    @Override
    public double getPercentBonusGold() {
        return 0.15 * count;
    }
}