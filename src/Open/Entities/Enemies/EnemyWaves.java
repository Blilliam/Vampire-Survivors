package Open.Entities.Enemies;

import main.AppPanel;
import main.GameObject;

public class EnemyWaves {

	private GameObject gameObj;

	// difficulty system
	private double credits;
	private double creditGainRate; // credits per tick
	private int difficultyLevel;

	public EnemyWaves(GameObject gameObj) {
		this.gameObj = gameObj;

		credits = 0;
		creditGainRate = 0.05; // starting difficulty
		difficultyLevel = 1;
	}

	public void update() {

		if (gameObj.getState() != gameObj.getStateOpen())
			return;

		// increase difficulty over time
		difficultyLevel++;

		// gain credits over time
		credits += creditGainRate;

		// slowly ramp difficulty
		if (difficultyLevel % 600 == 0) { // ~10 seconds at 60fps
			creditGainRate += 0.02;
		}

		// spawn as long as we can afford enemies
		while (credits >= 1) {
			spawnEnemy();
			credits -= 1;
		}
	}

	private void spawnEnemy() {

		int tier = 1 + (int) (Math.random() * 3); // basic tiers for now

		int margin = 100;

		int tempX = 0;
		int tempY = 0;

		boolean spawnLeftOrRight = Math.random() < 0.5;

		if (spawnLeftOrRight) {

			if (Math.random() < 0.5) {
				// left
				tempX = gameObj.getPlayer().getX() - AppPanel.WIDTH / 2 - margin;
			} else {
				// right
				tempX = gameObj.getPlayer().getX() + AppPanel.WIDTH / 2 + margin;
			}
			tempY = (int) (Math.random() * gameObj.getMap().HEIGHT);

		} else {

			if (Math.random() < 0.5) {
				// top
				tempY = gameObj.getPlayer().getY() - AppPanel.HEIGHT / 2 - margin;
			} else {
				// bottom
				tempY = gameObj.getPlayer().getY() + AppPanel.HEIGHT / 2 + margin;
			}
			tempX = (int) (Math.random() * gameObj.getMap().WIDTH);
		}
		// clamp to map
		tempX = Math.max(0, Math.min(tempX, gameObj.getMap().WIDTH - 1));
		tempY = Math.max(0, Math.min(tempY, gameObj.getMap().HEIGHT - 1));

		gameObj.addEnemy(new Enemy(gameObj, tempX, tempY, tier));
	}
}