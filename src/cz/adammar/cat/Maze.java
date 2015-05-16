/**
 * 
 */
package cz.adammar.cat;

import java.awt.image.BufferedImage;
//import java.awt.Component;
import java.awt.Graphics2D;
import java.io.*;

/**
 * 
 * Class for the map maze
 * 
 * @author madam
 *
 */
public class Maze {
	
	/**
	 * The default level number used in the plain constructor
	 */
	private static final int DEF_LEVEL_NUM = 2;
	
	/**
	 * Name of the maze configuration file
	 */
	private static final String CONF_FILE_NAME = "conf.d/Mapy.txt";
	
	/**
	 * The dafult size of the board
	 */
	private static final int DEF_SIZE_X = 30;
	private static final int DEF_SIZE_Y = 30;
	
	/**
	 * Default image size
	 */
	private static final int WIDTH = GamePanel.PIECE_WIDTH;
	private static final int HEIGHT = GamePanel.PIECE_WIDTH;
	
	/**
	 * Array for storing the wall positions, board[x][y] == true if wall is on x,y
	 */
	private Boolean[][] board = new Boolean[DEF_SIZE_X][DEF_SIZE_Y];
	
	/**
	 * Array of beasts
	 */
	public Beast[] beasts = new Beast[4];
	
	public Player player;
	
	private int dog1x, dog1y, dog2x, dog2y, mouseX, mouseY, playerX, playerY;
	
	/**
	 * Constructor: will read the map file, get the correct level, read the input, 
	 * and initialize the data structure accordingly
	 */
	public Maze(int levelNum){
		
		/**
		 * Reading the conf file
		 */
		try (BufferedReader br = new BufferedReader(new FileReader(CONF_FILE_NAME))) {
			String line; Boolean parsed = false;
			while (!parsed){
				line = br.readLine();
				
				/**
				 * Omit empty line
				 */
				if (line.isEmpty())
					continue;
				
				/**
				 * parsing the selected level
				 */
				// if the first character is not a number, than the whole line doesn't contain a number
				if (Character.isDigit(line.charAt(0)) && Integer.parseInt(line.trim())==levelNum){
					for	(int y = 0; y<DEF_SIZE_Y; ++y){
						parseLine(y,br.readLine());
					}
					
					parsed = true;
				}
			}
			
		} catch (IOException e) {
			System.err.println("Maze constructor: problem parsing line" + e.getMessage());
		} catch (NumberFormatException e) {
			System.err.println("Maze constructor: wrong input file format " + e.getMessage());
			System.exit(-1); //TODO: OK?
		}
	}
	
	/**
	 * Just a wrapper for the parameterized constructor (adds the default level number)
	 */
	public Maze(){
		this(DEF_LEVEL_NUM);
	}
	
	/**
	 * The maze will setup the status for the next round
	 * @param speed beasts speed for this round
	 * @param limit beasts limit for this round
	 * @param seeingDist beasts seeing distance for this round
	 * @param imgLoader image loader class
	 */
	public void newGame(int speed, int limit, int seeingDist, ImgLoader imgLoader){
		player = new Player(getPlayerX(), getPlayerY(), speed, imgLoader, this);
		addBeast(player,0);
		addBeast(new Mouse(getMouseX(), getMouseY(), speed, limit, seeingDist, imgLoader, this), 1);
		addBeast(new Chasing(getDog1X(), getDog1Y(), speed, limit, seeingDist, imgLoader, this), 2);
		addBeast(new Flanking(getDog2X(), getDog2Y(), speed, limit, seeingDist, imgLoader, this), 3);
	}
	
	public int getDog1X(){
		return getRealCoo(dog1x, WIDTH);
	}
	public int getDog1Y(){
		return getRealCoo(dog1y, HEIGHT);
	}
	
	public int getDog2X(){
		return getRealCoo(dog2x, WIDTH);
	}
	public int getDog2Y(){
		return getRealCoo(dog2y, HEIGHT);
	}
	
	public int getMouseX(){
		return getRealCoo(mouseX, WIDTH);
	}
	public int getMouseY(){
		return getRealCoo(mouseY, HEIGHT);
	}
	
	public int getPlayerX(){
		return getRealCoo(playerX, WIDTH);
	}
	public int getPlayerY(){
		return getRealCoo(playerY, HEIGHT);
	}
	
	/**
	 * Add beast to maze
	 * @param Beast to add
	 */
	public void addBeast(Beast b, int ind){
		beasts[ind] = b;
	}
	
	/**
	 * Return image of background
	 * @param pheight 
	 * @param pwidth 
	 * @param Wall image of wall
	 * @return Background image of the maze map 
	 */
	public BufferedImage getMap(BufferedImage wall, BufferedImage floor, int pwidth, int pheight){
		int top;
		int left;
		BufferedImage out = new BufferedImage(pwidth, pheight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = (Graphics2D) out.getGraphics(); 
			
		for (int x = 0; x < DEF_SIZE_X; ++x){
			for (int y = 0; y < DEF_SIZE_Y; ++y) {
				top = y * HEIGHT; left = x * WIDTH;
				if (board[x][y]) 
					g2d.drawImage(wall,null,left,top);
				else
					g2d.drawImage(floor,null,left,top);
			}
		}
		g2d.dispose();
		return out;
	}
	
	/**
	 * Check, if given field is a cross (e.g. if movement in more than two directions is valid)
	 * @param x board coordinate
	 * @param y board coordinate
	 * @return true, if field is cross road, false otherwise
	 */
	public boolean isCross(int x, int y){
		int score = 4;
		
		if (board[x][y-1])
			--score;
		if (board[x][y+1])
			--score;
		if (board[x-1][y])
			--score;
		if (board[x+1][y])
			--score;
		
		return score < 2;
	}
	
	/**
	 * Transforms horizontal window coordinates to board coordinates
	 * @param x window coordinate
	 * @return panel coordinate
	 */
	public int getBoardX(int x){
		return getBoardCoo(x,WIDTH);
	}
	
	/**
	 * Transforms horizontal window coordinates to board coordinates
	 * @param y window coordinate
	 * @return panel coordinate
	 */
	public int getBoardY(int y){
		return getBoardCoo(y,HEIGHT);
	}
	
	/**
	 * Get the x coordinate of the tile center of the tile, on which the input x coordinate lies
	 * @param x window coordinate
	 * @return the x coordinate of the tile center
	 */
	public int getCenterX(int x){
		return getRealCoo( getBoardX(x), WIDTH);
	}
	
	/**
	 * Get the y coordinate of the tile center of the tile, on which the input y coordinate lies
	 * @param y window coordinate
	 * @return the y coordinate of the tile center
	 */
	public int getCenterY(int y){
		return getRealCoo( getBoardY(y), HEIGHT);
	}
	
	/**
	 * Check, if the line in the middle of the aisle was crossed with last move
	 * @param oldX old X panel coordinate
	 * @param newX new X panel coordinate
	 * @return x coordinate of the line, -1 if no line was crossed
	 */
	public int crossedXLine(int oldX, int newX) {
		return crossedLine(oldX, newX, WIDTH);
	}
	
	/**
	 * Check, if the line in the middle of the aisle was crossed with last move
	 * @param oldY old Y coordinate
	 * @param newY new Y coordinate
	 * @return y coordinate of the line, -1 if no line was crossed
	 */
	public int crossedYLine(int oldY, int newY) {
		return crossedLine(oldY, newY, HEIGHT);
	}
	

	/**
	 * Check, if from the current possition (tile) you can change direction
	 * @param x coordinate x in pixels
	 * @param y coordinate y in pixels
	 * @param wishedDir wanted direction
	 * @return true, if from the current tile, there is a way in the wanted direction, false otherwise
	 */
	public boolean canGo(int x, int y, direction wishedDir){
		int tileX = getBoardX(x); int tileY = getBoardY(y);
		//System.err.println("for coo: " + x + " " + y + " Got tile: " + tileX + " " + tileY);
		return isPossibleDirection(tileX, tileY, wishedDir);
	}

	/**
	 * When in a turn, get some other direction in which you can leave the turn
	 * @param x
	 * @param y
	 * @param oldDir
	 * @return new possible direction
	 */
	public direction getNextDir(int x, int y, direction oldDir){
		for (direction d : direction.values()){
			if (d == oldDir || d == oldDir.getOpposite()) 
				continue;
			if (canGo(x,y,d))
				return d;
		}
		// shouldn't be necessary
		return oldDir.getOpposite();
	}
	
	//======================= private methods =================================
	
	/**
	 * Check if on tile next to input tile in direction is a wall
	 * @param tileX coordinate x of initial tile
	 * @param tileY coordinate y of initial tile
	 * @param dir wanted direction
	 * @return true if there's no wall, false otherwise
	 */
	private boolean isPossibleDirection(int tileX, int tileY, direction dir) {
		return !board[tileX + dir.getXDelta()][tileY + dir.getYDelta()];
	}
	
	/**
	 * Get panel coordinate from board coordinate
	 * @param coo coordinate
	 * @param size width or height
	 * @return middle of the board coordinate
	 */
	private int getRealCoo(int coo, int size){
		return (coo * size) + (size / 2);
	}
	
	/**
	 * Serves the getBoardX and getBoardY methods
	 * @param coo coordinate
	 * @param size width or height
	 * @return panel coordinate
	 */
	private int getBoardCoo(int coo, int size){
		return 	(coo / size);
	}
	
	/**
	 * Serves the crossedXLine and crossedYLine
	 * @param oldCoo old panel coordinate
	 * @param newCoo new panel coordinate
	 * @param size width or height
	 * @return coordinate of the line, -1 if no line was crossed
	 */
	private int crossedLine(int oldCoo, int newCoo, int size){
		/**
		 * board coordinate of the tile we are on
		 */
		int tile = getBoardCoo(oldCoo, size);
		
		/**
		 * real coordinate of the middle of the tile we are on
		 */
		int line = tile*size + (size/2);
		
		//System.err.println("old: " + oldCoo + " new: " + newCoo + " line: " + line);
		
		/**
		 * If one of the X coordinates is on the other side of the line than the other, the
		 * multiplication will come out negative (-> line was crossed)
		 */
		if ((oldCoo < line && newCoo >= line) || (oldCoo > line && newCoo <= line)) { 
			//System.err.println("line"); 
			return line;
		} else {
			//System.err.println("not-line");
			return -1;
		}
	}
	
	/**
	 * Parse one line of the maze map
	 * @param x horizontal coordinate of the maps line
	 * @param line the actual line
	 */
	private void parseLine(int x, String line) {
		
		char cur;
		for (int y = 0; y<DEF_SIZE_Y; ++y) {
			cur = line.charAt(y);
			switch (cur) {
				case 'X' :
				case 'x' :
					board[x][y] = true;
					break;
				case 'p':
				case 'P':
					playerX = y; playerY = x;
					board[x][y] = false;
					break;
				case 'm':
				case 'M':
					mouseX = y; mouseY = x;
					board[x][y] = false;
					break;
				case '1':
					dog1x = y; dog1y = x;
					board[x][y] = false;
					break;
				case '2':
					dog2x = y; dog2y = x;
					board[x][y] = false;
					break;
				default:
					board[x][y] = false;
			}
		}
	}
	
}
