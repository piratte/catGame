/**
 * 
 */
package cz.adammar.cat;

//import java.awt.Color;
//import java.awt.Dimension;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * 
 * The class of the game panel
 * 
 * The main logic is located here and is started as a new thread
 * 
 * @author madam
 *
 */
@SuppressWarnings("serial")
public class GamePanel  extends JPanel implements Runnable {

	/**
	 * Size of one piece in the game (width of the path)
	 */
	public static final int PIECE_WIDTH = 20;
	public static final int PIECE_HEIGHT = 20;
	/**
	 * Num of frames without sleeping, before the thread yields to other threads
	 */
	private static final int NO_DELAYS_PER_YIELD = 16;
	
	/**
	 * Number of frames that can be updated and not rendered
	 */
	private static int MAX_FRAME_SKIPS = 2;
	
	/**
	 * Width of panel
	 */
	private static final int PWIDTH = 600;   
	
	/**
	 * Height of panel
	 */
	private static final int PHEIGHT = 600;
	
	/**
	 * imageList filename
	 */
	private static final String IMGLIST = "imageList.txt";
	
	/**
	 * File name of wall image
	 */
	private static final String WALL_IMG = "wall.png";
	
	/**
	 * 
	 */
	private static final String WELCOME_MSG = "Welcome to the Cat game. You are the cat, you're task is to catch the mouse while avoiding the dogs. Good luck! Start first round?";
	/**
	 * Initial speed
	 */
	private static final int START_SPEED = 10;
	
	/**
	 * Animation thread
	 */
	private Thread animator;
	
	/**
	 * the graphics element of the gameImage, serving the methods for drawing on
	 * gameImage (Graphics2D is used for faster drawing)
	 */
	private Graphics2D gameGraphics;
	
	/**
	 * Background image
	 */
	private BufferedImage bckg;
	
	/**
	 * Object for loading images
	 */
	//private ImageLoader imgLoader;
	private ImgLoader imgLoader;
	
	private Image dbImage = null;
	
	/**
	 * Stops animation
	 */
	private boolean running = false;
	
	/**
	 * Stops game
	 */
	private boolean gameOver = false;
	
	/**
	 * signal to the updating method, that the game is paused
	 */
	private boolean isPaused = false;
	
	/**
	 * Number of levels;
	 */
	private int level = 0;
	
	/**
	 * Time between game updates in nanoseconds 
	 */
	private long period;
	
	/**
	 * Time, when the game was updated previously
	 */
	private long lastUpdate;
	
	/**
	 * class remembering the map and the positions of the beasts
	 */
	private Maze maze;
	
	/**
	 * Direction the user wants to move
	 */
	private direction wantedDir = direction.RIGHT;
	
	/**
	 * Speed of beasts
	 */
	public int speed = START_SPEED;
	
	/**
	 * The players beast
	 */
	private Player player;
	

	/**
	 * Constructor
	 * 
	 * The maze and the beasts will be initialized 
	 */
	public GamePanel(long period)
	{
		this.period = period;
		setBackground(Color.white);
		setPreferredSize( new Dimension(PWIDTH, PHEIGHT));
		
		setFocusable(true);
		requestFocus();
		
		//imgLoader = new ImageLoader(IMGLIST);
		
		imgLoader = new ImgLoader();
		
		maze = new Maze();
		bckg = maze.getMap(imgLoader.loadImage(WALL_IMG), PWIDTH, PHEIGHT);

		player = new Player(maze.getPlayerX(), maze.getPlayerY(), speed, imgLoader, maze);
		maze.addBeast(player,1);
		// create dogs
		// create mouse
		// create player
		
		
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				processKeyPress(e);
			}
		});

	}  // end of GamePanel()
	
	
	/**
	 * Process key press
	 * 
	 * Processes key press and sets appropriate variable
	 * 
	 * @param e the key event
	 */
	private void processKeyPress(KeyEvent e) {
		int key = e.getKeyCode();
		
		switch(key){
			case KeyEvent.VK_UP:
			case KeyEvent.VK_W:
				wantedDir = direction.UP; break;
			
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S:
				wantedDir = direction.DOWN; break;
				
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A:
				wantedDir = direction.LEFT; break;
				
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
				wantedDir = direction.RIGHT; break;
				
			case KeyEvent.VK_PAUSE:
			case KeyEvent.VK_P:
				if (isPaused){
					resumeGame();
				} else {
					pauseGame();
				} 
				break;
				
			case KeyEvent.VK_ESCAPE:
			case KeyEvent.VK_Q:
				stopGame(); break;
		}
		
	}

	
	
	/**
	 * Called when the panel is added to the main window and starts the game
	 */
	public void addNotify()
	{
		super.addNotify();   // creates the peer
		startGame();         // start the thread
	}
	
	/**
	 * Initialize and start the thread
	 */
	private void startGame()
	{
		if (animator == null || !running) {
			animator = new Thread(this);
			animator.start();
		}
	} // end of startGame()
	
	/**
	 * called by the user to stop execution
	 */
	public void stopGame() {  
			running = false;        
	}
	
	/**
	 * pause game updates
	 */
	public void pauseGame() {
		isPaused = true;
	}
	
	/**
	 * resume game updates
	 */
	public void resumeGame() {
		isPaused = false;
	}

	/**
	 * Creates a message window which asks the user, whether he wants to proceed with playing 
	 * @return true if user wants to play, false otherwise
	 */
	public boolean startRound() {
		int i;
		String options[] = {"Yes", "No"};
		
		if (level > 0){	
			i = JOptionPane.showOptionDialog(this, 
					"A next round is about to start, are you ready?", "New Round", 
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, 
					options, options[0]);
		} else {
			i = JOptionPane.showOptionDialog(this,
					WELCOME_MSG, "New Game", 
					JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE, null, 
					options, options[0]);
			lastUpdate = System.nanoTime();
		}
		/**
		 * YES ~ 0, we want to return true if yes, no otherwise
		 */
		return i==0;

	}
	
	@Override
	public void run() {
		
		long beforeTime, timeDiff, sleepTime, afterTime;
		long overSleepTime = 0L;
		long excess = 0L;
		int noDelays = 0;
		int skips = 0;
		beforeTime = System.nanoTime();		
		
		running = startRound();
		while(running) 
		{
			gameUpdate();   // game state is updated
			gameRender();   // render to a buffer
			paintScreen();  // paint buffer to screen
			
			afterTime = System.nanoTime();
			timeDiff = afterTime - beforeTime;
			sleepTime = (period - timeDiff) - overSleepTime;   // time left in this loop in ns
			debug("sleepTime: " + sleepTime + " timeDiff: " + timeDiff + " overSleepTime: " + overSleepTime + " period: " + period);
			if (sleepTime > 0)   // update/render took longer than period
			{	
			
				try {
					debug("Sleeping for: " + sleepTime / 1000000L + " -----------------------------------");
					Thread.sleep(sleepTime / 1000000L);  
				} 
				catch(InterruptedException ex) {}
				finally {
					
					/**
					 * Fixing the inaccuracies of the sleep command
					 */
					overSleepTime = (System.nanoTime() - afterTime) - sleepTime; 
				}
			} else {
				excess -= sleepTime;
				overSleepTime = 0;
				
				/**
				 * If this thread is running for too long without yielding, fix it 
				 */
				if (++noDelays >= NO_DELAYS_PER_YIELD){
					noDelays = 0;
					debug("Yielding --------------------------------------");
					Thread.yield();
				}
			}
			
			beforeTime = System.nanoTime();
			
			/**
			 * Updating game state without rendering in case the animation takes too long
			 */
			skips = 0;
			while ((excess > period) && (skips++ < MAX_FRAME_SKIPS)) {
				excess -= period;
				gameUpdate(); 
			}
		}
		System.exit(0);
	} // end of run()

	
	/**
	 * Method for active rendering
	 * 
	 * Actively render the buffer image to the screen
	 */
	private void paintScreen()
	{
		Graphics2D g;
		try {
			g = (Graphics2D) this.getGraphics();  // get the panel’s graphic context
			if ((g != null) && (dbImage != null))
				g.drawImage(dbImage, 0, 0, null);
			g.dispose();
		}
		catch (Exception e)
		{ 
			System.out.println("Graphics context error: " + e);
		}
	} // end of paintScreen()

	/**
	 * Update game state
	 */
	private void gameUpdate()
	{
		debug("update");
		/**
		 * if game is paused, this method does nothing
		 */
		if (gameOver && isPaused) 
			return;
		
		player.setDirection(wantedDir);
			
		long now = System.nanoTime();
		long interval = now - lastUpdate;
		
		for(Beast b : maze.beasts) {
			if (b == null)
				continue;
			b.updateDirection();
			b.move(interval);
		}
		
		// TODO: check if player is eaten
		
		// TODO: check if player got the mouse
		
		// TODO: if gameOver, call method to show dialog
		
		lastUpdate = now;

	}

	
	
	/**
	 * draw the current frame to an image buffer
	 */
	private void gameRender()
	{
		debug("render");
		if (dbImage == null) {  // create the buffer
			dbImage = createImage(PWIDTH, PHEIGHT);
			if (dbImage == null) {
				System.out.println("dbImage is null");
				return;
			}
			else
				gameGraphics = (Graphics2D) dbImage.getGraphics();
		}
		
		/**
		 * Draw background
		 */
		//gameGraphics.setColor(Color.white);
		gameGraphics.drawImage(bckg, null, 0, 0);
		
		
		for (Beast b : maze.beasts){
			if (b == null)
				continue;
			b.drawSelf(gameGraphics);
		}
	
		if (gameOver)
			gameOverMessage(gameGraphics);
	}  // end of gameRender()
	
	
	private void gameOverMessage(Graphics g)
	// center the game-over message
	{
		//g.drawString(msg, x, y);
	}  // end of gameOverMessage()
	// more methods, explained later
	
	private final boolean deb = true;
	private void debug(String s) {
		if (deb)
			System.err.println("DEBUG: " + s);
	}

}
