package dev.cgj.games.old;

import java.util.List;

public enum CarType {
    SPORTS(2, 8, 1, 24, 18,
            List.of("/sprites/sc1.png", "/sprites/sc2.png", "/sprites/sc3.png")),
    FAMILY(3, 6, 2, 24, 18,
            List.of("/sprites/fc1.png", "/sprites/fc2.png", "/sprites/fc3.png")),
    VAN(5, 5, 3, 24, 18,
            List.of("/sprites/va1.png")),
    TRUCK(10, 4, 6, 34, 52,
            List.of("/sprites/tr1.png", "/sprites/tr2.png", "/sprites/tr3.png"));

    final int storage;
    final int speed;
    final int health;

    /**
     * The width of the current vehicle for use in collisions
     */
    final int width;

    /**
     * The width of the blank space in car sprite for use in collisions
     */
    final int border;
    final List<String> sprites;

    CarType(int storage, int speed, int health, int width, int border, List<String> sprites) {
        this.storage = storage;
        this.speed = speed;
        this.health = health;
        this.width = width;
        this.border = border;
        this.sprites = sprites;
    }

    public int getStorage() {
        return storage;
    }

    public int getSpeed() {
        return speed;
    }

    public int getHealth() {
        return health;
    }

    public int getWidth() {
        return width;
    }

    public int getBorder() {
        return border;
    }

    public List<String> getSprites() {
        return sprites;
    }

    public String getRandomSprite() {
        return sprites.get((int) (Math.random() * sprites.size()));
    }

    public static CarType getRandomCarType(){
        return CarType.values()[(int) (Math.random() * CarType.values().length)];
    }
}
