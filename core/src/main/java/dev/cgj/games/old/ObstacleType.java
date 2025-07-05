package dev.cgj.games.old;

public enum ObstacleType {
    CACTUS("/sprites/cactus.png"),
    SKULL("/sprites/skull.png"),
    CONE("/sprites/cone.png"),
    OIL("/sprites/oil.png");

    private final String spritePath;

    ObstacleType(String spritePath) {
        this.spritePath = spritePath;
    }

    public String getSpritePath() {
        return spritePath;
    }
}
