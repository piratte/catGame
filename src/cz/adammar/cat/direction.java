package cz.adammar.cat;

/**
 * Enum for directions
 * @author madam
 *
 */
public enum direction {
	UP(0,1), 
	DOWN(0,-1), 
	LEFT(-1,0), 
	RIGHT(1,0);
	
	private int dX, dY;
	
	private direction(int dX, int dY){
		this.dX = dX;
		this.dY = dY;
	}
	
	public int getXDelta(){
		return dX;
	}
	
	public int getYDelta(){
		return dY;
	}
}
