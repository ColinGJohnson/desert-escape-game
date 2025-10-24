package dev.cgj.desertescape.util;

public class LaggedFloat {

  /**
   * Constant rate at which the value moves towards the target.
   */
  private final float rate;

  /**
   * The value that linearly approaches the {@link #target} over time.
   */
  private float value;

  /**
   * The number that {@link #value} approaches.
   */
  private float target;

  public LaggedFloat(float initialValue, float rate) {
    this.value = initialValue;
    this.rate = rate;
  }

  public void setTarget(float target) {
    this.target = target;
  }

  public void update(float delta) {
    if (value < target) {
      value = Math.min(value + delta * rate, target);
    } else if (value > target) {
      value = Math.max(value - delta * rate, target);
    }
  }

  public float getValue() {
    return value;
  }
}
