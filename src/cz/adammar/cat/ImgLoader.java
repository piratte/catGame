package cz.adammar.cat;

import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

/**
 * Class used for loading images from disk
 * @author madam
 *
 */
public class ImgLoader {
	
	/**
	 * Image directory
	 */
	private String imgDir;
	
	/**
	 * Default image directory
	 */
	private static final String DEF_IMG_DIR = "img/";
	
	/**
	 * Error string
	 */
	private static final String LOAD_ERROR = "Cannot read image from file: ";
	
	/**
	 * Constructor
	 * @param imageDir Image directory
	 */
	public ImgLoader(String imageDir){
		
		if (imageDir.charAt(imgDir.length()) != '/') {
			imageDir += "/";
		}
		imgDir = imageDir;
	}
	
	/**
	 * Constructor using the default image directory
	 */
	public ImgLoader() {
		this(DEF_IMG_DIR);
	}
	
	/**
	 * Load image from file 
	 * @param fileName name of file in the image directory to be loaded
	 * @return BufferedImage loaded from file
	 */
	public BufferedImage loadImg(String fileName) {
		BufferedImage out = null;
		try {
			out = ImageIO.read(new File(imgDir + fileName));
		} catch (IOException e) {
			System.err.println(LOAD_ERROR + imgDir + fileName);
		}
		return out;
	}

}
