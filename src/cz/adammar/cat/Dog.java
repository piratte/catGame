package cz.adammar.cat;

public abstract class Dog extends Beast {

	protected Dog(int x, int y, int speed, int limit, int sd, ImgLoader imgLoader, Maze maze){
		super(x,y,speed,limit, sd, maze);
		
		left = imgLoader.loadImage("dogLeft.png");
		right = imgLoader.loadImage("dogRight.png");
		up = imgLoader.loadImage("dogUp.png");
		down = imgLoader.loadImage("dogDown.png");
	}

}
