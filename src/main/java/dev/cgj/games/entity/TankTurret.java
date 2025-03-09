package dev.cgj.games.entity;

import dev.cgj.games.CarType;
import dev.cgj.games.EscapeGame;

public class TankTurret extends Tank {
    private double rotation = 0;
    Car player;
    EscapeGame game;

    public TankTurret(EscapeGame g, String r, int newX, int newY, Car player) {
        super(g, r, newX, newY);
        this.player = player;
        game = g;
    }

    /**
     * Aim turret at player.
     */
    public boolean aimTurret() {

        // Degrees to refine aim according to the player's current car
        int aimTweak;
        if (player.currentCar.equals(CarType.TRUCK)) {
            aimTweak = 390;
        } else {
            aimTweak = 320;
        }

        // calculate what rotation will make the turret point at the player's car
        double targetRotation = Math.toDegrees(Math.atan2(
                ((player.getX() - player.getImageWidth() / 2) + aimTweak) - y,
                player.getY() - x));

        // rotate the turret towards the player
        if (targetRotation < rotation) {
            rotation -= 0.7;
        } else if (targetRotation > rotation) {
            rotation += 0.7;
        }

        // is the turret aiming at the player?
        if (targetRotation < rotation + 2 && targetRotation > rotation - 2) {
            targetRotation = rotation;
            return true;
        }

        // turret is not aiming at the player
        return false;
    }

    // get the current turret rotation in degrees
    public double getRotation() {
        return rotation;
    }

	// if try to fire at the player
    public void fire() {

        // if aiming at the car, and car is slow enough
        if (aimTurret()) {

            // add a new projectile to arrayList (the tank shoots)
            game.projectileEntities.add(new TankShot(game, "/sprites/shot.jpg",
                    (int) (x + getImageWidth() / 2),
                    (int) (y + getImageHeight() / 2), rotation));
        }
    }

    // update tank turret y position and aim at player
    public void update() {

        // move tank upwards if too low on screen
        if (y > 659) {
            y--;
        }

        // rotate tank turret to face the player's car
        aimTurret();
    }
}
