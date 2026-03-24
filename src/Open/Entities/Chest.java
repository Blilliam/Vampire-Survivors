package Open.Entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import main.AppPanel;
import main.GameObject;
import main.enums.ChestState;

public class Chest extends Entity{

    private ChestState state = ChestState.CLOSED;

    private int shakeTimer = 0;
    private int openProgress = 0;

    private Random rand = new Random();

    public Chest(GameObject gameObj, int x, int y){
    	super(gameObj);
    	width = 80;
        height = 60;
    	this.x = x;
    	this.y = y;
    }

    public void openChest(){
        state = ChestState.SHAKING;
    }

    public void update(){
    	if (Entity.rectCollision(this, gameObj.getPlayer())) {
    		state = ChestState.OPENING;
    	}

        switch(state){

            case SHAKING:
                shakeTimer++;
                if(shakeTimer > 60){
                    state = ChestState.OPENING;
                }
                break;

            case OPENING:
                openProgress++;
                if(openProgress > 30){
                    state = ChestState.OPEN;
                }
                break;

            default:
                break;
        }
    }

    public void draw(Graphics2D g){

    	int drawX = x - gameObj.getCameraX() - width / 2;
		int drawY = y - gameObj.getCameraY() - height / 2;

        if(state == ChestState.SHAKING){
            drawX += rand.nextInt(6) - 3;
            drawY += rand.nextInt(6) - 3;
        }

        g.setColor(new Color(120, 70, 20));
        g.fillRect(drawX, drawY, width, height/2);

        int lidHeight = height/2;

        if(state == ChestState.OPENING || state == ChestState.OPEN){
            lidHeight = height/2 - openProgress;
            if(lidHeight < 0) lidHeight = 0;
        }

        g.setColor(new Color(160, 100, 40));
        g.fillRect(drawX, drawY - lidHeight, width, lidHeight);

        if(state == ChestState.OPEN || state == ChestState.OPENING){
            g.setColor(new Color(255, 255, 100, 120));
            g.fillOval(drawX - 20, drawY - 40, width + 40, height);
        }
    }
}
