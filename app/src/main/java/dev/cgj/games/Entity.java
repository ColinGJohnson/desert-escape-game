package dev.cgj.games;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Entity {
	protected double x; // current x location
	protected double y; // current y location
	public Sprite sprite; // this entity's sprite
	public double dx; // horizontal speed (px/s) + -> right
	public double dy; // vertical speed (px/s) + -> down
	public String spriteRef;
	private int currentSprite = 0; // current sprite being drawn
	private final Rectangle me = new Rectangle(); // bounding rectangle of

	/**
     * Constructor input: reference to the image for this entity, initial x and
     * y location to be drawn at
	 *
     */
	public Entity(String r, int newX, int newY) {
		x = newX;
		y = newY;
		
		spriteRef = r;
		sprite = (SpriteStore.get()).getSprite(r);
	}

	/*
	 * move input: delta - the amount of time passed in ms output: none purpose:
	 * after a certain amount of time has passed, update the location
	 */
	public void move(long delta) {
		
		// update location of entity based on move speeds
		x += (delta * dx) / 1000;
		y += (delta * dy) / 1000;
	} // move
	
	public int getImageHeight() {
		return sprite.getHeight();
	}
	
	public int getImageWidth() {
		return sprite.getWidth();
	}

	// get and set velocities
	public void setHorizontalMovement(double newDX) {
		dx = newDX;
	} // setHorizontalMovement

	public void setVerticalMovement(double newDY) {
		dy = newDY;
	} // setVerticalMovement

	public double getHorizontalMovement() {
		return dx;
	} // getHorizontalMovement

	public double getVerticalMovement() {
		return dy;
	} // getVerticalMovement

	// get position
	public int getX() {
		return (int) x;
	} // getX

	public int getY() {
		return (int) y;
	} // getY

	/*
	 * Draw this entity to the graphics object provided at (x,y)
	 */
	public void draw(Graphics g) {  
		sprite.draw(g, (int) x, (int) y, this);
	}

	public void setSprite(String r) {
		spriteRef = r;
		sprite = (SpriteStore.get()).getSprite(r);
	}

	public int getCurrentSprite() {
		return currentSprite;
	}

	public void setCurrentSprite(int id) {
		currentSprite = id;
	}

	/**
     * collidesWith input: the other entity to check collision against output:
     * true if entities collide purpose: check if this entity collides with the
     * other.
     */
	public boolean collidesWith(Entity other) {
		Rectangle him = new Rectangle();
		
		// rectangular hit box for entities 
		me.setBounds((int) x, (int) y, sprite.getWidth(), sprite.getHeight());
		him.setBounds(other.getX(), other.getY(), other.sprite.getWidth(), other.sprite.getHeight());
		
		return me.intersects(him);
	}

	/*
	 * collidedWith 
	 * input: the entity with which this has collided 
	 * purpose: notification that this entity collided with another
	 * Note: abstract methods must be implemented by any class that 
	 * extends this class
	 */
	public abstract void collidedWith(Entity other);
}
