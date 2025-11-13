package dev.cgj.desertescape.npc.pathfinding;

import com.badlogic.gdx.math.Vector2;
import dev.cgj.desertescape.npc.WaypointQueue;

public interface NavigationStrategy {
  void updateWaypoints(WaypointQueue waypoints, Vector2 position);
}
