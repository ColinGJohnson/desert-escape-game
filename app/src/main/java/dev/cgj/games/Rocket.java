package dev.cgj.games;

import java.awt.Color;
import java.awt.Rectangle;

public class Rocket extends Projectile {
	EscapeGame game;

	public Rocket(EscapeGame game, String spritePath, int newX, int newY, double shotAngle) {
		super(game, spritePath, newX, newY, shotAngle);
		this.game = game;
	}

	@Override
	public void collidedWith(Entity other) {
		
		// player rockets can destroy only obstacles
		if (other instanceof Obstacle) {

			// create particle effect
			game.spawnParticleEffects((int) x, (int) y, (int) dx, (int) dy, Color.orange, 50, 100, "rocket", 10);

			Rectangle explosionBounds = new Rectangle((int) (x - ROCKET_POWER / 2), (int) (y - ROCKET_POWER / 2),
					ROCKET_POWER, ROCKET_POWER);

			// remove all obstacles in range
			for (int i = 0; i < game.obstacleEntities.size(); i++) {
				Rectangle obstacleBounds = new Rectangle(game.obstacleEntities.get(i).getX(),
						game.obstacleEntities.get(i).getY(), game.obstacleEntities.get(i).getImageWidth(),
						game.obstacleEntities.get(i).getImageHeight());

				if (explosionBounds.intersects(obstacleBounds)) {
					game.removeEntity(game.obstacleEntities.get(i));

					// you get points for chaos!
					game.obstaclesDestroyed++;
				}
			}

			// remove rocket and obstacle
			game.removeEntity(other);
			game.removeEntity(this);
		}
	}
}
