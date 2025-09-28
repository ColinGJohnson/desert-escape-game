package dev.cgj.desertescape;

import com.badlogic.gdx.math.Vector2;

public class Constants {

  /**
   * How far the player needs to travel in world units to win the game.
   */
  public static final float GOAL_DISTANCE = 100;

  public static final int PIXEL_WIDTH = 480;
  public static final int PIXEL_HEIGHT = 270;

  public static final int WORLD_WIDTH = 48;
  public static final int WORLD_HEIGHT = 27;

  /**
   * The size in world units of each sprite pixel. This is *not* the size of a pixel on the
   * user's screen, which is expected to vary.
   */
  public static final float SPRITE_TO_WORLD = 0.1f;
  public static final float WORLD_TO_SPRITE = 1 / SPRITE_TO_WORLD;

  public static final float TIME_STEP = 1 / 60f;
  public static final int VELOCITY_ITERATIONS = 6;
  public static final int POSITION_ITERATIONS = 2;

  public static Vector2 spriteToWorld(Vector2 pixel) {
    return pixel.scl(SPRITE_TO_WORLD);
  }

  public static Vector2 spriteToWorld(float x, float y) {
    return new Vector2(x, y).scl(SPRITE_TO_WORLD);
  }

  public static Vector2 roundWorldToNearestPixel(Vector2 vector) {
    return new Vector2(roundWorldToNearestPixel(vector.x), roundWorldToNearestPixel(vector.y));
  }

  public static float roundWorldToNearestPixel(float value) {
    return Math.round(value * WORLD_TO_SPRITE) * SPRITE_TO_WORLD;
  }
}
