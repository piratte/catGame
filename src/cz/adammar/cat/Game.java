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
	public Game(int period) {
		
		gp = new GamePanel();
		addWindowListener(this);
		setContentPane(gp);
		pack();
		
		setTitle("Cat - the game");
		setResizable(false);
		setVisible(true);
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
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
		int period = (int) 1000000000 / fps;
		
		/**
		 * creating game
		 */
		new Game(period);
	}
	

}
