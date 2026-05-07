package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class GameButton {

    private int x, y, w, h;
    private String buttonText;
    private Runnable clickFunc;
    private Color mainColor;
    private Color borderColor;
    private boolean transparentBackground = false;

    public GameButton(int x, int y, int w, int h, String text, Runnable clickFunc, Color mainColor, Color borderColor) {
        this(x, y, w, h, text, clickFunc);
        this.mainColor = mainColor;
        this.borderColor = borderColor;
    }
    
    public GameButton(int x, int y, int w, int h, String text, Runnable clickFunc) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.buttonText = text;
        this.clickFunc = clickFunc;
        this.mainColor = new Color(60, 60, 60);
        this.borderColor = Color.WHITE;
    }

    public boolean isHovering() {
        return MouseInput.getMouseX() >= x && MouseInput.getMouseX() <= x + w &&
               MouseInput.getMouseY() >= y && MouseInput.getMouseY() <= y + h;
    }

    public void update() {
        if (isHovering() && MouseInput.isMousePressed()) {
            clickFunc.run();
            MouseInput.update(); 
        }
    }

    public void draw(Graphics2D g2) {
        if (!transparentBackground) {
            g2.setColor(mainColor);
            g2.fillRect(x, y, w, h);
        }

        if (isHovering()) {
            g2.setColor(new Color(0, 0, 0, 80));
            g2.fillRect(x, y, w, h);
        }

        g2.setColor(borderColor);
        g2.setStroke(new java.awt.BasicStroke(3));
        g2.drawRect(x, y, w, h);

        g2.setFont(new Font("Malgun Gothic", Font.BOLD, 22));
        g2.setColor(Color.WHITE);

        FontMetrics fm = g2.getFontMetrics();
        int textX = x + (w - fm.stringWidth(buttonText)) / 2;
        int textY = y + (h + fm.getAscent()) / 2 - 4;

        g2.drawString(buttonText, textX, textY);
    }

    public void setTransparent(boolean transparent) { this.transparentBackground = transparent; }
    public int getWidth() { return w; }
    public int getHeight() { return h; }
    public int getY() { return y; }
    public int getX() { return x; }
}