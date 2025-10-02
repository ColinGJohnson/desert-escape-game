package dev.cgj.desertescape;

public class Inventory {
  public static int MAX_ITEMS = 5;

  private int rockets = 0;
  private int nitro = 0;
  private int shield = 0;

  public int getRockets() {
    return rockets;
  }

  public int getNitro() {
    return nitro;
  }

  public int getShield() {
    return shield;
  }

  public void addShields(int i) {
    shield = Math.min(shield + i, MAX_ITEMS);
  }

  public void addRockets(int i) {
    rockets = Math.min(rockets + i, MAX_ITEMS);
  }

  public void addNitro(int i) {
    nitro = Math.min(rockets + i, MAX_ITEMS);
  }

  public void removeRockets(int i) {
    rockets = Math.max(rockets - i, 0);
  }

  public void removeShields(int i) {
    shield = Math.max(shield - i, 0);
  }

  public void removeNitro(int i) {
    nitro = Math.max(rockets - i, 0);
  }
}
