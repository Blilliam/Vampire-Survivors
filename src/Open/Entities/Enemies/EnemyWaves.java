package Open.Entities.Enemies;

import main.AppPanel;
import main.GameObject;
import main.enums.GameState;

public class EnemyWaves {
	GameObject gameObj;
	int waveNum; // make instance variable

	public EnemyWaves(GameObject gameObj) {
		this.gameObj = gameObj;
		waveNum = 1;
		createEnemies();
	}

	public void update() {
		// Only check for wave completion while playing
		if (GameObject.getState() == GameState.OPEN && gameObj.getEnemies().size() == 0) {
			waveNum++;
			createEnemies();
		}
	}

	/**
	 * creates more enemies in the enemies ArrayList
	 */
	public void createEnemies() {
	    int numEnemies;
	    int baseTier;

	    if (waveNum <= 10) {
	        numEnemies = waveNum + 2;
	        baseTier = 1;
	    } else if (waveNum <= 20) {
	        numEnemies = 10 + (waveNum - 10) * 2;
	        baseTier = 3;
	    } else {
	        numEnemies = 30 + (waveNum - 20) * 3;
	        baseTier = 5;
	    }

	    int margin = 200; // distance from player view

	    for (int i = 0; i < numEnemies; i++) {
	        int tier = baseTier + (int) (Math.random() * 3);

	        int tempX = 0;
	        int tempY = 0;

	        boolean spawnLeftOrRight = Math.random() < 0.5;

	        if (spawnLeftOrRight) {
	            // Spawn left or right off-screen
	            if (Math.random() < 0.5) {
	                // left
	                tempX = gameObj.getPlayer().getX() - AppPanel.WIDTH / 2 - margin;
	            } else {
	                // right
	                tempX = gameObj.getPlayer().getY() + AppPanel.WIDTH / 2 + margin;
	            }

	            tempY = (int) (Math.random() * gameObj.getMap().HEIGHT);

	        } else {
	            // Spawn top or bottom off-screen
	            if (Math.random() < 0.5) {
	                // top
	                tempY = gameObj.getPlayer().getY() - AppPanel.HEIGHT / 2 - margin;
	            } else {
	                // bottom
	                tempY = gameObj.getPlayer().getY() + AppPanel.HEIGHT / 2 + margin;
	            }

	            tempX = (int) (Math.random() * gameObj.getMap().WIDTH);
	        }

	        // Clamp to map boundaries
	        tempX = Math.max(0, Math.min(tempX, gameObj.getMap().WIDTH - 1));
	        tempY = Math.max(0, Math.min(tempY, gameObj.getMap().HEIGHT - 1));

	        gameObj.addEnemy(new Enemy(gameObj, tempX, tempY, tier));
	    }

	    // implemente boss later and events like swarm boss
	    
	}
}