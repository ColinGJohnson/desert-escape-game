public class TankTurret extends Tank {
	private double rotation = 0;
	Car player;
	EscapeGame game;

	// constructor
	public TankTurret(EscapeGame g, String r, int newX, int newY, Car player) {
		super(g, r, newX, newY);
		this.player = player;
		game = g;
	} // TankTurret

	// aim turret at player
	public boolean aimTurret() {
		int aimTweak; // degrees to refine aim
		
		// set aimTweak according to the player's current car
		if ((player.currentCar >= 0 && player.currentCar <= 5) || player.currentCar == 9) {
			aimTweak = 320;
		} else {
			aimTweak = 390;
		} // else
		
		// calculate what rotation will make the turret point at the player's car
		double targetRotation = Math.toDegrees(Math.atan2(
				((player.getX() - player.getImageWidth() / 2) + aimTweak) - y,
				player.getY() - x));

		// rotate the turret towards the player
		if (targetRotation < rotation) {
			rotation -= 0.7;
		} else if (targetRotation > rotation) {
			rotation += 0.7;
		} // if

		// is the turret aiming at the player?
		if (targetRotation < rotation + 2 && targetRotation > rotation - 2) {
			targetRotation = rotation;
			return true;
		} // if

		// turret is not aiming at the player
		return false;
	} // targetRotation

	// get the current turret rotation in degrees
	public double getRotation() {
		return rotation;
	} // getRotation

	// set the turrets rotation in degrees
	public void setRotation(double d) {
		rotation = d;
	} // setRotation

	
	// if try to fire at the player
	public void fire() {

		// if aiming at the car, and car is slow enough
		if (aimTurret()) {
			
			// add a new projectile to arrayList (the tank shoots)
			game.projectileEntities.add(new TankShot(game, "/resources/shot.jpg",
					(int) (x + getImageWidth() / 2),
					(int) (y + getImageHeight() / 2), rotation));
		} // if
	} // fire
	
	// update tank turret y position and aim at player
	public void update(){
		
		// move tank upwards if too low on screen
		if(y > 659){
			y--;
		}
		
		// rotate tank turret to face the player's car
		aimTurret();
	} // update
} // TankTurret
