package cz.adammar.cat;

public class Chasing extends Dog {

	protected Chasing(int x, int y, int speed, ImgLoader imgLoader, Maze maze) {
		super(x, y, speed, imgLoader, maze);
		
	}

	@Override
	public void updateDirection() {
		nextDir = direction.getCollisionCourse(x, y, maze.player.getX(), maze.player.getY());

	}

	@Override
	protected direction getNextDirection() {
		return direction.getAnotherCollisionCourse(x, y, maze.player.getX(), maze.player.getY());
	}

}
