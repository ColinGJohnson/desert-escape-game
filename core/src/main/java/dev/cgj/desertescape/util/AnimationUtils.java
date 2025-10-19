package dev.cgj.desertescape.util;

public class AnimationUtils {
  /**
   * Linearly interpolates between two values.
   *
   * @param start The starting value
   * @param end   The ending value
   * @param t     The interpolation factor in the range [0,1]
   * @return The interpolated value
   */
  public static float lerp(float start, float end, float t) {
    return start + (end - start) * t;
  }
}
