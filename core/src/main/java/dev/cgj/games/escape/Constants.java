package dev.cgj.games.escape;

import com.badlogic.gdx.math.Vector2;

public class Constants {
  public static final float OFF_ROAD_PENALTY = 0.75f;

  /**
   * The size in world units of each sprite pixel. This is *not* the size of a pixel on the
   * user's screen, which is expected to vary.
   */
  public static final float SPRITE_TO_WORLD = 0.1f;
  public static final float TIME_STEP = 1 / 60f;
  public static final int VELOCITY_ITERATIONS = 6;
  public static final int POSITION_ITERATIONS = 2;

  public static Vector2 spriteToWorld(Vector2 pixel) {
    return pixel.scl(SPRITE_TO_WORLD);
  }

  public static Vector2 spriteToWorld(float x, float y) {
    return new Vector2(x, y).scl(SPRITE_TO_WORLD);
  }
}
