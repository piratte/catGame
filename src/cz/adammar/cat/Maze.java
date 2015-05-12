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
	private static final int WIDTH = 20;
	private static final int HEIGHT = 20;
	
	/**
	 * Array for storing the wall positions, board[x][y] == true if wall is on x,y
	 */
	private Boolean[][] board = new Boolean[DEF_SIZE_X][DEF_SIZE_Y];
	
	/**
	 * Array of beasts
	 */
	public Beast[] beasts = new Beast[4];
	
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
	
	public int getDog1X(){
		return dog1x;
	}
	public int getDog1Y(){
		return dog1y;
	}
	
	public int getDog2X(){
		return dog2x;
	}
	public int getDog2Y(){
		return dog2y;
	}
	
	public int getMouseX(){
		return mouseX;
	}
	public int getMouseY(){
		return mouseY;
	}
	
	public int getPlayerX(){
		return playerX;
	}
	public int getPlayerY(){
		return playerY;
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
	public BufferedImage getMap(BufferedImage wall, int pwidth, int pheight){
		int top;
		int left;
		BufferedImage out = new BufferedImage(pwidth, pheight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = (Graphics2D) out.getGraphics(); 
		
		for (int y = 0; y < DEF_SIZE_Y; ++y){
			for (int x = 0; x < DEF_SIZE_X; ++x) {
				if (board[x][y]) {
					top = x * WIDTH; left = y * HEIGHT;
					g2d.drawImage(wall,null,left,top);
				}
			}
		}
		g2d.dispose();
		return out;
	}
	//======================= private methods =================================
	
	
	
	private void parseLine(int y, String line) {
		
		char cur;
		for (int x = 0; x<DEF_SIZE_X; ++x) {
			cur = line.charAt(x);
			switch (cur) {
				case 'X' :
				case 'x' :
					board[x][y] = true;
					break;
				case 'p':
				case 'P':
					playerX = x; playerY = y;
					board[x][y] = false;
					break;
				case 'm':
				case 'M':
					mouseX = x; mouseY = y;
					board[x][y] = false;
					break;
				case '1':
					dog1x = x; dog1y = y;
					board[x][y] = false;
					break;
				case '2':
					dog2x = x; dog2y = y;
					board[x][y] = false;
					break;
				default:
					board[x][y] = false;
			}
		}
	}
	
}
