package States;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import main.AppPanel;
import main.GameObject;

public class ControlsState extends BaseState {

	public ControlsState(GameObject gameObj) {
		super(gameObj);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Graphics2D g2) {

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

	@Override
	public void upadate() {
		gameObj.getExitControlButton().update(); // for going back

	}

}
