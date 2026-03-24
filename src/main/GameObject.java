package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import Open.Entities.Chest;
import Open.Entities.Exp;
import Open.Entities.Player;
import Open.Entities.Enemies.Enemy;
import Open.Entities.Enemies.EnemyWaves;
import Open.Map.Background;
import Open.Upgrades.Upgrades;
import Open.Weapons.WeaponProjectile.WeaponEntity;
import main.enums.GameState;

public class GameObject {
	// declaring everything

	private MouseInput mouseHandler;

	// buttons for game start/settings stuff
	private int startButtonWidth;
	private int startButtonHeight;
	private GameButton startButton;

	private int exitControlButtonWidth;
	private int exitControlButtonHeight;
	private GameButton exitControlButton;

	private int controlButtonWidth;
	private int controlButtonHeight;
	private GameButton controlButton;

	// keyboard inputs
	private KeyboardInput keyH;

	// managing the various tates
	private static GameState state;

	// player
	private Player player;

	// all enemies
	private ArrayList<Enemy> enemies;
	
	//chests
	private ArrayList<Chest> chests;
	
	//exp
	private ArrayList<Exp> exp;
	
	private ArrayList<WeaponEntity> projectiles;
	
	//upgrades
	private Upgrades upgrades;

	// manages enemy generation
	private EnemyWaves waves;

	// the map
	private Background map;

	public GameObject(KeyboardInput keyH, MouseInput mouseHandler) {
		Assets.load(); // loads all the images

		this.keyH = keyH;

		this.mouseHandler = mouseHandler;
		state = GameState.MENU; // sets teh state to the meneu
		
		

		// actually initializes the buttons
		startButtonWidth = 300;
		startButtonHeight = 100;

		controlButtonWidth = 300;
		controlButtonHeight = 100;

		exitControlButtonWidth = 300;
		exitControlButtonHeight = 100;

		startButton = new GameButton(AppPanel.WIDTH / 2 - startButtonWidth / 2,
				AppPanel.HEIGHT / 2 - startButtonHeight / 2, startButtonWidth, startButtonHeight, "START",
				this::startGame, new Color(0, 60, 60), Color.BLACK);

		controlButton = new GameButton(AppPanel.WIDTH / 2 - startButtonWidth / 2,
				AppPanel.HEIGHT / 2 - controlButtonWidth / 2 + 230 + controlButtonHeight / 2, controlButtonWidth,
				controlButtonHeight, "CONTROLS", this::showControls, new Color(0, 60, 60), Color.BLACK);

		exitControlButton = new GameButton(AppPanel.WIDTH / 2 - exitControlButtonWidth / 2,
				AppPanel.HEIGHT / 2 + exitControlButtonHeight / 2 + 50, exitControlButtonWidth, exitControlButtonHeight, "EXIT BACK",
				this::toMenu);
	}

	public void update() {
		if (state == GameState.MENU) { // while in menu

			// update buttons
			startButton.update();
			controlButton.update();

		} else if (state == GameState.OPEN) {
			
			ScoreManager.checkAndUpdateHighScore(player.getKills());//update the score

			// updatePlayer
			player.update();

			for (int i = enemies.size() - 1; i >= 0; i--) { // for every enemy (going backwards)
				Enemy e = enemies.get(i);

				e.update(); // update each enemy

				if (e.isDead) {
					enemies.remove(i); // removes dead enemies
				}
			}
			for (Exp e: exp) {
				e.update();
			}
			for (Chest e: chests) {
				e.update();
			}
			for (WeaponEntity e: projectiles) {
				e.update();
			}
			
			waves.update(); // update enemy spawning

		} else if (state == GameState.BOSS) { // boss battle

			player.update(); // still updates player

		} else if (state == GameState.DEAD) { // when you die

			exitControlButton.update(); // button for going back to menu

		} else if (state == GameState.UPGRADING) { // while upgrading
			
			upgrades.update(); // lets player select upgrades

			// Only go back to PLAY **after player confirms upgrade or no upgrades left**
			if (upgrades.hasFinishedUpgrading()) {
				state = GameState.OPEN;
			}

		} else if (state == GameState.CONTROLS) { // looking at controls
			exitControlButton.update(); // for going back
		}
		// Clear click after updates
		MouseInput.update();
	}

	public void draw(Graphics2D g2) {

		if (state == GameState.MENU) {

			// draw buttons
			startButton.draw(g2);
			controlButton.draw(g2);

		} else if (state == GameState.OPEN) {

			drawOpen(g2); // draws everything related to the main game loop

		} else if (state == GameState.BOSS) {

			player.draw(g2); // draw player

		} else if (state == GameState.DEAD) {

			drawOpen(g2);
			g2.setColor(new Color(0, 0, 0, 100));
			g2.fillRect(0, 0, AppPanel.WIDTH, AppPanel.HEIGHT);
			exitControlButton.draw(g2); // draw exit button

		} else if (state == GameState.UPGRADING) {
			
			g2.setColor(new Color(0, 0, 0, 150)); // dark semi-transparent overlay
			g2.fillRect(0, 0, AppPanel.WIDTH, AppPanel.HEIGHT);

			upgrades.draw(g2);

		} else if (state == GameState.CONTROLS) {

			drawControls(g2); // draw controls
			exitControlButton.draw(g2);
		}
	}

	public void drawOpen(Graphics2D g2) {
		map.draw(g2); // draw map
		player.draw(g2); // draw player
		for (Enemy e : enemies) {
			e.draw(g2); // draw every enemy
		}
		for (Exp e: exp) {
			e.draw(g2);
		}
		for (Chest e: chests) {
			e.draw(g2);
		}
		for (WeaponEntity e: projectiles) {
			e.draw(g2);
		}
	}

	public void drawControls(Graphics2D g2) {
		// background
		g2.setColor(new Color(30, 30, 80, 200)); // dark blue overlay
		g2.fillRect(0, 0, AppPanel.WIDTH, AppPanel.HEIGHT);

		// text settings
		g2.setColor(Color.BLACK);
		g2.setFont(new Font("Malgun Gothic", Font.PLAIN, 30));
		FontMetrics fm = g2.getFontMetrics();

		String s1 = "Up: W";
		String s2 = "Down: S";
		String s3 = "Left: A";
		String s4 = "Right: D";

		int x1 = (AppPanel.WIDTH - fm.stringWidth(s1)) / 2;
		int x2 = (AppPanel.WIDTH - fm.stringWidth(s2)) / 2;
		int x3 = (AppPanel.WIDTH - fm.stringWidth(s3)) / 2;
		int x4 = (AppPanel.WIDTH - fm.stringWidth(s4)) / 2;

		// actually drawing the controls
		g2.drawString(s1, x1, AppPanel.HEIGHT / 2 - 100);
		g2.drawString(s2, x2, AppPanel.HEIGHT / 2 - 70);
		g2.drawString(s3, x3, AppPanel.HEIGHT / 2 - 40);
		g2.drawString(s4, x4, AppPanel.HEIGHT / 2 - 10);
	}

	private void startGame() { // creates new everything

		enemies = new ArrayList<Enemy>();
		
		map = new Background(this);

		player = new Player(this);

		waves = new EnemyWaves(this);

		state = GameState.OPEN;
		
		exp = new ArrayList<Exp>();
		
		chests = new ArrayList<Chest>();
		chests.add(new Chest(this, player.getX() + 200, player.getY()));
	}
	
	public int getCameraX() {
	    return (int)player.getX() - AppPanel.WIDTH / 2;
	}

	public int getCameraY() {
	    return (int)player.getY() - AppPanel.HEIGHT / 2;
	}

	private void startBossBattle() {
		state = GameState.BOSS;
	}

	private void showControls() {
		state = GameState.CONTROLS;
	}

	private void toMenu() {
		state = GameState.MENU;

	}
	
	private void startUpgrades() {
		state = GameState.UPGRADING;
	}

	public MouseInput getMouseHandler() {
		return mouseHandler;
	}

	public void setMouseHandler(MouseInput mouseHandler) {
		this.mouseHandler = mouseHandler;
	}

	public KeyboardInput getKeyH() {
		return keyH;
	}

	public void setKeyH(KeyboardInput keyH) {
		this.keyH = keyH;
	}

	public static GameState getState() {
		return state;
	}

	public static void setState(GameState state) {
		GameObject.state = state;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}
	
	public void addEnemy(Enemy e) {
		enemies.add(e);
	}

	public void setEnemies(ArrayList<Enemy> enemies) {
		this.enemies = enemies;
	}

	public EnemyWaves getWaves() {
		return waves;
	}

	public void setWaves(EnemyWaves waves) {
		this.waves = waves;
	}
	
	public Background getMap() {
		return map;
	}
	
	public void addExp(int value, int x, int y) {
		exp.add(new Exp(this, value, x, y));
	}
}
