package cz.adammar.cat;

import java.util.Random;

/**
 * Enum for directions
 * @author madam
 *
 */
public enum direction {
	UP(0,-1), 
	DOWN(0,1), 
	LEFT(-1,0), 
	RIGHT(1,0);
	
	private int dX, dY;
	
	private static Random rand = new Random();
	
	private direction(int dX, int dY){
		this.dX = dX;
		this.dY = dY;
	}
	
	/**
	 * Get the opposite direction for input
	 * @param d input direction
	 * @return opposite to the input direction
	 */
	public direction getOpposite(){
		switch(this){
			case UP:
				return DOWN;
			case DOWN:
				return UP;
			case LEFT:
				return RIGHT;
			default: // = RIGHT (switch in this case has to have a default
				return LEFT;
		}
	}
	
	/**
	 * Get the coefficient to x coordinate, when traveling this direction
	 * @return 1 if going down, -1 if going up, 0 otherwise
	 */
	public int getXDelta(){
		return dX;
	}
	
	/**
	 * Get the coefficient to x coordinate, when traveling this direction
	 * @return 1 if going right, -1 if going left, 0 otherwise
	 */
	public int getYDelta(){
		return dY;
	}
	
	/**
	 * Return a random direction
	 * @param dir 
	 * @return random direction
	 */
	public static direction getRandom(direction dir){
		int ind =  rand.nextInt(4);
		if (ind == dir.getOpposite().ordinal())
			ind = (ind+1)%4;
		System.err.println("ind: " + ind);
		return values()[ind];
	}
	
	/**
	 * Get the direction from objects position to target position
	 * @param myX object horizontal coordinate 
	 * @param myY object vertical coordinate
	 * @param endX target horizontal coordinate
	 * @param endY target vertical coordinate
	 * @return direction towards target
	 */
	public static direction getCollisionCourse(int myX, int myY, int endX, int endY){
		
		/**
		 * Counting the vector coordinates to target
		 */
		int vecX = endX - myX;
		int vecY = endY - myY;
		
		/**
		 * Examine the vector coordinates and return the correct direction
		 */
		if (Math.abs(vecX)>Math.abs(vecY)) {
			if (vecX > 0)
				return RIGHT;
			else
				return LEFT;
		} else {
			if(vecY > 0)
				return DOWN;
			else 
				return UP;
		}
	}
	
	/**
	 * Get the second best direction towards the target
	 * @param myX object horizontal coordinate 
	 * @param myY object vertical coordinate
	 * @param endX target horizontal coordinate
	 * @param endY target vertical coordinate
	 * @return second best direction towards target
	 */
	public static direction getAnotherCollisionCourse(int myX, int myY, int endX, int endY){
		int vecX = endX - myX;
		int vecY = endY - myY;
		
		if (Math.abs(vecX)>Math.abs(vecY)) {
			if (vecY > 0)
				return DOWN;
			else
				return UP;
		} else {
			if(vecX > 0)
				return RIGHT;
			else 
				return LEFT;
		}
	}
}
