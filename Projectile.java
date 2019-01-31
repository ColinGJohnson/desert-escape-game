

public class Projectile extends Entity {
	private EscapeGame game;
	private boolean pastCollision = false; // the car cannot collide with an
											// obstacle twice.
	int moveSpeed = -3;
	double shotAngle = 0;
	int ROCKET_POWER = 250;

	public Projectile(EscapeGame game, String r, int newX, int newY, double shotAngle) {
		super(r, newX, newY);
		this.game = game;
		this.shotAngle = shotAngle;

		// set shot speeds (x and y)
		dx = moveSpeed * Math.cos(Math.toRadians(shotAngle + 90));
		dy = moveSpeed * Math.sin(Math.toRadians(shotAngle + 90));
	}

	public void move(long delta) {
		x += (dx);
		y += (dy);

		// remove entity once far enough off screen
		if (y < 0) {
			game.removeEntity(this);
		}
	} // move

	public void setY(double newY) {

		// TODO: move method to entity
		y = (int) newY;
	}

	public boolean getPastCollision() {
		return pastCollision;
	}

	public double getShotAngle() {
		return shotAngle;
	}

	@Override
	public void collidedWith(Entity other) {
		// tankShot collisions handled in car class.
	}
}
