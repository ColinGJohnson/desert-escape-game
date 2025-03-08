package dev.cgj.games;/* ShotEntity.java
 * March 27, 2006
 * Represents player's ship
 */


public class TankShot extends Projectile {
	private double moveSpeed = -2; // speed shot moves
	
	public TankShot(EscapeGame g, String r, int newX, int newY, double shotAngle) {
		super(g, r, newX, newY, shotAngle);
		dy = moveSpeed;
		
		// set shot speeds (x and y)
		dx = moveSpeed * Math.cos(Math.toRadians(shotAngle + 90));
		dy = moveSpeed * Math.sin(Math.toRadians(shotAngle + 90));
	} // TankShot
} // ShipEntity class