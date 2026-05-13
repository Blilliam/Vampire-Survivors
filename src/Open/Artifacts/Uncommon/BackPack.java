package Open.Artifacts.Uncommon;

import Open.Artifacts.Artifact;
import main.Assets;
import main.GameObject;

public class BackPack extends Artifact {
    public BackPack(GameObject gameObj) { // done
        super(gameObj);
        this.name = "BackPack";
        this.icon = Assets.UncommonBackPackIcon;
    }

    @Override
    public int getBonusProjectiles() {
        return 1 * count;
    }
}