package States;

import java.awt.Graphics2D;

import Open.Artifacts.WorldItem;
import Open.Entities.Exp;
import Open.Entities.Enemies.Enemy;
import Open.Entities.Interactible.Chest;
import Open.Weapons.WeaponProjectile.WeaponEntity;
import main.DamageText;
import main.GameObject;
import main.ScoreManager;

public class OpenState extends BaseState {

	public OpenState(GameObject gameObj) {
		super(gameObj);

	}

	@Override
	public void draw(Graphics2D g2) {
		gameObj.getMap().draw(g2); // draw map

		for (Enemy e : gameObj.getEnemies()) {
			if (gameObj.isOnScreen(e.getX(), e.getY(), e.getWidth(), e.getHeight()))
				e.draw(g2); // draw every enemy
		}
		for (WeaponEntity e : gameObj.getProjectiles()) {
			if (gameObj.isOnScreen(e.getX(), e.getY(), e.getWidth(), e.getHeight()))
				e.draw(g2);
		}
		for (Exp e : gameObj.getExp()) {
			if (gameObj.isOnScreen(e.getX(), e.getY(), e.getWidth(), e.getHeight()))
				e.draw(g2);
		}
		for (Chest e : gameObj.getChests()) {
			if (gameObj.isOnScreen(e.getX(), e.getY(), e.getWidth(), e.getHeight()))
				e.draw(g2);
		}
		for (WorldItem e : gameObj.getGroundItems()) {
			if (gameObj.isOnScreen(e.getX(), e.getY(), e.getWidth(), e.getHeight()))
				e.draw(g2);
		}
		for (DamageText dt : gameObj.getDamageTexts()) {
	        dt.draw(g2, gameObj.getCameraX(), gameObj.getCameraY());
	    }
		gameObj.getPlayer().draw(g2); // draw player

	}

	@Override
	public void upadate() {
		ScoreManager.checkAndUpdateHighScore(gameObj.getPlayer().getKills());// update the score

		// updatePlayer
		gameObj.getPlayer().update();

		for (int i = gameObj.getEnemies().size() - 1; i >= 0; i--) { // for every enemy (going backwards)
			Enemy e = gameObj.getEnemies().get(i);

			e.update(); // update each enemy

			if (e.isDead()) {
				gameObj.getEnemies().remove(i); // removes dead enemies
			}
		}
		for (int i = gameObj.getDamageTexts().size() - 1; i >= 0; i--) {
			gameObj.getDamageTexts().get(i).update();
			if (gameObj.getDamageTexts().get(i).isDead())
				gameObj.getDamageTexts().remove(i);
		}

		for (int i = gameObj.getExp().size() - 1; i >= 0; i--) { // for every enemy (going backwards)
			Exp e = gameObj.getExp().get(i);

			e.update(); // update each enemy

			if (e.isDead()) {
				gameObj.getExp().remove(i); // removes dead enemies
			}
		}

		for (Chest e : gameObj.getChests()) {
			e.update();
		}
		for (int i = gameObj.getProjectiles().size() - 1; i >= 0; i--) { // for every enemy (going backwards)
			WeaponEntity e = gameObj.getProjectiles().get(i);

			e.update(); // update each enemy

			if (e.isDead()) {
				gameObj.getProjectiles().remove(i); // removes dead enemies
			}
		}
		for (int i = gameObj.getGroundItems().size() - 1; i >= 0; i--) { // for every enemy (going backwards)
			WorldItem e = gameObj.getGroundItems().get(i);

			e.update(); // update each enemy
		}

		gameObj.getWaves().update(); // update enemy spawning

	}

}
