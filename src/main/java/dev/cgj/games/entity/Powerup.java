package dev.cgj.games.entity;

import dev.cgj.games.EscapeGame;
import dev.cgj.games.PowerupType;

public class Powerup extends Entity {
	private final EscapeGame game;
	private boolean pastCollision = false; // the car cannot collide with a powerup twice.
	private PowerupType type;

	public Powerup(EscapeGame g, String r, int newX, int newY, PowerupType powerupType) {
		super(r, newX, newY);
		type = powerupType;
		game = g;
	}

	@Override
	public void collidedWith(Entity other) {
		if(other instanceof Car){
			pastCollision = true;
		}
	}

	public void move(long delta) {
		setY(getY() - game.gameDY);

		// if power up moves off bottom of screen, remove it from PowerupEntity list
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

	public PowerupType getType() {
		return type;
	}
}
