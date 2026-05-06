package Open.Artifacts.Common;

import java.awt.Graphics2D;

import Open.Artifacts.Artifact;
import main.Assets;
import main.GameObject;

public class ChunkyOats extends Artifact {

	public ChunkyOats(GameObject gameobj) {
		super(gameobj);
		this.name = "Chunky Oats";
		this.icon = Assets.oatsIcon;
	}

	@Override
	public int getFlatHealth() {
		return 25 * count;
	}

}
