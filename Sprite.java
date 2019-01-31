/* Sprite.java
 * March 23, 2006
 * Store no state information, this allows the image to be stored only
 * once, but to be used in many different places.
 */


import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Sprite {

	public Image image; // the image to be drawn for this sprite

	// constructor
	public Sprite(Image i) {
		image = i;
	} // constructor

	// return width of image in pixels
	public int getWidth() {
		return image.getWidth(null);
	} // getWidth

	// return height of image in pixels
	public int getHeight() {
		return image.getHeight(null);
	} // getHeight

	// draw the sprite in the graphics object provided at location (x,y)
	public void draw(Graphics g, int x, int y, Entity source) {

		// if drawing the player's car, rotate sprite as necessary
		if (source instanceof Car || source instanceof TankTurret || source instanceof Projectile) {
			double locationX = 0;
			double locationY = 0;
			double rotationRadians = 0;
			
			
			// Rotation information
			if (source instanceof TankTurret) {
				locationX = image.getWidth(null)/2;
				locationY = image.getHeight(null)/2;
				rotationRadians = Math.toRadians(((TankTurret) source).getRotation());
			} else if (source instanceof Car){
				locationX = image.getWidth(null)/2;
				locationY = image.getHeight(null)/2;
				rotationRadians = Math.toRadians(((Car) source).getRotation());
			} else {
				locationX = image.getWidth(null)/2;
				locationY = image.getHeight(null)/2;
				rotationRadians = Math.toRadians(((Projectile) source).getShotAngle());
			}
			
			AffineTransform tx = AffineTransform.getRotateInstance(rotationRadians, locationX, locationY);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

			// Drawing the rotated image at the required drawing locations
			g.drawImage(op.filter((BufferedImage) image, null), x, y, null);
		} else {
			g.drawImage(image, x, y, null);
		}
	} // draw

} // Sprite