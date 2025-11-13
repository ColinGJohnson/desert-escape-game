package dev.cgj.desertescape.npc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import dev.cgj.desertescape.Player;
import dev.cgj.desertescape.entity.tank.Tank;
import dev.cgj.desertescape.npc.pathfinding.NavigationStrategy;
import dev.cgj.desertescape.physics.BodyUtils;
import dev.cgj.desertescape.physics.UserData;
import dev.cgj.desertescape.util.VectorUtils;

import java.util.List;

public class NpcTank implements Npc {

  /**
   * Threshold within which a waypoint is considered to have been reached by the NPC.
   */
  public static final float REACH_THRESHOLD = 4.0f;

  /**
   * The tank controlled by this NPC.
   */
  private final Tank tank;

  /**
   * Queue of locations this NPC will try to visit.
   */
  private final WaypointQueue waypoints = new WaypointQueue(REACH_THRESHOLD);

  private final NavigationStrategy navigationStrategy;

  public NpcTank(World world, NavigationStrategy navigationStrategy) {
    this.navigationStrategy = navigationStrategy;
    this.tank = new Tank(world);
    tank.setUserData(UserData.dataOnly(this));
  }

  @Override
  public void update(float delta, Player player) {
    Vector2 target = player.car().getPosition();
    tank.update(delta, target);

    navigationStrategy.updateWaypoints(waypoints, tank.getPosition());
    waypoints.getNext(tank.getPosition()).ifPresentOrElse(
      waypoint -> updateSteering(delta, waypoint),
      () -> tank.brake(1f));

    float angleToTarget = tank.getTurret().getTargetAngle(target);
    if (Math.abs(angleToTarget) < MathUtils.HALF_PI) {
      tank.getTurret().shootIfReloaded();
    }
  }

  @Override
  public void draw(SpriteBatch batch) {
    tank.draw(batch);
  }

  public void addWaypoints(List<Vector2> waypoints) {
    this.waypoints.addWaypoints(waypoints);
  }

  public void clearWaypoints() {
    this.waypoints.clear();
  }

  private void updateSteering(float delta, Vector2 waypoint) {
    Vector2 targetHeading = waypoint.cpy().sub(tank.getPosition()).nor();
    Vector2 currentHeading = BodyUtils.getForwardNormal(tank.getBody());
    float angleBetween = VectorUtils.angleBetween(currentHeading, targetHeading);

    // Facing in the wrong direction
    if (Math.abs(angleBetween) > MathUtils.HALF_PI) {
      tank.getLeftTread().accelerate(1);
      tank.getRightTread().accelerate(-1);
      return;
    }

    // Facing in the right direction
    tank.getRightTread().accelerate(1);
    tank.getLeftTread().accelerate(1);
  }

  @Override
  public void dispose() {
    tank.dispose();
  }
}
