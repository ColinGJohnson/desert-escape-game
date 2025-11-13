package dev.cgj.desertescape.npc;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Optional;
import java.util.Queue;

/**
 * Manages a sequence of Vector2 waypoints and provides the next target based on a caller-provided
 * current position.
 */
public class WaypointQueue {

  private final Queue<Vector2> waypoints = new ArrayDeque<>();
  private final float reachThreshold;

  public WaypointQueue(float reachThreshold) {
    this.reachThreshold = reachThreshold;
  }

  public void addWaypoints(Collection<Vector2> points) {
    this.waypoints.addAll(points);
  }

  public void addWaypoint(Vector2 point) {
    this.waypoints.offer(point);
  }

  public void clear() {
    this.waypoints.clear();
  }

  public boolean isEmpty() {
    return waypoints.isEmpty();
  }

  public int size() {
    return waypoints.size();
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
      Vector2 target = waypoints.peek();
      if (position.dst2(target) <= reachThreshold * reachThreshold) {
        waypoints.poll();
      } else {
        return Optional.of(target);
      }
    }
    return Optional.empty();
  }
}
