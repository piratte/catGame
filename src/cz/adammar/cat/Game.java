package cz.adammar.cat;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;


/**
 * Main game class
 * 
 * Implements method main, serves as game window
 * 
 * @author madam
 *
 */
@SuppressWarnings("serial")
public class Game  extends JFrame implements WindowListener {
	
	/**
	 * default frames per second
	 */
	private static int DEFAULT_FPS = 85;
	
	/**
	 * Game panel
	 */
	private GamePanel gp;
	
	/**
	 * Constructor
	 * 
	 * Creating the game window and setting its properties
	 * @param period integer period of updates in milliseconds
	 */
	public Game(long period) {
		
		gp = new GamePanel(period);
		addWindowListener(this);
		setContentPane(gp);
		pack();
		
		setTitle("Cat - the game");
		setResizable(false);
		setVisible(true);
	}

	/**
	 * window activated, resume game
	 * 
	 * @param arg0 the WindowEvent 
	 */
	@Override
	public void windowActivated(WindowEvent arg0) {
		gp.resumeGame();		
	}

	/**
	 * window closed, nothing left to be done
	 * 
	 * @param arg0 the WindowEvent 
	 */
	@Override
	public void windowClosed(WindowEvent arg0) {}

	/**
	 * window being closed, stop game
	 * 
	 * @param arg0 the WindowEvent 
	 */
	@Override
	public void windowClosing(WindowEvent arg0) {
		gp.stopGame();
	}

	/**
	 * window deactivated, pause game
	 * 
	 * @param arg0 the WindowEvent 
	 */
	@Override
	public void windowDeactivated(WindowEvent arg0) {
		gp.pauseGame();
	}

	/**
	 * window activated, resume game
	 * 
	 * @param arg0 the WindowEvent 
	 */
	@Override
	public void windowDeiconified(WindowEvent arg0) {
		gp.resumeGame();
	}

	/**
	 * window minimalized, pause game
	 * 
	 * @param arg0 the WindowEvent 
	 */
	@Override
	public void windowIconified(WindowEvent arg0) {
		gp.pauseGame();		
	}

	/**
	 * window opened for the first time, do nothing
	 * 
	 * @param arg0 the WindowEvent 
	 */
	@Override
	public void windowOpened(WindowEvent arg0) {}
	
	public static void main(String args[]) {
		int fps;
		
		/**
		 * parsing cmd line parameters
		 */
		if (args.length != 0) {
			fps = Integer.parseInt(args[0]);
		} else {
			fps = DEFAULT_FPS;
		}
		
		long period = (long) 1000000000L / fps;
		System.err.println("Period: "  + period);
		/**
		 * creating game
		 */
		new Game(period);
	}
	

}
