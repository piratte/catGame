package cz.adammar.cat;

public class Player extends Beast {
	
	private final direction DEF_DIR = direction.RIGHT;
	
	private direction nextDir;

	public Player(int x, int y, int speed, ImgLoader imgLoader, Maze maze) {
		this.x = x; this.y = y; this.speed = speed;
		left = imgLoader.loadImage("catLeft.png");
		right = imgLoader.loadImage("catRight.png");
		up = imgLoader.loadImage("catUp.png");
		down = imgLoader.loadImage("catDown.png");
		dir = DEF_DIR;		
	}
	
	public void setDirection(direction next){
		nextDir = next;
	}
	
	public void updateDirection() {
		if (dir != nextDir){
			dir = nextDir;
			dirToBeChanged = true;
		}
	}

}
