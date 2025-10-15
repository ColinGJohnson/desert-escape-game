package dev.cgj.desertescape.vehicle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import dev.cgj.desertescape.Player;

public class NpcTank implements Npc {

  private final Tank tank;

  public NpcTank(World world) {
    this.tank = new Tank(world);
  }

  @Override
  public void update(float delta, Player player) {
    tank.update(delta);
  }

  public void draw(SpriteBatch batch) {
    tank.draw(batch);
  }

  @Override
  public void dispose() {
    tank.dispose();
  }
}
