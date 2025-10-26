package dev.cgj.desertescape.npc;

import com.badlogic.gdx.math.Vector2;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Manages a sequence of Vector2 waypoints and provides the next target based on a caller-provided
 * current position.
 */
public class WaypointList {

  private final List<Vector2> waypoints = new LinkedList<>();
  private final float reachThreshold;

  public WaypointList(float reachThreshold) {
    this.reachThreshold = reachThreshold;
  }

  public void addWaypoints(List<Vector2> points) {
    this.waypoints.addAll(points);
  }

  public void clear() {
    this.waypoints.clear();
  }

  /**
   * Returns the next waypoint to navigate to, removing already-reached points
   * based on the provided current position.
   *
   * @param position A position to check against the current waypoint to see if it has been reached.
   * @return The next waypoint
   */
  public Optional<Vector2> getNext(Vector2 position) {
    while (!waypoints.isEmpty()) {
      Vector2 target = waypoints.getFirst();
      if (position.dst2(target) <= reachThreshold * reachThreshold) {
        waypoints.removeFirst();
      } else {
        return Optional.of(target);
      }
    }
    return Optional.empty();
  }
}
