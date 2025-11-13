package dev.cgj.desertescape.npc.pathfinding;

import com.badlogic.gdx.math.Vector2;
import dev.cgj.desertescape.Player;
import dev.cgj.desertescape.npc.WaypointQueue;

import java.util.Collections;

public class FollowPlayerPath implements NavigationStrategy {
  private final Player player;

  public FollowPlayerPath(Player player) {
    this.player = player;
  }

  @Override
  public void updateWaypoints(WaypointQueue waypoints, Vector2 position) {
    Vector2 playerPosition = player.car().carBody.getPosition().cpy();
    waypoints.clear();
    waypoints.addWaypoints(Collections.singletonList(playerPosition));
  }
}
