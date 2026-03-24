package main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseInput extends MouseAdapter {

	//mouse positionn
    public static int mouseX;
    public static int mouseY;

    //mouse click
    public static boolean mousePressed = false;

    //if clicked = true
    @Override
    public void mousePressed(MouseEvent e) {
        mousePressed = true;
    }

    //whenever moved, update position
    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    //also update position when moved
    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    // called once per frame to set pressed to false
    public static void update() {
        mousePressed = false;
    }
}