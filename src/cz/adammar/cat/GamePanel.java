/**
 * 
 */
package cz.adammar.cat;

//import java.awt.Color;
//import java.awt.Dimension;
import java.awt.*;
import java.awt.event.*;

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
	 * Num of frames without sleeping, before the thread yields to other threads
	 */
	private static final int NO_DELAYS_PER_YIELD = 16;
	
	/**
	 * Number of frames that can be updated and not rendered
	 */
	private static int MAX_FRAME_SKIPS = 5;
	
	/**
	 * Width of panel
	 */
	private static final int PWIDTH = 500;   
	
	/**
	 * Height of panel
	 */
	private static final int PHEIGHT = 400;
	
	/**
	 * imageList filename
	 */
	private static final String IMGLIST = "defaultList.txt";
	
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
	 * Object for loading images
	 */
	private ImageLoader imgLoader;
	
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
	 * Time between game updates in nanoseconds 
	 */
	private long period;
	
	/**
	 * class remembering the map and the positions of the beasts
	 */
	Maze maze;
	

	/**
	 * Constructor
	 * 
	 * The maze and the beasts will be initialized 
	 */
	public GamePanel()
	{
		setBackground(Color.white);
		setPreferredSize( new Dimension(PWIDTH, PHEIGHT));
		
		setFocusable(true);
		requestFocus();
		
		imgLoader = new ImageLoader(IMGLIST);
		
		
		maze = new Maze();
		
		// create dogs
		// create mouse
		// create player
		
		
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				processKeyPress(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				processKeyRelease(e);
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
		// TODO Auto-generated method stub
		
	}
	
	
	/**
	 * Process key release
	 * 
	 * Evaluate the key release action and change the state of the game accordingly 
	 * 
	 * @param e the key event
	 */
	private void processKeyRelease(KeyEvent e) {
		// TODO Auto-generated method stub
		
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
	public void stopGame()
	{  
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


	@Override
	public void run() {
		
		long beforeTime, timeDiff, sleepTime, afterTime;
		long overSleepTime = 0L;
		long excess = 0L;
		int noDelays = 0;
		int skips = 0;
		beforeTime = System.nanoTime();
		
		running = true;
		while(running) 
		{
			gameUpdate();   // game state is updated
			gameRender();   // render to a buffer
			paintScreen();  // paint buffer to screen
			
			afterTime = System.nanoTime();
			timeDiff = afterTime - beforeTime;
			sleepTime = (period - timeDiff) - overSleepTime;   // time left in this loop in ns
			
			if (sleepTime <= 0)   // update/render took longer than period
			{	
			
				try {
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
	{ if (!gameOver && !isPaused) {}
			// update game state ...
	}

	
	private Image dbImage = null;
	/**
	 * draw the current frame to an image buffer
	 */
	private void gameRender()
	{
		if (dbImage == null){  // create the buffer
			dbImage = createImage(PWIDTH, PHEIGHT);
			if (dbImage == null) {
				System.out.println("dbImage is null");
				return;
			}
			else
				gameGraphics = (Graphics2D) dbImage.getGraphics();
		}
		
		// clear the background
		gameGraphics.setColor(Color.white);
		gameGraphics.fillRect (0, 0, PWIDTH, PHEIGHT);
		
		// draw game elements
	
		if (gameOver)
			gameOverMessage(gameGraphics);
	}  // end of gameRender()
	
	
	private void gameOverMessage(Graphics g)
	// center the game-over message
	{
		//g.drawString(msg, x, y);
	}  // end of gameOverMessage()
	// more methods, explained later

}
