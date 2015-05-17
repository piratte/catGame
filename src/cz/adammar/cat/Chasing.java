package cz.adammar.cat;

/**
 * Class implementing one of the dog creatures. Tries to chase the player by going right for him.
 * @author user
 *
 */
public class Chasing extends Dog {

	private static final int SD_UPGRD = 100; 
	protected Chasing(int x, int y, int speed, int limit, int sd, ImgLoader imgLoader, Maze maze) {
		super(x,y,speed,limit, sd + SD_UPGRD, imgLoader, maze);
		
	}

	@Override
	public void updateDirection() {
		if (playerIsTooFar()) 
			nextDir = direction.getRandom(dir);
		else {
			if (mistake()) {
				nextDir = direction.getRandom(dir);
			} else 
				nextDir = direction.getCollisionCourse(x, y, maze.player.getX(), maze.player.getY());
		}
	}

	@Override
	protected direction getNextDirection() {
		return direction.getAnotherCollisionCourse(x, y, maze.player.getX(), maze.player.getY());
	}

}
