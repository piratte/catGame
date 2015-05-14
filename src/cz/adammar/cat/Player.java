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
		next = DEF_DIR;	
	}
	
	public void setDirection(direction next){
		this.next = next;
	}
	
	@Override
	public void updateDirection() {
		if (dir != next){
			nextDir = next;
		}
	}

	@Override
	protected direction getNextDirection() {
		return next;
	}
	
	public int getX() {
		return x;
	}
	public int getY(){
		return y;
	}
}
