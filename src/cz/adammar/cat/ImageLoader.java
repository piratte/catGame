/**
 * Class for loading all the images
 * 
 * This class was greatly inspired by ImageLoader class by 
 * Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
 * 
 * Modifications and major simplifications were made, because the original class was too complex
 * for my needs
 * 
 * ImagesFile Formats:
 * o <fnm>                     = a single image file
 * g <name> <fnm> [ <fnm> ]*   = a group of files with different names;
 *
 * and blank lines and comment lines.
 * 
 *  The images in group files can be accessed by the 'g' <name> and the
 *  <fnm> prefix of the particular file, or its position in the group.
 */

package cz.adammar.cat;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;

public class ImageLoader {

	/**
	 * image directory
	 */
	private final static String IMAGE_DIR = "img/";
	
	/**
	 * key: filename prefix
	 * value: ArrayList of BufferedImages
	 */
	private HashMap<String, ArrayList<BufferedImage>> imagesMap;

	/**
	 * key: group name
	 * value: list of filenames associated with key group
	 */
	private HashMap<String, ArrayList<String>> gNamesMap;
	/* The key is the 'g' <name> string, the object is an
	 ArrayList of filename prefixes for the group. This is used to 
	 access a group image by its 'g' name and filename. */
	private GraphicsConfiguration gc;

	/**
	 * Constructor
	 * @param fnm filename of image specifications file
	 */
	public ImageLoader(String fnm) // begin by loading the images specified in fnm
	{
		initLoader();
		loadImagesFile(fnm);
	} 

	public ImageLoader() {
		initLoader();
	}

	/**
	 * Initialization method
	 */
	private void initLoader() {
		imagesMap = new HashMap<String, ArrayList<BufferedImage>>();
		gNamesMap = new HashMap<String, ArrayList<String>>();

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
	}  // end of initLoader()

	/**
	 * Load images from the image list files
	 * @param fnm filename of image specifications file
	 */
	private void loadImagesFile(String fnm) 
	{
		String imsFNm = IMAGE_DIR + fnm;
		System.out.println("Reading file: " + imsFNm);
		try {
			InputStream in = this.getClass().getResourceAsStream(imsFNm);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			// BufferedReader br = new BufferedReader( new FileReader(imsFNm));
			String line;
			char ch;
			while ((line = br.readLine()) != null) {
				if (line.length() == 0) {
					continue;
				}
				if (line.startsWith("//")) {
					continue;
				}
				ch = Character.toLowerCase(line.charAt(0));
				if (ch == 'o') {
					getFileNameImage(line);
				} else if (ch == 'g') {
					getGroupImages(line);
				} else {
					System.out.println("Do not recognize line: " + line);
				}
			}
			br.close();
		} catch (IOException e) {
			System.out.println("Error reading file: " + imsFNm);
			System.exit(1);
		}
	}  // end of loadImagesFile()

	/**
	 * Get name of a single image file
	 * @param line from the configuration file
	 */
	private void getFileNameImage(String line) 
	/* format:
	 o <fileName>
	 */ {
		StringTokenizer tokens = new StringTokenizer(line);

		if (tokens.countTokens() != 2) {
			System.out.println("Wrong no. of arguments for " + line);
		} else {
			tokens.nextToken();    // skip command label
			System.out.print("o Line: ");
			loadSingleImage(tokens.nextToken());
		}
	}  // end of getFileNameImage()

	/**
	 * Load single image
	 * @param fnm filename
	 * @return boolean true if image can be loaded
	 */
	public boolean loadSingleImage(String fnm) // can be called directly
	{
		String name = getPrefix(fnm);

		if (imagesMap.containsKey(name)) {
			System.out.println("Error: " + name + "already used");
			return false;
		}

		BufferedImage bi = loadImage(fnm);
		if (bi != null) {
			ArrayList<BufferedImage> imsList = new ArrayList<BufferedImage>();
			imsList.add(bi);
			imagesMap.put(name, imsList);
			System.out.println("  Stored " + name + "/" + fnm);
			return true;
		} else {
			return false;
		}
	}  // end of loadSingleImage()

	/**
	 * Get the image prefix (the part of the name before the extension)
	 * @param fnm filename
	 * @return string image name without extension postfix
	 */
	private String getPrefix(String fnm) 
	{
		int posn;
		if ((posn = fnm.lastIndexOf(".")) == -1) {
			System.out.println("No prefix found for filename: " + fnm);
			return fnm;
		} else {
			return fnm.substring(0, posn);
		}
	} // end of getPrefix()


	/**
	 * Get the filenames when there is a grouped image on the input
	 * Format:
	 *  g <name> <fileName>  [ <fileName> ]*
	 *
	 * @param line from the configuration file
	 */
	private void getGroupImages(String line) 
	{
		StringTokenizer tokens = new StringTokenizer(line);

		if (tokens.countTokens() < 3) {
			System.out.println("Wrong no. of arguments for " + line);
		} else {
			tokens.nextToken();    // skip command label
			System.out.print("g Line: ");

			String name = tokens.nextToken();

			ArrayList<String> fnms = new ArrayList<String>();
			fnms.add(tokens.nextToken());  // read filenames
			while (tokens.hasMoreTokens()) {
				fnms.add(tokens.nextToken());
			}

			loadGroupImages(name, fnms);
		}
	}  // end of getGroupImages()

	/**
	 * Load all the images of the group
	 * 
	 * @param name group name
	 * @param fnms filenames
	 * @return number of loaded images
	 */
	public int loadGroupImages(String name, ArrayList<String> fnms) 
	/* Can be called directly to load a group of images, whose
	 filenames are stored in the ArrayList <fnms>. They will
	 be stored under the 'g' name <name>.
	 */ {
		if (imagesMap.containsKey(name)) {
			System.out.println("Error: " + name + "already used");
			return 0;
		}

		if (fnms.isEmpty()) {
			System.out.println("List of filenames is empty");
			return 0;
		}

		BufferedImage bi;
		ArrayList<String> nms = new ArrayList<String>();
		ArrayList<BufferedImage> imsList = new ArrayList<BufferedImage>();
		String nm, fnm;
		int loadCount = 0;

		System.out.println("  Adding to " + name + "...");
		System.out.print("  ");
		for (int i = 0; i < fnms.size(); i++) {    // load the files
			fnm = (String) fnms.get(i);
			nm = getPrefix(fnm);
			if ((bi = loadImage(fnm)) != null) {
				loadCount++;
				imsList.add(bi);
				nms.add(nm);
				System.out.print(nm + "/" + fnm + " ");
			}
		}
		System.out.println();

		if (loadCount == 0) {
			System.out.println("No images loaded for " + name);
		} else {
			imagesMap.put(name, imsList);
			gNamesMap.put(name, nms);
		}

		return loadCount;
	}  // end of loadGroupImages()

	public int loadGroupImages(String name, String[] fnms) // supply the group filenames in an array
	{
		ArrayList<String> al = new ArrayList<String>(Arrays.asList(fnms));
		return loadGroupImages(name, al);
	}

// ======================= access methods =======================
	/**
	 * Get selected image. If more images of same name are stored, return the first one
	 * @param name image name
	 * @return the image associated with <name>
	 */
	public BufferedImage getImage(String name) 
	{
		ArrayList<?> imsList = (ArrayList<?>) imagesMap.get(name);
		if (imsList == null) {
			System.out.println("No image(s) stored under " + name);
			return null;
		}

		// System.out.println("Returning image stored under " + name);  
		return (BufferedImage) imsList.get(0);
	}  // end of getImage() with name input;

	/**
	 * 
	 * @param name filename
	 * @param posn possition in the list
	 * @return image
	 */
	public BufferedImage getImage(String name, int posn) /* Get the image associated with <name> at position <posn>
	 in its list. If <posn> is < 0 then return the first image
	 in the list. If posn is bigger than the list's size, then
	 calculate its value modulo the size.
	 */ {
		ArrayList<?> imsList = (ArrayList<?>) imagesMap.get(name);
		if (imsList == null) {
			System.out.println("No image(s) stored under " + name);
			return null;
		}

		int size = imsList.size();
		if (posn < 0) {
			// System.out.println("No " + name + " image at position " + posn +
			//                      "; return position 0"); 
			return (BufferedImage) imsList.get(0);   // return first image
		} else if (posn >= size) {
			// System.out.println("No " + name + " image at position " + posn); 
			int newPosn = posn % size;   // modulo
			// System.out.println("Return image at position " + newPosn); 
			return (BufferedImage) imsList.get(newPosn);
		}

		// System.out.println("Returning " + name + " image at position " + posn);  
		return (BufferedImage) imsList.get(posn);
	}  // end of getImage() with posn input;

	/**
	 * Get the image from group
	 * @param name group name
	 * @param fnmPrefix image file prefix
	 * @return image
	 */
	public BufferedImage getImage(String name, String fnmPrefix)
	{
		ArrayList<?> imsList = (ArrayList<?>) imagesMap.get(name);
		if (imsList == null) {
			System.out.println("No image(s) stored under " + name);
			return null;
		}

		int posn = getGroupPosition(name, fnmPrefix);
		if (posn < 0) {
			// System.out.println("Returning image at position 0"); 
			return (BufferedImage) imsList.get(0);   // return first image
		}

		// System.out.println("Returning " + name + 
		//                        " image with pair name " + fnmPrefix);  
		return (BufferedImage) imsList.get(posn);
	}  // end of getImage() with fnmPrefix input;

	/**
	 * Search groups hashmap for image prefix 
	 * @param name group name
	 * @param fnmPrefix filename prefix
	 * @return possition in the list, -1 if not found
	 */
	private int getGroupPosition(String name, String fnmPrefix) 
	{
		ArrayList<?> groupNames = (ArrayList<?>) gNamesMap.get(name);
		if (groupNames == null) {
			System.out.println("No group names for " + name);
			return -1;
		}

		String nm;
		for (int i = 0; i < groupNames.size(); i++) {
			nm = (String) groupNames.get(i);
			if (nm.equals(fnmPrefix)) {
				return i;
			}          // the posn of <fnmPrefix> in the list of names
		}

		System.out.println("No " + fnmPrefix
				+ " group name found for " + name);
		return -1;
	}  // end of getGroupPosition()

	/**
	 * Return all buffered images for given name
	 * @param name image name
	 * @return list of images
	 */
	public ArrayList<?> getImages(String name)
	{
		ArrayList<?> imsList = (ArrayList<?>) imagesMap.get(name);
		if (imsList == null) {
			System.out.println("No image(s) stored under " + name);
			return null;
		}

		System.out.println("Returning all images stored under " + name);
		return imsList;
	}  // end of getImages();

	/**
	 * Check if name is in images hashmap
	 * @param name wanted name
	 * @return true if name present, false otherwise 
	 */
	public boolean isLoaded(String name)
	{
		ArrayList<?> imsList = (ArrayList<?>) imagesMap.get(name);
		if (imsList == null) {
			return false;
		}
		return true;
	}

	/**
	 * Get number of images stored under some name
	 * @param name searched name
	 * @return number of images stored the name
	 */
	public int numImages(String name)
	{
		ArrayList<?> imsList = (ArrayList<?>) imagesMap.get(name);
		if (imsList == null) {
			System.out.println("No image(s) stored under " + name);
			return 0;
		}
		return imsList.size();
	} // end of numImages()

	// ------------------- Image Input ------------------

	/**
	 * Load the specified image
	 * @param fnm filename
	 * @return buffed image
	 */
	public BufferedImage loadImage(String fnm) 
	{
		try {
			BufferedImage im = ImageIO.read(
					getClass().getResource(IMAGE_DIR + fnm));
			// An image returned from ImageIO in J2SE <= 1.4.2 is 
			// _not_ a managed image, but is after copying!

			int transparency = im.getColorModel().getTransparency();
			BufferedImage copy = gc.createCompatibleImage(
					im.getWidth(), im.getHeight(),
					transparency);
			// create a graphics context
			Graphics2D g2d = copy.createGraphics();
			
			// copy image
			g2d.drawImage(im, 0, 0, null);
			g2d.dispose();
			return copy;
		} catch (IOException e) {
			System.out.println("Load Image error for "
					+ IMAGE_DIR + "/" + fnm + ":\n" + e);
			return null;
		}
	} // end of loadImage() using ImageIO
}
