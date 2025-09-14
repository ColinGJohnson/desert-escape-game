package dev.cgj.desertescape.terrain;

import com.badlogic.gdx.math.Vector2;
import dev.cgj.desertescape.Constants;

public class SpawnZone {
  private final Vector2 min;
  private final Vector2 max;

  public SpawnZone(Vector2 min, Vector2 max) {
    this.min = min;
    this.max = max;
  }

  public Vector2 randomPosition() {
    return Constants.roundWorldToNearestPixel(randomPosition(min, max));
  }

  /**
   * Generates a random position within specified bounds.
   *
   * @param min Minimum x,y coordinates (bottom left)
   * @param max Maximum x,y coordinates (top right)
   * @return Random position vector
   */
  private Vector2 randomPosition(Vector2 min, Vector2 max) {
    float x = min.x + (float) Math.random() * (max.x - min.x);
    float y = min.y + (float) Math.random() * (max.y - min.y);
    return new Vector2(x, y);
  }
}
