package dev.cgj.desertescape.vehicle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import dev.cgj.desertescape.physics.BodyUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class NpcCar implements Disposable {

  /**
   * Threshold within which a waypoint is considered to have been reached by the NPC.
   */
  public static final float REACH_THRESHOLD = 4.0f;
  public static final float MIN_ACCELERATION = 0.3f;
  public static final float MAX_ACCELERATION = 0.5f;

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
   * Controls NPC car steering:
   * 1. If facing away from the target (angle > 90°), backs up and steers to turn around
   * 2. If within 90° of target, accelerates forward with steering proportional to angle
   * 3. Acceleration increases as a car aligns with a target direction
   */
  private void updateSteering(float delta, Vector2 waypoint) {
    Vector2 targetHeading = waypoint.cpy().sub(car.getPosition()).nor();
    Vector2 currentHeading = BodyUtils.getForwardNormal(car.body.carBody);
    float angleBetween = angleBetween(currentHeading, targetHeading);

    // Facing in the wrong direction
    if (Math.abs(angleBetween) > MathUtils.HALF_PI) {
      car.steer(delta, -Math.signum(angleBetween));
      car.accelerate(-1f);
      return;
    }

    // Facing in the right direction
    car.steer(delta, angleBetween / MathUtils.HALF_PI);
    car.accelerate(lerp(MIN_ACCELERATION, MAX_ACCELERATION, 1 - angleBetween / MathUtils.HALF_PI));
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
