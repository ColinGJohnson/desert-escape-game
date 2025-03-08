package dev.cgj.games;

public class ComCar extends Entity {
	private EscapeGame game;
	public boolean used = false;
	public double rotation;
	public int type;

	public ComCar(EscapeGame g, String r, int newX, int newY, int carType) {
		super(r, newX, newY);

		type = carType;
		game = g;
	}

	@Override
	public void collidedWith(Entity other) {
		if (!used && other instanceof Car) {
			((Car) other).swap(this);
		}
	} // collidedWith

	public void move(long delta) {
		setY(getY() - game.gameDY - 4);

		// if car moves off bottom of screen, remove it
		if (y > 700) {
			game.removeEntity(this);
		} // if

	} // move

	public void setY(double newY) {
		y = (int) newY;
	}
}
