package cz.adammar.cat;

/**
 * Class implementing one of the dog creatures. Tries to flank the player.
 * @author user
 *
 */
public class Flanking extends Dog {
	
	/**
	 * Coefficient determining, how much the dog is anticipating the player movement
	 */
	private final int FLANKING_COEF = 30;

	protected Flanking(int x, int y, int speed, int limit, int sd, ImgLoader imgLoader, Maze maze) {
		super(x,y,speed,limit, sd, imgLoader, maze);
	}

	@Override
	public void updateDirection() {
		direction d = maze.player.getDirection();
		nextDir = direction.getCollisionCourse(x, y, 
				maze.player.getX() + d.getXDelta() * FLANKING_COEF, 
				maze.player.getY() + d.getYDelta() * FLANKING_COEF);

	}

	@Override
	protected direction getNextDirection() {
		direction d = maze.player.getDirection();
		return direction.getAnotherCollisionCourse(x, y, 
				maze.player.getX() + d.getXDelta() * FLANKING_COEF, 
				maze.player.getY() + d.getYDelta() * FLANKING_COEF);
	}

}
