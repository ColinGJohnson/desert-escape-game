package dev.cgj.desertescape.vehicle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class NpcCar implements Disposable {

  /**
   * Threshold within which a waypoint is considered to have been reached by the NPC.
   */
  public static final float REACH_THRESHOLD = 1.0f;

  /**
   * The car controlled by this NPC.
   */
  private final Car car;

  /**
   * Waypoints for this NPC to sequentially navigate to.
   */
  private final List<Vector2> waypoints = new LinkedList<>();

  public NpcCar(World world, CarType carType) {
    this.car = new Car(carType, world);
  }

  public void update(float delta) {
    car.update(delta);
    Optional<Vector2> waypoint = getNextWaypoint();
    if (waypoint.isPresent()) {
      updateSteering(delta, waypoint.get());
    } else {
      car.brake(1f);
    }
  }

  public void draw(SpriteBatch batch) {
    car.draw(batch);
  }

  public void addWaypoints(List<Vector2> waypoints) {
    this.waypoints.addAll(waypoints);
  }

  public void clearWaypoints() {
    this.waypoints.clear();
  }

  /**
   * Steers the car towards a waypoint.
   */
  protected void updateSteering(float delta, Vector2 waypoint) {

    // Normalized vector from car to waypoint
    Vector2 carPos = car.getPosition();
    Vector2 targetDir = waypoint.cpy().sub(carPos).nor();

    // Current forward direction of the car
    float heading = car.body.carBody.getAngle() + (float) Math.PI / 2;

    // Compute signed smallest angle difference between forward and target
    float targetAngle = MathUtils.atan2(targetDir.y, targetDir.x);
    float angleDiff = MathUtils.atan2(MathUtils.sin(targetAngle - heading), MathUtils.cos(targetAngle - heading));
    float absDiff = Math.abs(angleDiff);

    // Determine steering input: left (-1) or right (1) proportional to angle
    float steerInput = 0f;
    if (absDiff > 1e-3f) {
      steerInput = MathUtils.clamp(angleDiff / (float) Math.PI, -1f, 1f);
    }

    // When facing in the opposite direction, reverse and steer towards the target to flip around.
    if (absDiff > MathUtils.degreesToRadians * 170f) {
      car.steer(delta, Math.signum(steerInput));
      if (car.body.getForwardVelocity() > 0) {
        car.brake(1f);
      } else {
        car.accelerate(-0.8f);
      }
      return;
    }

    // Normal steering to minimize angleDiff
    car.steer(delta, steerInput);

    // Increase acceleration when the NPC car is pointed directly towards the target
    float diffDeg = absDiff * MathUtils.radiansToDegrees;
    float accel;
    if (diffDeg >= 90f) {
      accel = 0.1f;
    } else {
      accel = 0.3f - (diffDeg / 90f) * (0.3f - 0.1f);
    }

    car.accelerate(accel);
  }

  /**
   * Retrieves the next waypoint that the NPC car should navigate to. If the current waypoint
   * is within the defined reach threshold of the car's position, it is removed, and the
   * next waypoint is considered. If no waypoints remain, an empty Optional is returned.
   *
   * @return An {@link Optional} containing the next waypoint as a {@link Vector2} if available,
   * or an empty Optional if all waypoints have been reached or the list is empty.
   */
  private Optional<Vector2> getNextWaypoint() {
    Vector2 currentPosition = car.getPosition();

    while (!waypoints.isEmpty()) {
      Vector2 target = waypoints.getFirst();
      if (currentPosition.dst2(target) <= REACH_THRESHOLD * REACH_THRESHOLD) {
        waypoints.removeFirst();
      } else {
        return Optional.of(target);
      }
    }

    return Optional.empty();
  }

  @Override
  public void dispose() {
    car.dispose();
  }
}
