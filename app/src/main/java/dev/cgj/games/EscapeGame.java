package dev.cgj.games;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("serial")
public class EscapeGame extends Canvas {

	// Images to be drawn
	Image zero = null;
	Image one = null;
	Image two = null;
	Image three = null;
	Image four = null;
	Image five = null;
	Image six = null;
	Image seven = null;
	Image eight = null;
	Image nine = null;
	Image road = null;
	Image ground = null;
	Image startGround = null;
	Image startGround2 = null;
	Image startGame = null;
	Image HUDoverlay = null;
	Image arrow = null;

	private BufferStrategy strategy; // take advantage of accelerated graphics

	private int pageHeight = 800; // height of game window
	private int pageWidth = 800; // width of game window

	private boolean showDebug = false; // debug view active?
	private boolean gameOver = false; // player has lost?
	private boolean gameWin = false; // player has won?
	private boolean repeatGame = false; // player wants to play another game?
	public boolean gameRunning = true; // game should be running?
	public boolean showMenu = true; // menu is being shown?

	int distanceTravelled; // distance traveled in pixels
	public int distanceTravelledMeters; // distance traveled in meters
	int distancegoal = 1000; // how far the player needs to drive to win

	private boolean leftPressed = false; // left arrow key currently pressed
	private boolean rightPressed = false; // right arrow key currently pressed
	private boolean upPressed = false; // up arrow key currently pressed
	private boolean ePressed = false; // e arrow key currently pressed
	private boolean rPressed = false; // r key currently pressed
	private boolean enterPressed = false; // any key pressed
	private boolean anyPressed = false; // any key pressed

	public int totalHealthLost = 0; // used to calculate score
	public int obstaclesDestroyed = 0; // used to calculate score
	private int score = 0; // score this game
	private int bestscore = 0; // best score (read from
								// "DesertEscapeScores.txt")
	private int spawnChance = 5; // chance to spawn each obstacle type on a new
									// tile (1 in spawnChance)

	private double carResistance = 8; // dx/dy reduction
	Car player; // entity for the player's car
	TankBody body; // entity for the tank's main body
	TankTurret turret; // entity for the tank turret (can be rotated)

	int Bgx = 0; // x position to draw the background at
	int Bgy = 0; // y position to draw the background at
	public boolean firstTile = true; // is the player on the unique first tile?

	long delta = 0;
	long lastSpawnLoop = 0;
	long lastFuelLoop = 0;
	long gameStartTime = 0;
	long lastFireLoop = 0;
	long lastImageBlinkLoop = 0;

	// obstacle entities
	public ArrayList<Obstacle> obstacleEntities = new ArrayList<Obstacle>();

	// computer controlled car entities
	public ArrayList<ComCar> comCarEntities = new ArrayList<ComCar>();

	// projectile entities
	public ArrayList<Projectile> projectileEntities = new ArrayList<Projectile>();

	// power up entities
	public ArrayList<Powerup> powerupEntities = new ArrayList<Powerup>();

	// entities to remove
	private ArrayList<Entity> removeEntities = new ArrayList<Entity>();

	// particle entities
	private ArrayList<Particle> particles = new ArrayList<Particle>();
	public ArrayList<ParticleEffect> particleEffects = new ArrayList<ParticleEffect>();
	private ArrayList<Particle> removeParticleEffects = new ArrayList<Particle>();
	private ArrayList<Particle> removeParticles = new ArrayList<Particle>();

	public double gameDY = 0; // movement for all entities this loop

	/*
	 * Construct our game and set it running.
	 */
	public EscapeGame(JFrame container) {

		// record time of game start
		gameStartTime = System.currentTimeMillis();

		// get hold the content of the frame
		JPanel gamePanel = (JPanel) container.getContentPane();

		// set up the resolution of the game
		gamePanel.setPreferredSize(new Dimension(800, 800));
		gamePanel.setLayout(null);

		// set up canvas size (this) and add to frame
		setBounds(0, 0, 800, 800);
		gamePanel.add(this);

		// Tell AWT not to bother repainting canvas since that will
		// be done using graphics acceleration
		setIgnoreRepaint(true);

		// center window on screen
		container.pack();
		container.setLocationRelativeTo(null);

		// make the window visible
		container.setResizable(false);
		container.setVisible(true);

		// if user closes window, shutdown game and jre
		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			} // windowClosing
		});

		// add key listener to this canvas
		addKeyListener(new KeyInputHandler());

		// request focus so key events are handled by this canvas
		requestFocus();

		// create buffer strategy to take advantage of accelerated graphics
		createBufferStrategy(2);
		strategy = getBufferStrategy();

		do {

			// Initialize variables and entities
			init();

			// load images
			loadImages();

			// show the menu
			menuLoop();

			// start the game
			gameLoop();

			// get graphics context and turn on antialiasing
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);

			// show gameOver/gameWin screens
			drawEndScreen(g);

			// clear graphics and flip buffer
			g.dispose();
			strategy.show();

			// wait until user presses 'esc' to exit
			// or presses enter for a new game
			repeatGame = false;
			enterPressed = false;
			while (!repeatGame) {
				if (enterPressed) {
					repeatGame = true;
				} // if

				// prevent to loop from running too fast with a small delay
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} // catch
			} // while
		} while (repeatGame);
	} // constructor

	public void drawEndScreen(Graphics2D g) {
		if (gameOver) {
			g.setColor(new Color(204, 0, 0));
			g.fillRect(100, 350, 600, 125);
			g.setColor(Color.white);
			g.setFont(new Font("Arial", Font.PLAIN, 40));
			g.drawString("Game Over!", 290, 400);
			g.setFont(new Font("Arial", Font.PLAIN, 14));
			g.drawString("Distance Travelled: " + distanceTravelledMeters + "m", 320, 425);
		} else if (gameWin) {
			g.setColor(new Color(0, 204, 0));
			g.fillRect(100, 350, 600, 125);
			g.setColor(Color.white);
			g.setFont(new Font("Arial", Font.PLAIN, 40));
			g.drawString("You Win!", 315, 400);
			g.setFont(new Font("Arial", Font.PLAIN, 14));
			g.drawString("You scored: " + score + " points", 320, 425);
		} // else if

		if (score > bestscore) {
			g.drawString("New High Score!", 345, 440);
		} // if
		g.drawString("Exit: 'Esc' | New Game: 'Enter'", 300, 455);

		// write high score to file
		if (score > bestscore) {
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter("DesertEscapeScores.txt"));
				out.write(String.valueOf(score));
				out.close();
			} catch (Exception e) {
				System.out.println("Score Output Error");
			} // catch
		} // if
	} // drawEndScreen

	public void menuLoop() {
		while (showMenu) {

			// get graphics context for the accelerated surface and make it red
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			g.setColor(Color.red);

			// draw the menu
			drawMenu(g);

			// clear graphics and flip buffer
			g.dispose();
			strategy.show();

			// end the menu on enter press
			if (anyPressed) {
				showMenu = false;
			}
		} // while
	} // menuLoop

	private void drawMenu(Graphics2D g) {

		// menu background
		g.drawImage(startGround, 0, 0, this);

		// draw "start game" image for one second, then hide for another second
		if (System.currentTimeMillis() > lastImageBlinkLoop + 1000) {
			g.drawImage(startGame, 232, 149, this);
			if (System.currentTimeMillis() > lastImageBlinkLoop + 2000) {
				lastImageBlinkLoop = System.currentTimeMillis();
			} // if
		} // if
	} // DrawMenu

	// sets image variables to their respective images
	private void loadImages() {

		try {
			zero = ImageIO.read(EscapeGame.class.getResource("/resources/0.png"));
			one = ImageIO.read(EscapeGame.class.getResource("/resources/1.png"));
			two = ImageIO.read(EscapeGame.class.getResource("/resources/2.png"));
			three = ImageIO.read(EscapeGame.class.getResource("/resources/3.png"));
			four = ImageIO.read(EscapeGame.class.getResource("/resources/4.png"));
			five = ImageIO.read(EscapeGame.class.getResource("/resources/5.png"));
			six = ImageIO.read(EscapeGame.class.getResource("/resources/6.png"));
			seven = ImageIO.read(EscapeGame.class.getResource("/resources/7.png"));
			eight = ImageIO.read(EscapeGame.class.getResource("/resources/8.png"));
			nine = ImageIO.read(EscapeGame.class.getResource("/resources/9.png"));
			road = ImageIO.read(EscapeGame.class.getResource("/resources/road.jpg"));
			startGame = ImageIO.read(EscapeGame.class.getResource("/resources/pressAnyKeyToStart.png"));
			HUDoverlay = ImageIO.read(EscapeGame.class.getResource("/resources/HUD.png"));
			arrow = ImageIO.read(EscapeGame.class.getResource("/resources/arrow.png"));
			ground = ImageIO.read(EscapeGame.class.getResource("/resources/ground.jpg"));
			startGround2 = ImageIO.read(EscapeGame.class.getResource("/resources/startGround2.jpg"));
			startGround = ImageIO.read(EscapeGame.class.getResource("/resources/startGround1.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		} // catch
	} // loadImages

	// set all variables to their correct values at the start of the game
	@SuppressWarnings("unused")
	private void init() {

		// reset all variables to default values
		showDebug = false;
		gameOver = false;
		gameWin = false;
		repeatGame = false;
		distanceTravelled = 0;
		distanceTravelledMeters = 0;
		gameRunning = true;
		showMenu = true;
		leftPressed = false;
		rightPressed = false;
		upPressed = false;
		ePressed = false;
		rPressed = false;
		anyPressed = false;
		enterPressed = false;
		totalHealthLost = 0;
		obstaclesDestroyed = 0;
		score = 0;
		spawnChance = 5;
		Bgx = 0;
		Bgy = 0;
		firstTile = true;
		delta = 0;
		lastSpawnLoop = 0;
		lastFuelLoop = 0;
		gameStartTime = 0;
		lastFireLoop = 0;
		lastImageBlinkLoop = 0;

		// clear existing projectiles and obstacles
		obstacleEntities.clear();
		projectileEntities.clear();

		// Initialize player car and enemy tank objects
		player = new Car(this, "/resources/sc1.png", 400, 500);
		body = new TankBody(this, "/resources/body1.png", (pageWidth / 2) - 38, pageHeight);
		turret = new TankTurret(this, "/resources/turret.png", (pageWidth / 2) - 84, pageHeight - 21, player);

		// get HighScore from text file
		try {
			BufferedReader in = new BufferedReader(new FileReader("DesertEscapeScores.txt"));
			in.mark(Short.MAX_VALUE);
			bestscore = Integer.parseInt(in.readLine());
			in.close();
		} catch (Exception e) {
			System.out.println("Scores File Not Found\n");
			bestscore = 0;
		} // catch

		// preload sprites to avoid lag during game start
		Obstacle preload1 = new Obstacle(this, "/resources/skull.png", -100, -100, "preload");
		Obstacle preload2 = new Obstacle(this, "/resources/cactus.png", -100, -100, "preload");
		Obstacle preload3 = new Obstacle(this, "/resources/cone.png", -100, -100, "preload");
		Obstacle preload5 = new Obstacle(this, "/resources/shot.jpg", -100, -100, "preload");
		Obstacle preload6 = new Obstacle(this, "/resources/health.png", -100, -100, "preload");
		Obstacle preload7 = new Obstacle(this, "/resources/rocket.png", -100, -100, "preload");
		Obstacle preload8 = new Obstacle(this, "/resources/nitro.png", -100, -100, "preload");
		Obstacle preload9 = new Obstacle(this, "/resources/body2.png", -100, -100, "preload");
		Obstacle preload10 = new Obstacle(this, "/resources/body3.png", -100, -100, "preload");
	} // init

	public void gameLoop() {

		// initialize lastLoopTime
		long lastLoopTime = System.currentTimeMillis();

		// keep loop running until game ends
		while (gameRunning) {

			// calculate time since last update
			delta = System.currentTimeMillis() - lastLoopTime;
			lastLoopTime = System.currentTimeMillis();

			// get graphics context for the accelerated surface and make it red
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			g.setColor(Color.red);
			g.fillRect(0, 0, 800, 800);

			// remove dead entities
			obstacleEntities.removeAll(removeEntities);
			projectileEntities.removeAll(removeEntities);
			powerupEntities.removeAll(removeEntities);
			comCarEntities.removeAll(removeEntities);
			removeEntities.clear();

			// update car logic
			player.updateStats();

			// update stuff according to user input
			updateControls();

			// update movement
			moveEntities();

			// update tank turret and body
			updateTank();

			// brute all for collisions between all entities
			checkCollisions();

			// spawn exhaust particles
			updateParticles();

			// consume fuel every 1/2 second
			if (System.currentTimeMillis() - lastFuelLoop > 500) {
				player.useFuel();
				lastFuelLoop = System.currentTimeMillis();
			} // if

			// track progress
			distanceTravelled += gameDY;
			distanceTravelledMeters = Math.abs(distanceTravelled) / 20;

			// check if the player has won
			checkWin();

			// update score
			score = distanceTravelledMeters / 10;
			score += obstaclesDestroyed * 2;

			if ((score -= totalHealthLost / 10) > 0) {
				score -= totalHealthLost / 10;
			} // if

			if (score < 0) {
				score = 0;
			} // if

			// draw frame
			drawFrame(g);

			// show debug if requested
			if (showDebug) {
				drawTestInfo(g);
			} // if

			// clear graphics and flip buffer
			g.dispose();
			strategy.show();

			// exit the game loop if the player has lost or won
			if (gameOver || gameWin) {
				return;
			} // if

			// pause
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				e.printStackTrace();
				;
			} // catch
		} // while
	} // gameLoop

	public void spawnParticleEffects(int x, int y, int dx, int dy, Color effectColor, int life, int numParticles,
			String type, int size) {
		particleEffects.add(new ParticleEffect(x, y, dx, dy, effectColor, life, numParticles, type, size));
	} // spawnParticleEffects

	private void updateParticles() {

		// create exhaust particles
		if (gameDY < 0) {
			// randomize particle parameters
			double randomDX = (-6 + (Math.random() * ((6 + 6) + 1)));
			double randomDY = (6 + (Math.random() * ((6 - 6) + 1)));
			int randomLifespan = (int) (10 + (Math.random() * ((20 - 10) + 1)));
			int randColor = (int) (20 + (Math.random() * ((100 - 20) + 1)));

			// create new particle
			particles.add(
					new Particle(player.getX() + player.getImageWidth() / 2, player.getY() + player.getImageHeight(),
							randomDX, randomDY, 4, randomLifespan, new Color(randColor, randColor, randColor), 0.1));
		} // if

		// remove spent particles and effects
		particles.removeAll(removeParticles);
		particles.removeAll(removeParticleEffects);
		removeParticles.clear();
		removeParticleEffects.clear();
	} // updateParticles

	private void updateTank() {
		body.update();
		turret.update();

		// if enough time has passed and turret is aimed, fire
		if (System.currentTimeMillis() - lastFireLoop > 1000) {
			turret.fire();
			lastFireLoop = System.currentTimeMillis();
		} // if
	} // updateTank

	private void moveEntities() {
		// update entity movement relative to player
		if (player.speed > 0) {
			gameDY = (delta * player.getVerticalMovement()) / 1000;
		} else {
			gameDY = 0;
		} // else

		// move projectiles
		for (int i = 0; i < projectileEntities.size(); i++) {
			projectileEntities.get(i).move(i);
		} // for

		// move obstacles
		for (int i = 0; i < obstacleEntities.size(); i++) {
			obstacleEntities.get(i).move(i);
		} // for

		// move comCars
		for (int i = 0; i < comCarEntities.size(); i++) {
			comCarEntities.get(i).move(i);
		} // for

		// move power ups
		for (int i = 0; i < powerupEntities.size(); i++) {
			powerupEntities.get(i).move(i);
		} // for

		// move player car
		player.move(delta);
	} // moveEntites

	private void checkWin() {
		if (Math.abs(distanceTravelled / 20) > distancegoal) {
			System.out.println("you win");
			gameWin = true;
		} // if
	} // checkWin

	private void checkCollisions() {

		// check collisions with obstacles
		for (int i = 0; i < obstacleEntities.size(); i++) {
			Obstacle me = obstacleEntities.get(i);
			if (me.collidesWith(player) && !me.getPastCollision()) {

				// let the car and obstacle objects know
				// that a collision has occurred
				me.collidedWith(player);
				player.collidedWith(me);
			} // if
		} // for

		// check collisions with shells
		for (int i = 0; i < projectileEntities.size(); i++) {
			Projectile me = projectileEntities.get(i);
			if (me.collidesWith(player) && !me.getPastCollision()) {

				// let the car and obstacle objects know
				// that a collision has occurred
				me.collidedWith(player);
				player.collidedWith(me);
			} // if
		} // for

		// check collisions with power ups
		for (int i = 0; i < powerupEntities.size(); i++) {
			Powerup me = powerupEntities.get(i);
			if (me.collidesWith(player) && !me.getPastCollision()) {

				// let the car and power up objects know
				// that a collision has occurred
				me.collidedWith(player);
				player.collidedWith(me);
			} // if
		} // for

		// check collisions with comCars and notify entities if true
		for (int i = 0; i < comCarEntities.size(); i++) {
			ComCar me = comCarEntities.get(i);
			if (me.collidesWith(player)) {
				me.collidedWith(player);
				player.collidedWith(me);
			} // if
		} // for

		// check projectile collisions with obstacles
		for (int i = 0; i < projectileEntities.size(); i++) {
			Projectile me = projectileEntities.get(i);
			for (int j = 0; j < obstacleEntities.size(); j++) {
				Obstacle other = obstacleEntities.get(j);
				if (me.collidesWith(other) && !me.getPastCollision()) {

					// let the projectiles and obstacle entities know about
					// collision
					me.collidedWith(other);
					other.collidedWith(me);
				} // if
			} // for
		} // for
	} // checkCollisions

	private void spawn(int minX, int maxX, int minY, int maxY, int numToSpawn, String toSpawn, String type) {
		for (int i = 0; i < numToSpawn; i++) {
			if (type.equals("Powerup")) {
				Powerup spawn = null;

				switch (toSpawn) {
				case "health":
					spawn = new Powerup(this, "/resources/health.png",
							ThreadLocalRandom.current().nextInt(minX, maxX + 1),
							ThreadLocalRandom.current().nextInt(minY, maxY + 1), "health");
					break;

				case "fuel":
					spawn = new Powerup(this, "/resources/fuel.png",
							ThreadLocalRandom.current().nextInt(minX, maxX + 1),
							ThreadLocalRandom.current().nextInt(minY, maxY + 1), "fuel");
					break;

				case "nitro":
					spawn = new Powerup(this, "/resources/nitro.png",
							ThreadLocalRandom.current().nextInt(minX, maxX + 1),
							ThreadLocalRandom.current().nextInt(minY, maxY + 1), "nitro");
					break;

				case "shield":
					spawn = new Powerup(this, "/resources/shield.png",
							ThreadLocalRandom.current().nextInt(minX, maxX + 1),
							ThreadLocalRandom.current().nextInt(minY, maxY + 1), "shield");
					break;

				case "rocket":
					spawn = new Powerup(this, "/resources/rocket.png",
							ThreadLocalRandom.current().nextInt(minX, maxX + 1),
							ThreadLocalRandom.current().nextInt(minY, maxY + 1), "rocket");
					break;
				default:
					System.err.println("tried to spawn unknown object");
					break;
				} // switch

				// add the new Power up to arrayList
				if (spawn != null) {
					powerupEntities.add(spawn);
				}
			} else if (type.equals("Obstacle")) {
				Obstacle spawn = null;

				switch (toSpawn) {
				case "cactus":
					spawn = new Obstacle(this, "/resources/cactus.png",
							ThreadLocalRandom.current().nextInt(minX, maxX + 1),
							ThreadLocalRandom.current().nextInt(minY, maxY + 1), "cactus");
					break;

				case "skull":
					spawn = new Obstacle(this, "/resources/skull.png",
							ThreadLocalRandom.current().nextInt(minX, maxX + 1),
							ThreadLocalRandom.current().nextInt(minY, maxY + 1), "cactus");
					break;

				case "roadworks":
					obstacleEntities.add(new Obstacle(this, "/resources/cone.png", 454, maxY, "cone"));
					obstacleEntities.add(new Obstacle(this, "/resources/cone.png", 430, maxY, "cone"));
					obstacleEntities.add(new Obstacle(this, "/resources/cone.png", 406, maxY, "cone"));

					obstacleEntities.add(new Obstacle(this, "/resources/cone.png", 406, -164, "cone"));
					obstacleEntities.add(new Obstacle(this, "/resources/cone.png", 406, -188, "cone"));

					obstacleEntities.add(new Obstacle(this, "/resources/cone.png", 454, -212, "cone"));
					obstacleEntities.add(new Obstacle(this, "/resources/cone.png", 430, -212, "cone"));
					obstacleEntities.add(new Obstacle(this, "/resources/cone.png", 406, -212, "cone"));
					break;

				case "oil":
					break;

				default:
					System.err.println("tried to spawn unknown object");
					break;
				} // switch

				// add the new Obstacle to arrayList
				if (spawn != null) {
					obstacleEntities.add(spawn);
				} // if
			} else if (type.equals("comCar")) {
				switch (ThreadLocalRandom.current().nextInt(0, 9 + 1)) {
				case 0:
					comCarEntities.add(new ComCar(this, "/resources/sc1.png", minX - 30, minY, 0));
					break;
				case 1:
					comCarEntities.add(new ComCar(this, "/resources/sc2.png", minX - 30, minY, 1));
					break;
				case 2:
					comCarEntities.add(new ComCar(this, "/resources/sc3.png", minX - 30, minY, 2));
					break;
				case 3:
					comCarEntities.add(new ComCar(this, "/resources/fc1.png", minX - 30, minY, 3));
					break;
				case 4:
					comCarEntities.add(new ComCar(this, "/resources/fc2.png", minX - 30, minY, 4));
					break;
				case 5:
					comCarEntities.add(new ComCar(this, "/resources/fc3.png", minX - 30, minY, 5));
					break;
				case 6:
					comCarEntities.add(new ComCar(this, "/resources/tr1.png", minX - 70, minY, 6));
					break;
				case 7:
					comCarEntities.add(new ComCar(this, "/resources/tr2.png", minX - 70, minY, 7));
					break;
				case 8:
					comCarEntities.add(new ComCar(this, "/resources/tr3.png", minX - 70, minY, 8));
					break;
				case 9:
					comCarEntities.add(new ComCar(this, "/resources/va1.png", minX - 30, minY, 9));
					break;

				default:
					break;
				} // switch

			} // else if
		} // for
	} // spawn

	private void updateControls() {

		// car should slow down once it starts going a certain speed
		if (player.getHorizontalMovement() > 5) {
			player.setHorizontalMovement(
					player.getHorizontalMovement() - (player.getHorizontalMovement() / carResistance));
		} else if (player.getHorizontalMovement() < 5) {
			player.setHorizontalMovement(
					player.getHorizontalMovement() - (player.getHorizontalMovement() / carResistance));
		} else {
			player.setHorizontalMovement(0);
		}

		if (player.getVerticalMovement() > 5) {
			player.setVerticalMovement(player.getVerticalMovement() - (player.getVerticalMovement() / carResistance));
		} else if (player.getVerticalMovement() < 5) {
			player.setVerticalMovement(player.getVerticalMovement() - (player.getVerticalMovement() / carResistance));
		} else {
			player.setVerticalMovement(0);
		}

		// car should lose speed without input
		if (!upPressed) {
			if (player.speed > 15) {
				player.speed = player.speed - player.speed / 16;
			} else {
				player.speed = 0;
			}
		} // if

		// respond to user turning car left and right
		if ((leftPressed) && (!rightPressed) && (player.getRotation() > -90)) {
			player.setRotation(player.getRotation() - 1);
		} else if ((rightPressed) && (!leftPressed) && (player.getRotation() < 90)) {
			player.setRotation(player.getRotation() + 1);
		} // else

		// respond to user moving car up and down
		if (upPressed && player.speed < player.getMaxSpeed()) {
			player.speed += 15;
		}

		// if the player is not on the road, half maximum speed
		if (player.getX() > 470 || player.getX() < 330) {
			player.offRoad = true;
		} else {
			player.offRoad = false;
		} // if

		// fire a rocket if 'e' pressed
		if (ePressed) {
			player.useRocket();

			ePressed = false;
		} // if

		// use Nitro if 'r' pressed
		if (rPressed && player.getNumNitro() > 0) {
			if (player.useNitro()) {
				rPressed = false;
			} // if
		} // if

		if (player.speed > player.maxSpeedDefault && !player.nitroActive) {
			player.speed = player.speed - player.speed / 200;
		}

	} // updateControls

	// draw everything onto the game window!
	private void drawFrame(Graphics2D g) {
		// draw background
		drawBg(g);

		// draw obstacles
		for (int i = 0; i < obstacleEntities.size(); i++) {
			if (obstacleEntities.get(i) instanceof Obstacle) {
				((Entity) obstacleEntities.get(i)).draw(g);
			}
		}

		// draw comCars
		for (int i = 0; i < comCarEntities.size(); i++) {
			if (comCarEntities.get(i) instanceof ComCar) {
				((Entity) comCarEntities.get(i)).draw(g);
			}
		}

		// draw powerups
		for (int i = 0; i < powerupEntities.size(); i++) {
			if (powerupEntities.get(i) instanceof Powerup) {
				((Entity) powerupEntities.get(i)).draw(g);
			}
		}

		// draw projectiles
		for (int i = 0; i < projectileEntities.size(); i++) {
			projectileEntities.get(i).draw(g);
		}

		// draw player's car
		player.draw(g);

		// draw the tank
		body.draw(g);
		turret.draw(g);

		// draw particles
		drawParticles(g);

		// overlay HUD
		drawHUD(g);
	} // drawFrame

	// draw all particles
	private void drawParticles(Graphics2D g) {
		for (int i = 0; i < particles.size(); i++) {
			if (particles.get(i).update()) {
				removeParticles.add(particles.get(i));
			}
			particles.get(i).draw(g);
		} // for

		for (int i = 0; i < particleEffects.size(); i++) {
			particleEffects.get(i).draw(g);
		} // for
	} // drawParticles

	// draw background dirt and road images and update locations of background
	// "tiles" to create an endless background to drive on.
	private void drawBg(Graphics2D g) {

		// update location of entities based on car movement
		Bgy -= gameDY;

		// if the top tile is all that's left
		if (Bgy - 800 > 0) {

			// show bottom tile
			Bgy = 0;

			// spawn stuff on new tile
			updateSpawns(g);

			// car is no longer on unique initial tile
			firstTile = false;
		} // if

		// draw bottom road
		if (firstTile) {
			g.drawImage(startGround2, 0, Bgy, this);
		} else {
			g.drawImage(ground, 0, Bgy, this);
		} // else

		g.drawImage(road, 330, Bgy, this);

		// draw top road
		g.drawImage(ground, 0, Bgy - 800, this);
		g.drawImage(road, 330, Bgy - 800, this);
	} // drawBg

	// spawn all spawnable entites randomly
	private void updateSpawns(Graphics2D g) {

		// spawn obstacles, cars and power ups on new top tile randomly
		if (!firstTile) {
			spawn(100, 300, -800, 0, 1, "skull", "Obstacle");
			spawn(470, 700, -800, 0, 1, "skull", "Obstacle");
			spawn(100, 300, -800, 0, 1, "cactus", "Obstacle");
			spawn(470, 700, -800, 0, 1, "cactus", "Obstacle");
			spawn(470, 700, 0, -140, 1, "roadworks", "Obstacle");

			if (comCarEntities.size() == 0) {
				if (ThreadLocalRandom.current().nextInt(0, spawnChance * 2 + 1) == 0) {
					spawn(360, -500, 0, 0, 1, null, "comCar");
				} // if
			} // if

			if (ThreadLocalRandom.current().nextInt(0, spawnChance + 1) == 0) {
				spawn(100, 300, -800, 0, 1, "health", "Powerup");
				spawn(470, 700, -800, 0, 1, "health", "Powerup");
			} // if
			if (ThreadLocalRandom.current().nextInt(0, spawnChance + 1) == 0) {
				spawn(100, 300, -800, 0, 1, "fuel", "Powerup");
				spawn(470, 700, -800, 0, 1, "fuel", "Powerup");
			} // if
			if (ThreadLocalRandom.current().nextInt(0, spawnChance + 1) == 0) {
				spawn(300, 470, -800, 0, 1, "rocket", "Powerup");
			} // if
			if (ThreadLocalRandom.current().nextInt(0, spawnChance + 1) == 0) {
				spawn(100, 300, -800, 0, 1, "nitro", "Powerup");
				spawn(470, 700, -800, 0, 1, "nitro", "Powerup");
			} // if
			if (ThreadLocalRandom.current().nextInt(0, spawnChance + 1) == 0) {
				spawn(100, 300, -800, 0, 1, "shield", "Powerup");
				spawn(470, 700, -800, 0, 1, "shield", "Powerup");
			} // if
		} // if

	} // updateSpawns

	// draw the HUD
	private void drawHUD(Graphics2D g) {

		// draw HUD graphics
		g.drawImage(HUDoverlay, 0, 0, this);

		// health and fuel bars
		drawBars(g);

		// inventory numbers
		drawImageNumber(g, player.getNumRocket(), 60, 436);
		drawImageNumber(g, player.getNumNitro(), 60, 476);
		drawImageNumber(g, player.getNumShield(), 60, 520);

		// stats
		drawImageNumber(g, player.getCarStats("speed"), 64, 320);
		drawImageNumber(g, player.getCarStats("health"), 64, 344);
		drawImageNumber(g, player.getCarStats("storage"), 64, 368);

		// scores
		drawImageNumber(g, score, 708, 72);
		drawImageNumber(g, bestscore, 708, 136);

		// progressArrow
		g.drawImage(arrow, 750 - 14, (int) (((double) (distanceTravelled / 20) / distancegoal * 315) + 586 - 14), this);

		// draw shielded overlay
		if (player.getShieldActive()) {
			g.setColor(Color.yellow);
			g.setStroke(new BasicStroke(5));
			g.drawRect(10, 238, 80, 75);
			g.setStroke(new BasicStroke(1));
		} // if
	} // drawHUD

	// draw health and fuel bars
	private void drawBars(Graphics2D g) {

		// health bar
		if (player.getHealth() > 0) {
			g.setColor(new Color(184, 0, 0));
			g.fillRect(20, 56, 18, player.getHealth());
		} // if

		// fuel bar
		g.setColor(new Color(154, 91, 11));
		g.fillRect(68, 56, 18, player.getFuel());
	} // drawBars

	// draw debug view when requested, includes entity hitboxes and game info
	private void drawTestInfo(Graphics2D g) {

		// black box
		g.setColor(Color.black);
		g.fillRect(100, 700, 180, 70);

		// text
		g.setColor(Color.white);
		g.drawString(distanceTravelledMeters + "m, Current Speed: " + (int) ((Math.abs(gameDY) / 20) * delta) + "m/s",
				110, 715);
		g.drawString(Math.round((System.currentTimeMillis() - gameStartTime) / 1000) + "s", 110, 728);
		g.drawString(player.getHealth() + "/" + player.healthStat * 25 + " HP", 110, 741);
		g.setColor(Color.yellow);
		if (delta > 0) {
			g.drawString(1000 / delta + "FPS", 110, 754);
		} // if

		// draw some collision boxes
		int degree = (int) player.getRotation(); // desired degree
		int rectX = player.getX() + player.getVehicleBorder();
		int rectY = player.getY();
		int rectWidth = player.getVehicleWidth();
		int rectHeight = player.getImageHeight();

		// creating the rectangle to rotate
		Shape playerCollisions = new Rectangle(rectX, rectY, rectWidth, rectHeight);

		// Affine transform Object for hit box rotations
		AffineTransform transform = new AffineTransform();

		// specify rotation amount
		transform.rotate(Math.toRadians(degree), rectX + rectWidth / 2, rectY + rectHeight / 2);

		// rotate rectangle
		playerCollisions = transform.createTransformedShape(playerCollisions);

		// Draw new rectangle on screen
		g.setColor(Color.green);
		g.draw(playerCollisions);

		// draw all obstacle collision boxes
		for (int i = 0; i < obstacleEntities.size(); i++) {
			g.setColor(Color.red);
			g.drawRect(obstacleEntities.get(i).getX(), obstacleEntities.get(i).getY(),
					obstacleEntities.get(i).getImageWidth(), obstacleEntities.get(i).getImageHeight());
		} // for

		// draw all projectile collision boxes
		for (int i = 0; i < projectileEntities.size(); i++) {
			if (projectileEntities.get(i) instanceof Rocket) {
				g.setColor(Color.green);
			} else {
				g.setColor(Color.red);
			} // else
			g.drawRect(projectileEntities.get(i).getX(), projectileEntities.get(i).getY(),
					projectileEntities.get(i).getImageWidth(), projectileEntities.get(i).getImageHeight());
		} // for

		// draw all power up collision boxes
		for (int i = 0; i < powerupEntities.size(); i++) {
			g.setColor(Color.green);
			g.drawRect(powerupEntities.get(i).getX(), powerupEntities.get(i).getY(),
					powerupEntities.get(i).getImageWidth(), powerupEntities.get(i).getImageHeight());
		} // for

		// draw optimal targeting line between tank turret and player
		g.setColor(Color.red);
		g.drawLine(player.getX() + player.getImageWidth() / 2, player.getY() + player.getImageHeight() / 2,
				turret.getX() + turret.getImageWidth() / 2, turret.getY() + turret.getImageHeight() / 2);

	} // drawTestInfo

	// draws a number made of images for each digit given a number to draw and a
	// starting point
	private void drawImageNumber(Graphics2D g, int number, int x, int y) {

		// make a string from the number
		String numberStr = number + "";

		// draw number images starting at x, y coords
		int pos = 0;
		for (int i = 0; i < numberStr.length(); i++) {

			switch (numberStr.charAt(i)) {
			case (0 + 48):
				g.drawImage(zero, x + pos, y, this);
				break;
			case (1 + 48):
				g.drawImage(one, x + pos, y, this);
				break;
			case (2 + 48):
				g.drawImage(two, x + pos, y, this);
				break;
			case (3 + 48):
				g.drawImage(three, x + pos, y, this);
				break;
			case (4 + 48):
				g.drawImage(four, x + pos, y, this);
				break;
			case (5 + 48):
				g.drawImage(five, x + pos, y, this);
				break;
			case (6 + 48):
				g.drawImage(six, x + pos, y, this);
				break;
			case (7 + 48):
				g.drawImage(seven, x + pos, y, this);
				break;
			case (8 + 48):
				g.drawImage(eight, x + pos, y, this);
				break;
			case (9 + 48):
				g.drawImage(nine, x + pos, y, this);
				break;
			default:
				break;
			} // switch

			// increment drawing position according to character width
			pos += 16;
		} // for
	} // drawImageNumber

	/*
	 * inner class KeyInputHandler handles keyboard input from the user
	 */
	private class KeyInputHandler extends KeyAdapter {

		/*
		 * The following methods are required for any class that extends the
		 * abstract class KeyAdapter. They handle keyPressed, keyReleased and
		 * keyTyped events.
		 */
		public void keyPressed(KeyEvent e) {

			// respond to move left, right and up keys being pressed
			if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
				leftPressed = true;
			} // if

			if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
				rightPressed = true;
			} // if

			if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
				upPressed = true;
			} // if
		} // keyPressed

		public void keyReleased(KeyEvent e) {

			// any key pressed
			anyPressed = !anyPressed;

			// enter key to restart game
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				enterPressed = true;
			} // if

			// left arrow or a to turn car left
			if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
				leftPressed = false;
			} // if

			// right arrow or d to turn car right
			if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
				rightPressed = false;
			} // if

			// up arrow or w to move car forward
			if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
				upPressed = false;
			} // if

			// r key to use nitro
			if (e.getKeyCode() == KeyEvent.VK_R) {
				rPressed = true;
			} // if

			// if ']' is pressed, show debug
			if (e.getKeyChar() == KeyEvent.VK_CLOSE_BRACKET) {
				showDebug = !showDebug;
			}

			// e key to use a rocket
			if (e.getKeyCode() == KeyEvent.VK_E) {
				ePressed = !ePressed;
			} // if
		} // keyReleased

		public void keyTyped(KeyEvent e) {
			// if escape is pressed, end game
			if (e.getKeyChar() == 27) {
				System.exit(0);
			} // if escape pressed
		} // keyTyped
	} // class KeyInputHandler

	// adds a given entity to a list of entities to remove, this list is cleared
	// in the game loop
	public void removeEntity(Entity entity) {
		removeEntities.add(entity);
	} // removeEntity

	// called after any event that should lose the game
	public void gameOver() {
		gameOver = true;
	} // gameOver
} // Game
