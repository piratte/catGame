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
	protected final int HALF_WIDTH = GamePanel.PIECE_WIDTH/2;
	protected final int HALF_HEIGHT = GamePanel.PIECE_HEIGHT/2;
	
	/**
	 * Default direction to prevent Null Pointer
	 */
	protected final direction DEF_DIR = direction.RIGHT;
	
	/**
	 * Position in pixels
	 */
	protected int x,y;
	
	/**
	 * Maze in which the beast lives
	 */
	protected Maze maze;
	
	/**
	 * Direction of movement
	 */
	protected direction dir = DEF_DIR;
	
	/**
	 * Direction that the beast wants to take
	 */
	protected direction nextDir = DEF_DIR;
	
	/**
	 * Base images
	 */
	protected BufferedImage up,down,right,left,cur;
	
	/**
	 * Speed of movement in pixels/s
	 */
	protected int speed;
	
	/**
	 * Changes in X and Y for the move method
	 */
	protected int deltaX, deltaY;
	
	/**
	 * Temporary coordinates
	 */
	protected int newX, newY;
	
	protected int tileX = 0;
	protected int tileY = 0;
	
	protected Beast(int x, int y, int speed, Maze maze){
		this.x = x; this.y = y; 
		this.speed = speed; this.maze = maze;
		dir = DEF_DIR; 
	}
	
	public void move(long interval){
		/**
		 * Avoiding the first iteration
		 */
		if (interval < 0)
			return;
		

		interval /= 1000000;
		//System.err.println(interval + "ms");
		deltaX = (int) ((speed * dir.getXDelta() * interval)/100);
		deltaY = (int) ((speed * dir.getYDelta() * interval)/100);
		//System.err.println("x: " + deltaX + " y: " + deltaY);
		newX = x + deltaX;
		newY = y + deltaY;

		/**
		 * We can check, whether there is a need to change direction only, when the Beast crosses the midle of the tile
		 */
		if (maze.crossedXLine(x, newX) != -1 || maze.crossedYLine(y, newY) != -1){
			
			if (maze.canGo(newX, newY, nextDir)){
				System.err.println("Changing direction");
				dir = nextDir;
				
				/**
				 * Center the beast on the tile + give it 1px in its new direction
				 */
				x = getNextX(newX);
				y = getNextY(newY);
								
			} else if (maze.canGo(newX, newY, dir)) {
				x = newX; y = newY;
			} else { // can't go in desired direction
				
				if(maze.isCross(maze.getBoardX(newX), maze.getBoardY(newY))) {
					System.err.println("Crossing");
					dir = getNextDirection();
					if (maze.canGo(x, y, dir)){
						x = getNextX(newX);
						y = getNextY(newY);
					} else {} // the desired direction is not available -> do nothing
				} else { // not a crossing, a turn
					System.err.println("Turn");
					dir = maze.getNextDir(newX, newY, dir);
					x = getNextX(newX);
					y = getNextY(newY);
				}
				
			}
			
		} else { // line not crossed -> continue normally
			x = newX; y = newY;
		}
		
	}
	
	/**
	 * Updates the direction of movement. Specific for each creature, so abstract here
	 * Overrider must change the dirToBeChanged variable, when changing direction
	 */
	public abstract void updateDirection();
	
	/**
	 * Returns the next direction when on a crossing and can't go in desired direction
	 * @return second most wanted direction
	 */
	protected abstract direction getNextDirection();
	
	/**
	 * Draw itself into the supplied graphics variable
	 * @param g graphics for drawing
	 */
	public void drawSelf(Graphics2D g){
		
		/**
		 * Calculate the position of the top left corner
		 */
		int top = y - HALF_HEIGHT;
		int side = x - HALF_WIDTH;
		//System.err.println("x: " + x + "y: " + y);
		
		/**
		 * Pick the right image
		 */
		switch(dir) {
			case UP:
				cur = up; break;
			case DOWN:
				cur = down; break;
			case LEFT:
				cur = left; break;
			case RIGHT:
				cur = right; break;				
		}
		
		g.drawImage(cur,null,side,top);
	}
	
	private int getNextX(int x){
		return maze.getCenterX(x) + dir.getXDelta();
	}
	
	private int getNextY(int y){
		return maze.getCenterY(y) + dir.getYDelta();
	}
	
}
