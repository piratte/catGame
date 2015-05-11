/**
 * 
 */
package cz.adammar.cat;

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
	
	
}
