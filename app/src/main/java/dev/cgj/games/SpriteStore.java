package dev.cgj.games;/* SpriteStore.java
 * Manages the sprites in the game.  
 * Caches them for future use.
 */


import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class SpriteStore {

	// one instance of this class will exist
	private static SpriteStore single = new SpriteStore();
	private HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();

	// returns the single instance of this class
	public static SpriteStore get() {
		return single;
	} // get

	/*
	 * getSprite input: a string specifying which sprite image is required
	 * output: a sprite instance containing an accelerated image of the
	 * requested image purpose: to return a specific sprite
	 */
	public Sprite getSprite(String ref) {

		// if the sprite is already in the HashMap
		// then return it
		if (sprites.get(ref) != null) {
			return (Sprite) sprites.get(ref);
		} // if

		// else, load the image into the HashMap off the
		// hard drive (and hence, into memory)

		

		Image sourceImage = null;

		try {
			System.out.println("loading image from: " + ref);
			sourceImage = ImageIO.read(SpriteStore.class.getResource(ref));
			System.out.println("loaded image from: " + ref);
			
		} catch (IOException e) {
			e.printStackTrace();
		} // get image
		
		System.out.println();
		
		

		/*
		 * // create an accelerated image to store our sprite in
		 * GraphicsConfiguration gc =
		 * GraphicsEnvironment.getLocalGraphicsEnvironment().
		 * getDefaultScreenDevice().getDefaultConfiguration(); Image image =
		 * gc.createCompatibleImage(sourceImage.getWidth(),
		 * sourceImage.getHeight(), Transparency.BITMASK);
		 * 
		 * // draw our source image into the accelerated image
		 * image.getGraphics().drawImage(sourceImage, 0, 0, null);
		 * 
		 * // create a sprite, add it to the cache and return it Sprite sprite =
		 * new Sprite(image);
		 */

		// create a sprite, add it to the cache and return it
		Sprite sprite = new Sprite(sourceImage);
		sprites.put(ref, sprite);

		return sprite;
	} // getSprite

} // SpriteStore