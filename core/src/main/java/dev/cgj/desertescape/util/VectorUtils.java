package dev.cgj.desertescape.util;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class VectorUtils {

  /**
   * Calculates the angle in radians between two 2D vectors. The angle is measured counter-clockwise
   * from the first vector to the second.
   * <ul>
   *   <li>Positive result: b is counter-clockwise from a.</li>
   *   <li>Negative result: b is clockwise from a</li>
   * </ul>
   *
   * @param a The first vector.
   * @param b The second vector.
   * @return The angle in radians between the two vectors.
   */
  public static float angleBetween(Vector2 a, Vector2 b) {
    return MathUtils.atan2(a.crs(b), a.dot(b));
  }
}
