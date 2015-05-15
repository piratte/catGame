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
		// TODO Auto-generated method stub
		nextDir = direction.getCollisionCourse(x, y, maze.player.getX(), maze.player.getY()).getOpposite();
		
		
	}

	@Override
	protected direction getNextDirection() {
		// TODO Auto-generated method stub
		return direction.getAnotherCollisionCourse(x, y, maze.player.getX(), maze.player.getY()).getOpposite();
	}

}
