package cz.adammar.cat;

public class Player extends Beast {
	
	//private final direction DEF_DIR = direction.RIGHT;
	
	private direction next;

	public Player(int x, int y, int speed, ImgLoader imgLoader, Maze maze) {
		super(x,y,speed,maze);
		
		left = imgLoader.loadImage("catLeft.png");
		right = imgLoader.loadImage("catRight.png");
		up = imgLoader.loadImage("catUp.png");
		down = imgLoader.loadImage("catDown.png");
		next = dir;	
	}
	
	/**
	 * Update the variable next accordingly to the user actions
	 * @param next Direction wanted by user
	 */
	public void setDirection(direction next){
		this.next = next;
	}
	
	/**
	 * Update direction from user commands. 
	 */
	@Override
	public void updateDirection() {
		
		/**
		 * The variable next is updated accordingly to the user actions
		 */
		if (dir != next){
			nextDir = next;
		}
	}

	@Override
	protected direction getNextDirection() {
		return next;
	}
	
	/**
	 * Return the horizontal coordinate
	 * @return Players x coordinate
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Return the vertical coordinate
	 * @return Players y coordinate
	 */
	public int getY(){
		return y;
	}
}
