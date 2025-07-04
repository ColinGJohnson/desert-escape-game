package dev.cgj.games.entity;

import dev.cgj.games.EscapeGame;

public class Tank extends Entity {

    private long tankSwitch = 0;
    private double moveSpeed = 300; // vertical speed shot moves
    EscapeGame game;

    public Tank(EscapeGame g, String r, int newX, int newY) {
        super(r, newX, newY); // calls the constructor in Entity
        dy = moveSpeed;
        game = g;
    }

    public void move(long delta) {
        super.move(delta); // calls the move method in Entity
    } // move

    @Override
    public void collidedWith(Entity other) {
        // no-op
    }

    // animate tank sprite
    public void updateTankAnimations() {
        if (game.gameDY < 0) {
            if ((System.currentTimeMillis() - 100 / Math.abs(game.gameDY)) > tankSwitch) {
                switch (getCurrentSprite()) {
                    case 0:
                        setSprite("/sprites/body4.png");
                        setCurrentSprite(1);
                        break;
                    case 1:
                        setSprite("/sprites/body3.png");
                        setCurrentSprite(2);
                        break;
                    case 2:
                        setSprite("/sprites/body2.png");
                        setCurrentSprite(3);
                        break;
                    case 3:
                        setSprite("/sprites/body1.png");
                        setCurrentSprite(0);
                        break;
                    default:
                        break;
                }
                tankSwitch = System.currentTimeMillis();
            }
        }
    }
}
