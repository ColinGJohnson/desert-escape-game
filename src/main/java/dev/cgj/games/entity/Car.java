package dev.cgj.games.entity;

import dev.cgj.games.CarType;
import dev.cgj.games.EscapeGame;
import dev.cgj.games.PowerupType;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

public class Car extends Entity {
    public static final double OFF_ROAD_PENALTY = 0.75;
    private final EscapeGame game;

    private int health = 100;
    private int fuel = 100;
    private int maxFuel = 100;
    private int numRocket = 0;
    private int numNitro = 10;
    private int numShield = 0;

    public CarType currentCar = CarType.SPORTS;
    public int healthStat = 1;
    public int storageStat = 2;
    public int speedStat = 7;
    public int maxSpeedDefault = 700;
    public int maxSpeed = 700;

    private int usedNitroFrames = 0;
    public boolean nitroActive = false;
    private boolean shieldActive = false;
    private double rotation = 0;
    public int speed = 0;
    public boolean offRoad = false;

    public Car(EscapeGame game, String r, int x, int y) {
        super(r, x, y);
        this.game = game;
    }

    public void move(long delta) {

        // keep player on viewable play area, area is smaller on unique first tile
        if (game.distanceTravelledMeters < 25) {

            // stop at left side of screen
            if ((dx < 0) && (x < 300)) {
                x = 300;
                return;
            }

            // stop at right side of screen
            if ((dx > 0) && (x > 450)) {
                x = 450;
                return;
            }
        } else {

            // stop at left side of screen
            if ((dx < 0) && (x < 86)) {
                x = 86;
                return;
            }

            // stop at right side of screen
            if ((dx > 0) && (x > 650)) {
                x = 650;
                return;
            }
        }

        // set x and y velocities based on car angle
        dx = -speed * Math.cos(Math.toRadians(rotation + 90));
        dy = -speed * Math.sin(Math.toRadians(rotation + 90));

        // update location of entity based on move speeds
        if (offRoad) {
            x += (delta * (dx)) / 1000;
        } else {
            x += (delta * (dx * OFF_ROAD_PENALTY)) / 1000;
        }

        if (y > 502) {
            y--;
        } else if (y < 488) {
            y++;
        }
    }

    /**
     * Checks if the car's refined and rotated collision box has collided with "other" (another entity)
     */
    public void collidedWith(Entity other) {

        // creating the rectangle you want to rotate
        Shape playerCollisions = new Rectangle(getX() + currentCar.getBorder(), getY(), currentCar.getWidth(), getImageHeight());

        // creating other rectangle to check intersection
        Rectangle otherHitbox = new Rectangle(other.getX(), other.getY(), other.getImageWidth(), other.getImageHeight());

        // Affine transform Object for hit box rotations
        AffineTransform transform = new AffineTransform();

        // specify rotation amount
        transform.rotate(Math.toRadians((int) getRotation()), getX() + currentCar.getWidth() / 2, getY() + getImageHeight() / 2);

        // rotate rectangle
        playerCollisions = transform.createTransformedShape(playerCollisions);

        // do stuff if there is a collision
        if (playerCollisions.intersects(otherHitbox) && !(other instanceof Powerup)) {

            // decrease health on obstacle collision
            if (other instanceof Obstacle && health > 0) {
                if (!nitroActive) {
                    if (shieldActive) {
                        toggleShieldStatus();
                    } else {
                        health -= 5;
                        game.totalHealthLost += 5;
                    }
                }
            }

            // decrease health on shell collision
            if (other instanceof TankShot && health > 0) {
                if (!nitroActive) {
                    if (shieldActive) {
                        toggleShieldStatus();
                    } else {
                        health -= 10;
                        game.totalHealthLost += 10;
                    }
                }
            }

            if (!(other instanceof Rocket)) {
                // remove what the car collided with
                game.removeEntity(other);
            }

            // game over if health is now 0 or less
            if (health <= 0) {
                game.gameOver();
            }
        }

        // add a power up to inventory on power up collision
        // power up pickups do not check rotated hit box as they are too small
        // to drive the car at well
        if (other instanceof Powerup) {
            switch (((Powerup) other).getType()) {
                case PowerupType.NITRO:
                    if (numNitro <= storageStat) {
                        numNitro++;
                    }
                    break;
                case PowerupType.SHIELD:
                    if (numShield <= storageStat) {
                        numShield++;
                        if (!shieldActive) {
                            toggleShieldStatus();
                            numShield--;
                        }
                    }
                    break;
                case PowerupType.ROCKET:
                    if (numRocket <= storageStat) {
                        numRocket++;
                    }
                    break;
                case PowerupType.FUEL:
                    fuel += 10;
                    break;
                case PowerupType.HEALTH:
                    health += 20;
                    break;
                default:
                    break;
            }

            game.removeEntity(other);
        }
    }

    public void updateStats() {

        // Set a new sprite if the current one doesn't match the car type
        if (!currentCar.getSprites().contains(spritePath)) {
            System.out.println(spritePath);
            setSprite(currentCar.getRandomSprite());
        }

        storageStat = currentCar.getStorage();
        speedStat = currentCar.getSpeed();
        healthStat = currentCar.getHealth();

        // set maxSpeed
        if (nitroActive) {
            maxSpeed = (speedStat * 100) * 2;
        } else {
            maxSpeed = speedStat * 100;
        }

        // remove extra power ups
        if (numNitro > storageStat) {
            numNitro = storageStat;
        }
        if (numRocket > storageStat) {
            numRocket = storageStat;
        }
        if (numShield > storageStat) {
            numShield = storageStat;
        }

        // reduce health if over max
        if (health > healthStat * 25) {
            health = healthStat * 25;
        }

        // reduce fuel if over max
        if (fuel > maxFuel) {
            fuel = maxFuel;
        }

        // update shielded status if car not shielded and has shields
        if (numShield > 0 && !getShieldActive()) {
            numShield--;
            toggleShieldStatus();
        }
    }

    public int getNumRocket() {
        return numRocket;
    }

    public void useRocket() {
        if (numRocket > 0) {
            Rocket rocket = new Rocket(game, (int) (x + getImageWidth() / 2f), (int) (y + getImageHeight() / 2f), rotation);
            game.projectileEntities.add(rocket);
            numRocket--;
        }
    }

    public int getNumNitro() {
        return numNitro;
    } // getNumNitro

    public boolean useNitro() {
        if (usedNitroFrames == 0) {
            nitroActive = true;
            numNitro--;
        }
        usedNitroFrames++;

        System.out.println(usedNitroFrames);

        if (usedNitroFrames > 120) {
            System.out.println("nitro finished");
            nitroActive = false;
            usedNitroFrames = 0;
            maxSpeed = maxSpeedDefault;
            return true;
        }

        return false;
    }

    public int getNumShield() {
        return numShield;
    }

    public void toggleShieldStatus() {
        shieldActive = !shieldActive;
    }

    public boolean getShieldActive() {
        return shieldActive;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double d) {
        this.rotation = d;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public int getHealth() {
        return health;
    }

    public int getFuel() {
        return fuel;
    }

    public void useFuel() {
        if (fuel <= 0) {
            game.gameOver();
        } else {
            fuel -= 1;
        }
    }

    public void swap(ComCar newCar) {
        x = newCar.x;
        y = newCar.y;
        currentCar = newCar.type;
        game.removeEntity(newCar);
        health = 50;
        fuel = 100;
        numNitro = 0;
        numShield = 0;
        numRocket = 0;
        nitroActive = false;

        if (shieldActive) {
            shieldActive = false;
        }
    }
}
