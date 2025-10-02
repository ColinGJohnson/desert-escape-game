package dev.cgj.desertescape.vehicle;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.LinkedList;
import java.util.List;

public class NpcCar {
  // world units
  public static final float REACH_THRESHOLD = 1.0f;
  private final Car car;
  private final List<Vector2> waypoints = new LinkedList<>();

  public NpcCar(World world, CarType carType) {
    this.car = new Car(carType, world);
  }

  public void update(float delta) {
    car.update(delta);
    updateSteering(delta);
  }

  public void addWaypoints(List<Vector2> waypoints) {
    this.waypoints.addAll(waypoints);
  }

  public void clearWaypoints() {
    this.waypoints.clear();
  }

  /**
   * Steers the car towards the next waypoint.
   */
  private void updateSteering(float delta) {
    if (car == null || waypoints == null || waypoints.isEmpty()) {
      return;
    }

    // Get the current target waypoint
    Vector2 target = waypoints.getFirst();
    Vector2 pos = car.body.carBody.getPosition();

    // Advance to the next waypoint if close enough
    if (pos.dst2(target) <= REACH_THRESHOLD * REACH_THRESHOLD) {
      waypoints.removeFirst();
      if (waypoints.isEmpty()) return;
      target = waypoints.getFirst();
    }

    // Desired angle in world space (relative to +X axis)
    float desired = MathUtils.atan2(target.y - pos.y, target.x - pos.x);
    float current = car.body.carBody.getAngle();

    // Smallest signed angular difference [-PI, PI]
    float error = desired - current;
    while (error > MathUtils.PI) error -= MathUtils.PI2;
    while (error < -MathUtils.PI) error += MathUtils.PI2;

    // Map angular error to steering input in [-1, 1]
    float maxSteerRad = (float) Math.toRadians(car.type.maxSteerAngleDeg);
    if (maxSteerRad <= 0) return;
    float steerInput = MathUtils.clamp(error / maxSteerRad, -1f, 1f);

    // Use Car API to steer
    car.steer(delta, steerInput);

    // Simple driving logic: accelerate if the path ahead is clear, otherwise brake
    Vector2 forward = new Vector2(MathUtils.cos(current), MathUtils.sin(current));
    Vector2 toTarget = new Vector2(target).sub(pos).nor();
    float alignment = forward.dot(toTarget); // 1 when directly ahead, -1 behind
    float absError = Math.abs(error);
    boolean ahead = alignment > 0.5f; // roughly within 60 degrees in front
    boolean smallAngle = absError < MathUtils.degreesToRadians * 25f; // ~25 degrees

    if (ahead && smallAngle) {
      car.accelerate(1f);
    } else {
      car.brake(1f);
    }
  }
}
