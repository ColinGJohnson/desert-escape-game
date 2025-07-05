package dev.cgj.games.old;

import dev.cgj.games.old.entity.Car;
import dev.cgj.games.old.entity.ComCar;
import dev.cgj.games.old.entity.Obstacle;
import dev.cgj.games.old.entity.Powerup;
import dev.cgj.games.old.entity.Projectile;
import dev.cgj.games.old.entity.Rocket;
import dev.cgj.games.old.entity.TankTurret;
import dev.cgj.games.old.particle.Particle;
import dev.cgj.games.old.particle.ParticleEffect;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class EscapeGameRenderer extends Canvas {
    Image road = null;
    Image ground = null;
    Image startGround = null;
    Image startGround2 = null;
    Image startGame = null;
    Image HUDoverlay = null;
    Image arrow = null;

    public static final int PAGE_HEIGHT = 800; // height of game window
    public static final int PAGE_WIDTH = 800; // width of game window

    private final BufferStrategy strategy;
    private final Graphics2D g;
    private final DigitRenderer digitRenderer;

    public EscapeGameRenderer(JFrame container) {
        digitRenderer = new DigitRenderer();
        loadImages();

        // get hold the content of the frame
        JPanel gamePanel = (JPanel) container.getContentPane();

        // set up the resolution of the game
        gamePanel.setPreferredSize(new Dimension(800, 800));
        gamePanel.setLayout(null);

        // set up canvas size (this) and add to frame
        setBounds(0, 0, 800, 800);
        gamePanel.add(this);

        // Tell AWT not to bother repainting canvas since that will be done using graphics acceleration
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
            }
        });

        // request focus so key events are handled by this canvas
        requestFocus();

        // create buffer strategy to take advantage of accelerated graphics
        createBufferStrategy(2);
        strategy = getBufferStrategy();

        // get graphics context and turn on antialiasing
        g = (Graphics2D) strategy.getDrawGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
    }

    /**
     * clear graphics and flip buffer
     */
    public void renderFrame() {
        strategy.show();
    }

    /**
     * Draw everything onto the game window.
     */
    public void drawFrame(EscapeGame escapeGame) {

        // draw background
        drawBg(escapeGame, escapeGame.gameDY);

        // draw obstacles
        for (Obstacle obstacleEntity : escapeGame.obstacleEntities) {
            obstacleEntity.draw(g);
        }

        // draw comCars
        for (ComCar comCarEntity : escapeGame.comCarEntities) {
            comCarEntity.draw(g);
        }

        // draw powerups
        for (Powerup powerupEntity : escapeGame.powerupEntities) {
            powerupEntity.draw(g);
        }

        // draw projectiles
        for (Projectile projectileEntity : escapeGame.projectileEntities) {
            projectileEntity.draw(g);
        }

        // draw player's car
        escapeGame.player.draw(g);

        // draw the tank
        escapeGame.body.draw(g);
        escapeGame.turret.draw(g);

        // draw particles
        drawParticles(escapeGame.particleEffects, escapeGame.particles, escapeGame.removeParticles, g);

        // overlay HUD
        drawHUD(escapeGame.bestScore, escapeGame.distanceTravelled, escapeGame.distancegoal, escapeGame.player, escapeGame.score);
    }

    /**
     * Draw all particles.
     */
    public void drawParticles(ArrayList<ParticleEffect> particleEffects, ArrayList<Particle> particles,
                              ArrayList<Particle> removeParticles, Graphics2D g) {
        for (Particle particle : particles) {
            if (particle.update()) {
                removeParticles.add(particle);
            }
            particle.draw(g);
        }

        for (ParticleEffect particleEffect : particleEffects) {
            particleEffect.draw(g);
        }
    }

    /**
     * draw debug view when requested, includes entity hitboxes and game info
     */
    public void drawTestInfo(long delta, int distanceTravelledMeters, double gameDY, long gameStartTime,
                             ArrayList<Obstacle> obstacleEntities, Car player, ArrayList<Powerup> powerupEntities,
                             ArrayList<Projectile> projectileEntities, TankTurret turret) {

        // black box
        g.setColor(Color.black);
        g.fillRect(100, 700, 180, 70);

        // text
        g.setColor(Color.white);
        g.drawString(distanceTravelledMeters + "m, Current Speed: " + (int) ((Math.abs(gameDY) / 20) * delta) + "m/s",
                110, 715);
        g.drawString(Math.round((System.currentTimeMillis() - gameStartTime) / 1000f) + "s", 110, 728);
        g.drawString(player.getHealth() + "/" + player.healthStat * 25 + " HP", 110, 741);
        g.setColor(Color.yellow);
        if (delta > 0) {
            g.drawString(1000 / delta + "FPS", 110, 754);
        }

        // draw some collision boxes
        Shape playerCollisions = getPlayerHitbox(player);

        // Draw new rectangle on screen
        g.setColor(Color.green);
        g.draw(playerCollisions);

        // draw all obstacle collision boxes
        for (Obstacle obstacleEntity : obstacleEntities) {
            g.setColor(Color.red);
            g.drawRect(obstacleEntity.getX(), obstacleEntity.getY(),
                    obstacleEntity.getImageWidth(), obstacleEntity.getImageHeight());
        }

        // draw all projectile collision boxes
        for (Projectile projectileEntity : projectileEntities) {
            if (projectileEntity instanceof Rocket) {
                g.setColor(Color.green);
            } else {
                g.setColor(Color.red);
            }
            g.drawRect(projectileEntity.getX(), projectileEntity.getY(),
                    projectileEntity.getImageWidth(), projectileEntity.getImageHeight());
        }

        // draw all power up collision boxes
        for (Powerup powerupEntity : powerupEntities) {
            g.setColor(Color.green);
            g.drawRect(powerupEntity.getX(), powerupEntity.getY(),
                    powerupEntity.getImageWidth(), powerupEntity.getImageHeight());
        }

        // draw optimal targeting line between tank turret and player
        g.setColor(Color.red);
        g.drawLine(player.getX() + player.getImageWidth() / 2, player.getY() + player.getImageHeight() / 2,
                turret.getX() + turret.getImageWidth() / 2, turret.getY() + turret.getImageHeight() / 2);
    }

    private Shape getPlayerHitbox(Car player) {
        int degree = (int) player.getRotation();
        int rectX = player.getX() + player.currentCar.getBorder();
        int rectY = player.getY();
        int rectWidth = player.currentCar.getWidth();
        int rectHeight = player.getImageHeight();

        // creating the rectangle to rotate
        Shape playerCollisions = new Rectangle(rectX, rectY, rectWidth, rectHeight);

        // Affine transform Object for hit box rotations
        AffineTransform transform = new AffineTransform();

        // specify rotation amount
        transform.rotate(Math.toRadians(degree), rectX + rectWidth / 2f, rectY + rectHeight / 2f);

        // rotate rectangle
        playerCollisions = transform.createTransformedShape(playerCollisions);
        return playerCollisions;
    }

    public void drawEndScreen(int bestscore, int distanceTravelledMeters, GamePhase gameState, int score) {
        if (gameState == GamePhase.LOST) {
            g.setColor(new Color(204, 0, 0));
            g.fillRect(100, 350, 600, 125);
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.PLAIN, 40));
            g.drawString("Game Over!", 290, 400);
            g.setFont(new Font("Arial", Font.PLAIN, 14));
            g.drawString("Distance Travelled: " + distanceTravelledMeters + "m", 320, 425);
        } else if (gameState == GamePhase.WON) {
            g.setColor(new Color(0, 204, 0));
            g.fillRect(100, 350, 600, 125);
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.PLAIN, 40));
            g.drawString("You Win!", 315, 400);
            g.setFont(new Font("Arial", Font.PLAIN, 14));
            g.drawString("You scored: " + score + " points", 320, 425);
        }

        if (score > bestscore) {
            g.drawString("New High Score!", 345, 440);
        }
        g.drawString("Exit: 'Esc' | New Game: 'Enter'", 300, 455);

        // write high score to file
        if (score > bestscore) {
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter("DesertEscapeScores.txt"));
                out.write(String.valueOf(score));
                out.close();
            } catch (Exception e) {
                System.out.println("Score Output Error");
            }
        }
    }

    void drawRed() {
        g.setColor(Color.red);
        g.fillRect(0, 0, PAGE_WIDTH, PAGE_HEIGHT);
    }

    void drawMenu(EscapeGame escapeGame) {

        // menu background
        g.setColor(Color.red);
        g.drawImage(startGround, 0, 0, this);

        // draw the "start game" image for one second, then hide it for another second
        if (System.currentTimeMillis() > escapeGame.lastImageBlinkLoop + 1000) {
            g.drawImage(startGame, 232, 149, this);
            if (System.currentTimeMillis() > escapeGame.lastImageBlinkLoop + 2000) {
                escapeGame.lastImageBlinkLoop = System.currentTimeMillis();
            }
        }
    }

    /**
     * Draw background dirt and road images and update locations of background "tiles" to create an endless background
     * to drive on.
     */
    void drawBg(EscapeGame escapeGame, double gameDY) {

        // update location of entities based on car movement
        escapeGame.Bgy -= gameDY;

        // if the top tile is all that's left
        if (escapeGame.Bgy - 800 > 0) {

            // show bottom tile
            escapeGame.Bgy = 0;

            // spawn stuff on new tile
            if (!escapeGame.firstTile) {
                escapeGame.updateSpawns();
            }

            // car is no longer on unique initial tile
            escapeGame.firstTile = false;
        }

        // draw bottom road
        if (escapeGame.firstTile) {
            g.drawImage(startGround2, 0, escapeGame.Bgy, this);
        } else {
            g.drawImage(ground, 0, escapeGame.Bgy, this);
        }

        g.drawImage(road, 330, escapeGame.Bgy, this);

        // draw top road
        g.drawImage(ground, 0, escapeGame.Bgy - 800, this);
        g.drawImage(road, 330, escapeGame.Bgy - 800, this);
    }

    /**
     * Draw the HUD.
     */
    void drawHUD(int bestscore, int distanceTravelled, int distancegoal, Car player, int score) {

        // draw HUD graphics
        g.drawImage(HUDoverlay, 0, 0, this);

        // health and fuel bars
        drawBars(player);

        // inventory numbers
        digitRenderer.drawImageNumber(g, this, player.getNumRocket(), 60, 436);
        digitRenderer.drawImageNumber(g, this, player.getNumNitro(), 60, 476);
        digitRenderer.drawImageNumber(g, this, player.getNumShield(), 60, 520);

        // stats
        digitRenderer.drawImageNumber(g, this, player.speedStat, 64, 320);
        digitRenderer.drawImageNumber(g, this, player.healthStat, 64, 344);
        digitRenderer.drawImageNumber(g, this, player.storageStat, 64, 368);

        // scores
        digitRenderer.drawImageNumber(g, this, score, 708, 72);
        digitRenderer.drawImageNumber(g, this, bestscore, 708, 136);

        // progressArrow
        g.drawImage(arrow, 750 - 14, (int) (((double) (distanceTravelled / 20) / distancegoal * 315) + 586 - 14), this);

        // draw shielded overlay
        if (player.getShieldActive()) {
            g.setColor(Color.yellow);
            g.setStroke(new BasicStroke(5));
            g.drawRect(10, 238, 80, 75);
            g.setStroke(new BasicStroke(1));
        }
    }

    /**
     * draw health and fuel bars
     */
    private void drawBars(Car player) {

        // health bar
        if (player.getHealth() > 0) {
            g.setColor(new Color(184, 0, 0));
            g.fillRect(20, 56, 18, player.getHealth());
        }

        // fuel bar
        g.setColor(new Color(154, 91, 11));
        g.fillRect(68, 56, 18, player.getFuel());
    }

    /**
     * sets image variables to their respective images
     */
    private void loadImages() {
        try {
            road = ImageIO.read(EscapeGame.class.getResource("/sprites/road.jpg"));
            startGame = ImageIO.read(EscapeGame.class.getResource("/sprites/pressAnyKeyToStart.png"));
            HUDoverlay = ImageIO.read(EscapeGame.class.getResource("/sprites/HUD.png"));
            arrow = ImageIO.read(EscapeGame.class.getResource("/sprites/arrow.png"));
            ground = ImageIO.read(EscapeGame.class.getResource("/sprites/ground.jpg"));
            startGround2 = ImageIO.read(EscapeGame.class.getResource("/sprites/startGround2.jpg"));
            startGround = ImageIO.read(EscapeGame.class.getResource("/sprites/startGround1.jpg"));
        } catch (IOException e) {
            throw new RuntimeException("Error loading images", e);
        }
    }
}
