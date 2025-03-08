package dev.cgj.games;

// an obstacle is an entity that can damage the player
public class Obstacle extends Entity {
	private EscapeGame game;
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

		// if obstacle moves off bottom of screen, remove it from ObstacleEntity list
		if (y > 800) {
			game.removeEntity(this);
		} // if

	} // move

	public void setY(double newY) {
		
		//TODO: move method to entity
		y = (int)newY;
	}
	
	public boolean getPastCollision(){
		return pastCollision;
	}
}
