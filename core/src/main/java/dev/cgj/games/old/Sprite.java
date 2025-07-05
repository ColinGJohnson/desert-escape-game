package dev.cgj.games.old;

import dev.cgj.games.old.entity.Car;
import dev.cgj.games.old.entity.Entity;
import dev.cgj.games.old.entity.Projectile;
import dev.cgj.games.old.entity.TankTurret;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Sprite {
	public Image image;

	public Sprite(Image image) {
		this.image = image;
	}

	/**
     * return width of image in pixels
     */
	public int getWidth() {
		return image.getWidth(null);
	}

	/**
     * @return Height of image in pixels
     */
	public int getHeight() {
		return image.getHeight(null);
	}

	/**
     * Draw the sprite in the graphics object provided at location (x,y).fire
	 *
     */
	public void draw(Graphics g, int x, int y, Entity source) {

		// if drawing the player's car, rotate sprite as necessary
		if (source instanceof Car || source instanceof TankTurret || source instanceof Projectile) {

            // Rotation information
            double locationX;
            double locationY;
            double rotationRadians;
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
	}
}
