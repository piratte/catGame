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

	}

	@Override
	protected direction getNextDirection() {
		// TODO Auto-generated method stub
		return null;
	}

}
