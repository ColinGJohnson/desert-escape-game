package game;

import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		System.out.println("starting game...");
		
		// create a frame to contain game
		JFrame container = new JFrame("Desert escape");
		
		// make a new game.
		@SuppressWarnings("unused")
		EscapeGame game = new EscapeGame(container);
	}
}
