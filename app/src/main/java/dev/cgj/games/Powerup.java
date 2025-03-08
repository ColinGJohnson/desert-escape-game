package dev.cgj.games;

public class Powerup extends Entity{
	private EscapeGame game;
	private boolean pastCollision = false; // the car cannot collide with a powerup twice.
	String type;

	public Powerup(EscapeGame g, String r, int newX, int newY, String powerupType) {
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
	} // move

	public void setY(double newY) {
		
		//TODO: move method to entity
		y = (int)newY;
	}
	
	public boolean getPastCollision(){
		return pastCollision;
	}
}
