package dev.cgj.games.old.entity;

import dev.cgj.games.old.EscapeGame;
import dev.cgj.games.old.ObstacleType;

/**
 * An obstacle is an entity that can damage the player.
 */
public class Obstacle extends Entity {
	private final EscapeGame game;
	private boolean pastCollision = false; // the car cannot collide with an obstacle twice.
	ObstacleType type;

	public Obstacle(EscapeGame g, int newX, int newY, ObstacleType type) {
		super(type.getSpritePath(), newX, newY);
		this.type = type;
		game = g;
	}

	@Override
	public void collidedWith(Entity other) {
		pastCollision = true;
	}

	public void move(long delta) {
		setY(getY() - game.gameDY);
		if (y > 800) {
			game.removeEntity(this);
		}
	}

	public void setY(double newY) {
		y = (int)newY;
	}

	public boolean getPastCollision(){
		return pastCollision;
	}
}
