package dev.cgj.games;

/**
 * An obstacle is an entity that can damage the player.
 */
public class Obstacle extends Entity {
	private final EscapeGame game;
	private boolean pastCollision = false; // the car cannot collide with an obstacle twice.
	String type;

	public Obstacle(EscapeGame g, String r, int newX, int newY, String obstacleType) {
		super(r, newX, newY);
		type = obstacleType;
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
