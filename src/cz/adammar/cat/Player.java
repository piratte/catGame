package cz.adammar.cat;

public class Player extends Beast {
	
	private final direction DEF_DIR = direction.RIGHT;

	
	public Player(int x, int y, ImageLoader imgLoader, Maze maze) {
		this.x = x; this.y = y;
		left = imgLoader.loadImage("catLeft.png");
		right = imgLoader.loadImage("catRight.png");
		up = imgLoader.loadImage("catUp.png");
		down = imgLoader.loadImage("catDown.png");
		dir = DEF_DIR;
	}
	
	@Override
	public void move() {
		// TODO Auto-generated method stub

	}

}
