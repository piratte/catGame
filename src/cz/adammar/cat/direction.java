package cz.adammar.cat;

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
	
	private direction(int dX, int dY){
		this.dX = dX;
		this.dY = dY;
	}
	
	/**
	 * Get the opposite direction for input
	 * @param d input direction
	 * @return opposite to the input direction
	 */
	public direction getOpposite(direction d){
		switch(d){
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
}
