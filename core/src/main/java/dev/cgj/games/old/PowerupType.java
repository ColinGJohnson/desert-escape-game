package dev.cgj.games.old;

public enum PowerupType {
    HEALTH("/sprites/health.png"),
    FUEL("/sprites/fuel.png"),
    NITRO("/sprites/nitro.png"),
    SHIELD("/sprites/shield.png"),
    ROCKET("/sprites/rocket.png");

    private final String spritePath;

    PowerupType(String spritePath) {
        this.spritePath = spritePath;
    }

    public String getSpritePath() {
        return spritePath;
    }
}
