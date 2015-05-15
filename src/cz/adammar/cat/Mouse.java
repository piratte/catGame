package cz.adammar.cat;

public class Mouse extends Beast {
	
	public Mouse(int x, int y, int speed, ImgLoader imgLoader, Maze maze){
		super(x,y,speed,maze);
		
		left = imgLoader.loadImage("mouseLeft.png");
		right = imgLoader.loadImage("mouseRight.png");
		up = imgLoader.loadImage("mouseUp.png");
		down = imgLoader.loadImage("mouseDown.png");
	}

	@Override
	public void updateDirection() {
		
		/**
		 * If the player is too far, get a random direction. Otherwise go directly away from him
		 */
		if (playerIsTooFar()) 
			nextDir = direction.getRandom(dir);
		else 
			nextDir = direction.getCollisionCourse(x, y, maze.player.getX(), maze.player.getY()).getOpposite();
	}

	@Override
	protected direction getNextDirection() {
		
		/**
		 * Get the second best retreating direction
		 */
		return direction.getAnotherCollisionCourse(x, y, maze.player.getX(), maze.player.getY()).getOpposite();
	}

}
