package dev.cgj.games;

import dev.cgj.games.entity.*;
import dev.cgj.games.particle.Particle;
import dev.cgj.games.particle.ParticleEffect;

import javax.swing.JFrame;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class EscapeGame {
    private final KeyInputHandler keyInputHandler;
    private final EscapeGameRenderer renderer;
    private GamePhase phase;

    int distanceTravelled; // distance traveled in pixels
    public int distanceTravelledMeters; // distance traveled in meters
    int distancegoal = 1000; // how far the player needs to drive to win

    public int totalHealthLost = 0; // used to calculate score
    public int obstaclesDestroyed = 0; // used to calculate score
    public int score = 0; // score this game
    public int bestScore = 0; // best score (read from "DesertEscapeScores.txt")
    public int spawnChance = 5; // chance to spawn each obstacle type on a new tile (1 in spawnChance)

    Car player; // entity for the player's car
    TankBody body; // entity for the tank's main body
    TankTurret turret; // entity for the tank turret (can be rotated)

    int Bgx = 0; // x position to draw the background at
    int Bgy = 0; // y position to draw the background at
    public boolean firstTile = true; // is the player on the unique first tile?

    long lastSpawnLoop = 0;
    long lastFuelLoop = 0;
    long lastFireLoop = 0;
    long lastImageBlinkLoop = 0;
    long gameStartTime;

    public ArrayList<Obstacle> obstacleEntities = new ArrayList<>();
    public ArrayList<ComCar> comCarEntities = new ArrayList<>();
    public ArrayList<Projectile> projectileEntities = new ArrayList<>();
    public ArrayList<Powerup> powerupEntities = new ArrayList<>();
    private final ArrayList<Entity> removeEntities = new ArrayList<>();

    public final ArrayList<Particle> particles = new ArrayList<>();
    public ArrayList<ParticleEffect> particleEffects = new ArrayList<>();
    public final ArrayList<Particle> removeParticleEffects = new ArrayList<>();
    public final ArrayList<Particle> removeParticles = new ArrayList<>();

    public double gameDY = 0; // movement for all entities this loop

    /**
     * Construct our game and set it running.
     */
    public EscapeGame(JFrame container) {
        gameStartTime = System.currentTimeMillis();
        keyInputHandler = new KeyInputHandler();
        renderer = new EscapeGameRenderer(container);
        renderer.addKeyListener(keyInputHandler);
    }

    public void run() {
        phase = GamePhase.MENU;
        while (true) {
            init();
            menuLoop();
            gameLoop();

            // show gameOver/gameWin screens
            renderer.drawEndScreen(bestScore, distanceTravelledMeters, phase, score);

            // clear graphics and flip buffer
            renderer.renderFrame();

            // wait until user presses 'esc' to exit or presses enter for a new game
            boolean repeatGame = false;
            keyInputHandler.setEnterPressed(false);
            while (!repeatGame) {
                if (keyInputHandler.isEnterPressed()) {
                    repeatGame = true;
                }
                if (keyInputHandler.isEnterPressed()) {
                    repeatGame = true;
                }

                // prevent to loop from running too fast with a small delay
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void menuLoop() {
        while (phase == GamePhase.MENU) {
            renderer.drawMenu(this);
            renderer.renderFrame();
            if (keyInputHandler.isEnterPressed()) {
                phase = GamePhase.RUNNING;
            }
        }
    }

    /**
     * set all variables to their correct values at the start of the game
     */
    private void init() {

        // reset all variables to default values
        distanceTravelled = 0;
        distanceTravelledMeters = 0;
        totalHealthLost = 0;
        obstaclesDestroyed = 0;
        score = 0;
        spawnChance = 5;
        Bgx = 0;
        Bgy = 0;
        firstTile = true;
        lastSpawnLoop = 0;
        lastFuelLoop = 0;
        gameStartTime = 0;
        lastFireLoop = 0;
        lastImageBlinkLoop = 0;

        // clear existing projectiles and obstacles
        obstacleEntities.clear();
        projectileEntities.clear();

        // Initialize player car and enemy tank objects
        player = new Car(this, "/sprites/sc1.png", 400, 500);
        body = new TankBody(this, "/sprites/body1.png", (EscapeGameRenderer.PAGE_WIDTH / 2) - 38, EscapeGameRenderer.PAGE_HEIGHT);
        turret = new TankTurret(this, "/sprites/turret.png", (EscapeGameRenderer.PAGE_WIDTH / 2) - 84, EscapeGameRenderer.PAGE_HEIGHT - 21, player);

        // get HighScore from text file
        try {
            BufferedReader in = new BufferedReader(new FileReader("DesertEscapeScores.txt"));
            in.mark(Short.MAX_VALUE);
            bestScore = Integer.parseInt(in.readLine());
            in.close();
        } catch (Exception e) {
            System.out.println("Scores File Not Found\n");
            bestScore = 0;
        }
    }

    public void gameLoop() {

        // initialize lastLoopTime
        long lastLoopTime = System.currentTimeMillis();

        // keep loop running until game ends
        while (phase == GamePhase.RUNNING) {

            // calculate time since last update
            long delta = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();

            // get graphics context for the accelerated surface and make it red
            renderer.drawRed();

            // remove dead entities
            obstacleEntities.removeAll(removeEntities);
            projectileEntities.removeAll(removeEntities);
            powerupEntities.removeAll(removeEntities);
            comCarEntities.removeAll(removeEntities);
            removeEntities.clear();

            // update car logic
            player.updateStats();

            // update stuff according to user input
            updateControls(player, keyInputHandler);

            // update movement
            moveEntities(delta);

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
            }

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
            }

            if (score < 0) {
                score = 0;
            }

            // draw frame
            renderer.drawFrame(this);

            // show debug if requested
            if (keyInputHandler.isShowDebug()) {
                renderer.drawTestInfo(
                        delta,
                        distanceTravelledMeters,
                        gameDY,
                        gameStartTime,
                        obstacleEntities,
                        player,
                        powerupEntities,
                        projectileEntities,
                        turret
                );
            }

            renderer.renderFrame();

            // pause
            try {
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void spawnParticleEffects(int x, int y, int dx, int dy, Color effectColor, int life, int numParticles,
                                     String type, int size) {
        particleEffects.add(new ParticleEffect(x, y, dx, dy, effectColor, life, numParticles, type, size));
    }

    private void updateParticles() {

        // create exhaust particles
        if (gameDY < 0) {
            // randomize particle parameters
            double randomDX = (-6 + (Math.random() * ((6 + 6) + 1)));
            double randomDY = (6 + (Math.random() * (1)));
            int randomLifespan = (int) (10 + (Math.random() * ((20 - 10) + 1)));
            int randColor = (int) (20 + (Math.random() * ((100 - 20) + 1)));

            // create new particle
            particles.add(
                    new Particle(player.getX() + player.getImageWidth() / 2, player.getY() + player.getImageHeight(),
                            randomDX, randomDY, 4, randomLifespan, new Color(randColor, randColor, randColor), 0.1));
        }

        // remove spent particles and effects
        particles.removeAll(removeParticles);
        particles.removeAll(removeParticleEffects);
        removeParticles.clear();
        removeParticleEffects.clear();
    }

    private void updateTank() {
        body.update();
        turret.update();

        // if enough time has passed and turret is aimed, fire
        if (System.currentTimeMillis() - lastFireLoop > 1000) {
            turret.fire();
            lastFireLoop = System.currentTimeMillis();
        }
    }

    private void moveEntities(long delta) {
        // update entity movement relative to player
        if (player.speed > 0) {
            gameDY = (delta * player.getVerticalMovement()) / 1000;
        } else {
            gameDY = 0;
        }

        // move projectiles
        for (int i = 0; i < projectileEntities.size(); i++) {
            projectileEntities.get(i).move(i);
        }

        // move obstacles
        for (int i = 0; i < obstacleEntities.size(); i++) {
            obstacleEntities.get(i).move(i);
        }

        // move comCars
        for (int i = 0; i < comCarEntities.size(); i++) {
            comCarEntities.get(i).move(i);
        }

        // move power ups
        for (int i = 0; i < powerupEntities.size(); i++) {
            powerupEntities.get(i).move(i);
        }

        // move player car
        player.move(delta);
    }

    private void checkWin() {
        if (Math.abs(distanceTravelled / 20) > distancegoal) {
            phase = GamePhase.WON;
        }
    }

    private void checkCollisions() {

        // check collisions with obstacles
        for (Obstacle me : obstacleEntities) {
            if (me.collidesWith(player) && !me.getPastCollision()) {
                me.collidedWith(player);
                player.collidedWith(me);
            }
        }

        // check collisions with shells
        for (Projectile me : projectileEntities) {
            if (me.collidesWith(player) && !me.getPastCollision()) {
                me.collidedWith(player);
                player.collidedWith(me);
            }
        }

        // check collisions with power ups
        for (Powerup me : powerupEntities) {
            if (me.collidesWith(player) && !me.getPastCollision()) {
                me.collidedWith(player);
                player.collidedWith(me);
            }
        }

        // check collisions with comCars and notify entities if true
        for (ComCar me : comCarEntities) {
            if (me.collidesWith(player)) {
                me.collidedWith(player);
                player.collidedWith(me);
            }
        }

        // check projectile collisions with obstacles
        for (Projectile me : projectileEntities) {
            for (Obstacle other : obstacleEntities) {
                if (me.collidesWith(other) && !me.getPastCollision()) {
                    me.collidedWith(other);
                    other.collidedWith(me);
                }
            }
        }
    }

    private List<Obstacle> spawnObstaclesOfType(int minX, int maxX, int minY, int maxY, ObstacleType type) {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        return switch (type) {
            case ObstacleType.CACTUS -> Collections.singletonList(new Obstacle(this,
                    random.nextInt(minX, maxX + 1), random.nextInt(minY, maxY + 1), ObstacleType.CACTUS));
            case ObstacleType.SKULL -> Collections.singletonList(new Obstacle(this,
                    random.nextInt(minX, maxX + 1), random.nextInt(minY, maxY + 1), ObstacleType.SKULL));
            case ObstacleType.CONE -> Arrays.asList(
                    new Obstacle(this, 454, maxY, ObstacleType.CONE),
                    new Obstacle(this, 430, maxY, ObstacleType.CONE),
                    new Obstacle(this, 406, maxY, ObstacleType.CONE),
                    new Obstacle(this, 406, -164, ObstacleType.CONE),
                    new Obstacle(this, 406, -188, ObstacleType.CONE),
                    new Obstacle(this, 454, -212, ObstacleType.CONE),
                    new Obstacle(this, 430, -212, ObstacleType.CONE),
                    new Obstacle(this, 406, -212, ObstacleType.CONE)
            );
            case ObstacleType.OIL ->
                // TODO: Add oil obstacle type
                Collections.emptyList();
        };
    }

    private Powerup spawnPowerupOfType(int minX, int maxX, int minY, int maxY, PowerupType type) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int x = random.nextInt(minX, maxX + 1);
        int y = random.nextInt(minY, maxY + 1);
        return new Powerup(this, type.getSpritePath(), x, y, type);
    }

    private void updateControls(Car player, KeyInputHandler inputHandler) {

        // car should slow down once it starts going a certain speed
        // dx/dy reduction
        double carResistance = 8;
        if (player.getHorizontalMovement() > 5) {
            player.setHorizontalMovement(player.getHorizontalMovement() - (player.getHorizontalMovement() / carResistance));
        } else if (player.getHorizontalMovement() < 5) {
            player.setHorizontalMovement(player.getHorizontalMovement() - (player.getHorizontalMovement() / carResistance));
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
        if (!inputHandler.isUpPressed()) {
            if (player.speed > 15) {
                player.speed = player.speed - player.speed / 16;
            } else {
                player.speed = 0;
            }
        }

        // respond to user turning car left and right
        if ((inputHandler.isLeftPressed()) && (!inputHandler.isRightPressed()) && (player.getRotation() > -90)) {
            player.setRotation(player.getRotation() - 1);
        } else if ((inputHandler.isRightPressed()) && (!inputHandler.isLeftPressed()) && (player.getRotation() < 90)) {
            player.setRotation(player.getRotation() + 1);
        }

        // respond to user moving car up and down
        if (inputHandler.isUpPressed() && player.speed < player.getMaxSpeed()) {
            player.speed += 15;
        }

        // if the player is not on the road, half maximum speed
        player.offRoad = player.getX() > 470 || player.getX() < 330;

        // fire a rocket if 'e' pressed
        if (inputHandler.isePressed()) {
            player.useRocket();
            inputHandler.setePressed(false);
        }

        // use Nitro if 'r' pressed
        if (inputHandler.isrPressed() && player.getNumNitro() > 0) {
            if (player.useNitro()) {
                inputHandler.setrPressed(false);
            }
        }

        if (player.speed > player.maxSpeedDefault && !player.nitroActive) {
            player.speed = player.speed - player.speed / 200;
        }
    }

    /**
     * Spawn obstacles, cars and powerups randomly.
     */
    public void updateSpawns() {
        obstacleEntities.addAll(spawnObstaclesOfType(100, 300, -800, 0, ObstacleType.SKULL));
        obstacleEntities.addAll(spawnObstaclesOfType(470, 700, -800, 0, ObstacleType.SKULL));
        obstacleEntities.addAll(spawnObstaclesOfType(100, 300, -800, 0, ObstacleType.CACTUS));
        obstacleEntities.addAll(spawnObstaclesOfType(470, 700, -800, 0, ObstacleType.CACTUS));
        obstacleEntities.addAll(spawnObstaclesOfType(470, 700, 0, -140, ObstacleType.CONE));

        ThreadLocalRandom random = ThreadLocalRandom.current();
        if (comCarEntities.isEmpty()) {
            if (random.nextInt(0, spawnChance * 2 + 1) == 0) {
                comCarEntities.add(new ComCar(this, 360 - 30, -500, CarType.getRandomCarType()));
            }
        }

        if (random.nextInt(0, spawnChance + 1) == 0) {
            powerupEntities.add(spawnPowerupOfType(100, 300, -800, 0, PowerupType.HEALTH));
            powerupEntities.add(spawnPowerupOfType(470, 700, -800, 0, PowerupType.HEALTH));
        }
        if (random.nextInt(0, spawnChance + 1) == 0) {
            powerupEntities.add(spawnPowerupOfType(100, 300, -800, 0, PowerupType.FUEL));
            powerupEntities.add(spawnPowerupOfType(470, 700, -800, 0, PowerupType.FUEL));
        }
        if (random.nextInt(0, spawnChance + 1) == 0) {
            powerupEntities.add(spawnPowerupOfType(300, 470, -800, 0, PowerupType.ROCKET));
        }
        if (random.nextInt(0, spawnChance + 1) == 0) {
            powerupEntities.add(spawnPowerupOfType(100, 300, -800, 0, PowerupType.NITRO));
            powerupEntities.add(spawnPowerupOfType(470, 700, -800, 0, PowerupType.NITRO));
        }
        if (random.nextInt(0, spawnChance + 1) == 0) {
            powerupEntities.add(spawnPowerupOfType(100, 300, -800, 0, PowerupType.SHIELD));
            powerupEntities.add(spawnPowerupOfType(470, 700, -800, 0, PowerupType.SHIELD));
        }
    }

    /**
     * adds a given entity to a list of entities to remove, this list is cleared in the game loop.
     */
    public void removeEntity(Entity entity) {
        removeEntities.add(entity);
    }

    /**
     * Called after any event that should lose the game.
     */
    public void gameOver() {
        phase = GamePhase.LOST;
    }
}
