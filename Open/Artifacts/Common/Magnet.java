package Open.Artifacts.Common;

import Open.Artifacts.Artifact;
import Open.Entities.Exp;
import main.Assets;
import main.GameObject;

public class Magnet extends Artifact {
	int cooldown;
	int count;

	public Magnet(GameObject gameobj) {
		super(gameobj);
		this.name = "Watch";
		this.icon = Assets.magnetIcon;
		cooldown = 200;
		count = 0;
	}

	@Override
	public void update() {
		count++;
		if (count >= cooldown) {
			count = 0;
			for (Exp e : gameObj.getExp()) {
				e.setCollected(true);
			}

		}
	}

}
