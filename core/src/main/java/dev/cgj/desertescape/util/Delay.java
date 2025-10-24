package dev.cgj.desertescape.util;


public class Delay {
  private final float delay;
  private float elapsed;

  public Delay(float delay) {
    this.delay = delay;
  }

  public void update(float delta) {
    elapsed += delta;
  }

  /**
   * Executes the specified action if the elapsed time has reached or exceeded the defined delay.
   * Resets the elapsed time to zero after the action is executed.
   *
   * @param action The action to be executed if the delay condition is met.
   */
  public void tryRun(Runnable action) {
    if (elapsed >= delay) {
      action.run();
      elapsed = 0;
    }
  }
}
