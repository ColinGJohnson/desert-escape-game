package dev.cgj.games.escape;

public class Constants {
    public static final float OFF_ROAD_PENALTY = 0.75f;

    /**
     * The size in world units of each sprite pixel. This is *not* the size of a pixel on the
     * user's screen, which is expected to vary.
     */
    public static final float PIXEL_TO_WORLD = 0.1f;

    public static final float TIME_STEP = 1 / 60f;
    public static final int VELOCITY_ITERATIONS = 6;
    public static final int POSITION_ITERATIONS = 2;
}
