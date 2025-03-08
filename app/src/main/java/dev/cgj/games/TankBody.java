package dev.cgj.games;

// Specialized Entity for the body of enemy tank.
public class TankBody extends Tank{

	// constructor
	public TankBody(EscapeGame g, String r, int newX, int newY) {
		super(g, r, newX, newY);
	} // TankBody
	
	//returns tank Y value to correct range
	public void update(){
		
		// move tank upwards if too low on screen
		if(y > 680){
			y--;
		} // if
		
		// make the tank's treads move
		updateTankAnimations();
	} // update
} // TankBody
