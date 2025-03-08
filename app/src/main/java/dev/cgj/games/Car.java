package dev.cgj.games;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

public class Car extends Entity {

    /**
     * the game in which the ship exists.
     */
    private final EscapeGame game;

    private int health = 100;
    private int fuel = 100;
    private int maxFuel = 100;
    private int numRocket = 0;
    private int numNitro = 10;
    private int numShield = 0;

    public int currentCar = 2;
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
        } // else

        // set x and y velocities based on car angle
        dx = -speed * Math.cos(Math.toRadians(rotation + 90));
        dy = -speed * Math.sin(Math.toRadians(rotation + 90));

        // update location of entity based on move speeds
        if (offRoad) {
            x += (delta * (dx)) / 1000;
        } else {
            x += (delta * (dx)) / 1000;
        }

        // move car to correct y position if too high/low
        if (y > 502 && y < 502) {
            y = 500;
        }

        if (y > 502) {
            y--;
        } else if (y < 488) {
            y++;
        }
    } // move

    // checks if the car's refined and rotated collision box has collided with
    // "other" (another entity)
    public void collidedWith(Entity other) {

        // creating the rectangle you want to rotate
        Shape playerCollisions = new Rectangle(getX() + getVehicleBorder(), getY(), getVehicleWidth(), getImageHeight());

        // creating other rectangle to check intersection
        Rectangle otherHitbox = new Rectangle(other.getX(), other.getY(), other.getImageWidth(), other.getImageHeight());

        // Affine transform Object for hit box rotations
        AffineTransform transform = new AffineTransform();

        // specify rotation amount
        transform.rotate(Math.toRadians((int) getRotation()), getX() + getVehicleWidth() / 2, getY() + getImageHeight() / 2);

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
            switch (((Powerup) other).type) {
                case "nitro":
                    if (numNitro <= storageStat) {
                        numNitro++;
                    }
                    break;
                case "shield":
                    if (numShield <= storageStat) {
                        numShield++;
                        if (!shieldActive) {
                            toggleShieldStatus();
                            numShield--;
                        }
                    }
                    break;
                case "rocket":
                    if (numRocket <= storageStat) {
                        numRocket++;
                    }
                    break;

                case "fuel":
                    fuel += 10;
                    break;
                case "health":
                    health += 20;
                    break;

                default:
                    break;
            }

            game.removeEntity(other);
        }
    } // collidedWith

    public void updateStats() {

        // set correct sprite
        switch (currentCar) {
            case 0:
                setSprite("/sprites/sc1.png");
                break;
            case 1:
                setSprite("/sprites/sc2.png");
                break;
            case 2:
                setSprite("/sprites/sc3.png");
                break;
            case 3:
                setSprite("/sprites/fc1.png");
                break;
            case 4:
                setSprite("/sprites/fc2.png");
                break;
            case 5:
                setSprite("/sprites/fc3.png");
                break;
            case 6:
                setSprite("/sprites/tr1.png");
                break;
            case 7:
                setSprite("/sprites/tr2.png");
                break;
            case 8:
                setSprite("/sprites/tr3.png");
                break;
            case 9:
                setSprite("/sprites/va1.png");
                break;
            default:
                System.out.println("Error selecting car");
                currentCar = 0;
                break;
        }

        // correct car stats
        if (currentCar == 0 || currentCar == 1 || currentCar == 2) {

            // sports car stats
            storageStat = 2;
            speedStat = 8;
            healthStat = 1;
        } else if (currentCar == 3 || currentCar == 4 || currentCar == 5) {

            // regular car stats
            storageStat = 3;
            speedStat = 6;
            healthStat = 2;
        } else if (currentCar == 6 || currentCar == 7 || currentCar == 8) {

            // truck stats
            storageStat = 10;
            speedStat = 4;
            healthStat = 6;
        } else if (currentCar == 9) {

            // van stats
            storageStat = 5;
            speedStat = 5;
            healthStat = 3;
        } // else if

        // set maxSpeed
        if (nitroActive) {
            maxSpeed = (speedStat * 100) * 2;
        } else {
            maxSpeed = speedStat * 100;
        } // else

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
    } // updateStats

    public int getNumRocket() {
        return numRocket;
    } // getNumRocket

    public void useRocket() {

        if (numRocket > 0) {
            game.projectileEntities.add(new Rocket(game, "/sprites/rocket.png", (int) (x + getImageWidth() / 2), (int) (y + getImageHeight() / 2), rotation));

            numRocket--;
        }
    } // useRocket

    public int getNumNitro() {
        return numNitro;
    } // getNumNitro

    public void setNumNitro(int numNitro) {
        this.numNitro = numNitro;
    } // setNumNitro

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
    } // useNitro

    public int getNumShield() {
        return numShield;
    } // getNumShield

    public void toggleShieldStatus() {
        shieldActive = !shieldActive;
    } // toggleShieldStatus

    public boolean getShieldActive() {
        return shieldActive;
    } // getShieldActive

    public int getCarStats(String stat) {
        if (stat.equals("health")) {
            return healthStat;
        } else if (stat.equals("speed")) {
            return speedStat;
        } else if (stat.equals("storage")) {
            return storageStat;
        }

        return 0;
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

    /**
     * returns the width of the current vehicle for use in collisions
     */
    public int getVehicleWidth() {
        if ((currentCar >= 0 && currentCar <= 5) || currentCar == 9) {
            return 24;
        } else {
            return 34;
        }
    }

    /**
     * returns the width of the blank space in car image for use in collisions
     */
    public int getVehicleBorder() {
        if ((currentCar >= 0 && currentCar <= 5) || currentCar == 9) {
            return 18;
        } else {
            return 52;
        }
    }

    public void swap(ComCar newCar) {
        double newx = newCar.x;
        double newy = newCar.y;
        x = newx;
        y = newy;
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