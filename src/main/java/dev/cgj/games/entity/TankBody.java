package dev.cgj.games.entity;

import dev.cgj.games.EscapeGame;

/**
 * Specialized Entity for the body of enemy tank.
 */
public class TankBody extends Tank {

    public TankBody(EscapeGame g, String r, int newX, int newY) {
        super(g, r, newX, newY);
    }

    /**
     * move tank upwards if too low on screen and animate the tank's treads.
     */
    public void update() {
        if (y > 680) {
            y--;
        }

        updateTankAnimations();
    }
}
