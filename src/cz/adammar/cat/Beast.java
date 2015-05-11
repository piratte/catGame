/**
 * 
 */
package cz.adammar.cat;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * 
 * Abstract class from which all the game beasts will inherit
 * 
 * 
 * @author madam
 *
 */
public abstract class Beast {
	
	/**
	 * Size in pixels
	 */
	protected final int HALF_WIDTH = 10;
	protected final int HALF_HEIGHT = 10;
	
	/**
	 * Position in pixels
	 */
	protected int x,y;
	
	/**
	 * Direction of movement
	 */
	direction dir;
	
	/**
	 * Base images
	 */
	protected BufferedImage up,down,right,left;
	
	/**
	 * Speed of movement in pixels/s
	 */
	protected int speed;
	
	
	public void move(long interval){
		int deltaX = (int) (speed * dir.getXDelta() * interval / 1000000);
		int deltaY = (int) (speed * dir.getYDelta() * interval / 1000000);
		x += deltaX;
		y += deltaY;
	}
	
	public void drawSelf(Graphics2D g){
		int top = y - HALF_HEIGHT;
		int left = x - HALF_WIDTH;
		g.drawImage(up,null,left,top);
	}
	
	public abstract void updateDirection();
	
	
	
}
