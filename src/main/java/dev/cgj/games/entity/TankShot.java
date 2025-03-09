package dev.cgj.games.entity;

import dev.cgj.games.EscapeGame;

public class TankShot extends Projectile {
	public static final double MOVE_SPEED = -2;

	public TankShot(EscapeGame g, String r, int newX, int newY, double shotAngle) {
		super(g, r, newX, newY, shotAngle);

        // set shot speeds (x and y) speed shot moves
		dx = MOVE_SPEED * Math.cos(Math.toRadians(shotAngle + 90));
		dy = MOVE_SPEED * Math.sin(Math.toRadians(shotAngle + 90));
	}
}