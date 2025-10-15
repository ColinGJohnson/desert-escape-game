package dev.cgj.desertescape;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import dev.cgj.desertescape.vehicle.CarType;
import dev.cgj.desertescape.vehicle.Npc;
import dev.cgj.desertescape.vehicle.NpcCar;
import dev.cgj.desertescape.vehicle.NpcTank;

import java.util.List;

public class NpcManager implements Disposable {
  private final List<Npc> npcs;

  public NpcManager(World world) {
    npcs = List.of(
      new NpcCar(world, CarType.VAN),
      new NpcTank(world)
    );
  }

  public void update(float delta, Player player) {
    for (Npc npc : npcs) {
      npc.update(delta, player);
    }
  }

  public void draw(SpriteBatch batch) {
    for (Npc npc : npcs) {
      npc.draw(batch);
    }
  }

  @Override
  public void dispose() {
    npcs.forEach(Disposable::dispose);
  }
}
