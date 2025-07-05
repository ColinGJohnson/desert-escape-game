package dev.cgj.games.old.entity;

import dev.cgj.games.old.Sprite;
import dev.cgj.games.old.SpriteStore;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Entity {
    public double x; // current x location
    public double y; // current y location
    public Sprite sprite; // this entity's sprite
    public double dx; // horizontal speed (px/s) + -> right
    public double dy; // vertical speed (px/s) + -> down
    public String spritePath;
    private int currentSprite = 0; // current sprite being drawn
    private final Rectangle me = new Rectangle(); // bounding rectangle of

    /**
     * @param spritePath Sprite for this entity.
     * @param x          Initial horizontal position.
     * @param y          Initial vertical position.
     */
    public Entity(String spritePath, int x, int y) {
        this.x = x;
        this.y = y;

        this.spritePath = spritePath;
        sprite = (SpriteStore.getInstance()).getSprite(spritePath);
    }

    /**
     * Update location of entity based on move speeds.
     *
     * @param delta The amount of time passed in milliseconds.
     */
    public void move(long delta) {
        x += (delta * dx) / 1000;
        y += (delta * dy) / 1000;
    }

    public int getImageHeight() {
        return sprite.getHeight();
    }

    public int getImageWidth() {
        return sprite.getWidth();
    }

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

    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }

    /**
     * Draw this entity to the graphics object provided at (x,y)
     */
    public void draw(Graphics g) {
        sprite.draw(g, (int) x, (int) y, this);
    }

    public void setSprite(String spritePath) {
        this.spritePath = spritePath;
        sprite = (SpriteStore.getInstance()).getSprite(spritePath);
    }

    public int getCurrentSprite() {
        return currentSprite;
    }

    public void setCurrentSprite(int id) {
        currentSprite = id;
    }

    /**
     * Check if this entity collides with another.
     *
     * @return True if entity is in contact with the other.
     */
    public boolean collidesWith(Entity other) {
        Rectangle him = new Rectangle();

        // rectangular hit box for entities
        me.setBounds((int) x, (int) y, sprite.getWidth(), sprite.getHeight());
        him.setBounds(other.getX(), other.getY(), other.sprite.getWidth(), other.sprite.getHeight());

        return me.intersects(him);
    }

    /**
     * Called when this entity collides with another.
     *
     * @param other The entity with which this has collided.
     */
    public abstract void collidedWith(Entity other);
}
