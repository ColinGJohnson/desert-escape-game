package dev.cgj.desertescape;

public class ScoreBoard {
  private int score;
  private int maxDistance;

  public int getScore() {
    return score;
  }

  public void updateDistance(float distance) {
    score += Math.max(0, (int) distance - maxDistance);
    maxDistance = Math.max(maxDistance, (int) distance);
  }
}
