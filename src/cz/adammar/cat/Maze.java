/**
 * 
 */
package cz.adammar.cat;

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
	 * Array for storing the wall positions 
	 */
	private Boolean[][] board = new Boolean[DEF_SIZE_X][DEF_SIZE_Y];
	
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
				 * parsing the selected level
				 */

				// if the first character is not a number, than the whole line doesn't contain a number
				if (Character.isDigit(line.charAt(0)) && Integer.parseInt(line)==levelNum){
					
					for	(int y = 0; y<DEF_SIZE_Y; ++y){
						for (int x = 0; x<DEF_SIZE_X; ++x) {
							board[x][y]= line.charAt(x) == 'X' ? true : false;
						}
					}
					
					parsed = true;
				}
			}
			
		} catch (IOException e) {
			System.err.println("Maze constructor: problem parsing line" + e.getMessage());
		} catch (NumberFormatException e) {
			System.err.println("Maze constructor: wrong input file format");
			System.exit(-1); //TODO: OK?
		}
	}
	
	/**
	 * Just a wrapper for the parameterized constructor (adds the default level number)
	 */
	public Maze(){
		this(DEF_LEVEL_NUM);
	}
	
}
