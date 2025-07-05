package dev.cgj.games.old.entity;

import dev.cgj.games.old.CarType;
import dev.cgj.games.old.EscapeGame;

public class ComCar extends Entity {
    private final EscapeGame game;
    public boolean used = false;
    public CarType type;

    public ComCar(EscapeGame g, int newX, int newY, CarType carType) {
        super(carType.getRandomSprite(), newX, newY);
        type = carType;
        game = g;
    }

    @Override
    public void collidedWith(Entity other) {
        if (!used && other instanceof Car) {
            ((Car) other).swap(this);
        }
    }

    public void move(long delta) {
        setY(getY() - game.gameDY - 4);

        // if car moves off bottom of screen, remove it
        if (y > 700) {
            game.removeEntity(this);
        }
    }

    public void setY(double newY) {
        y = (int) newY;
    }
}
