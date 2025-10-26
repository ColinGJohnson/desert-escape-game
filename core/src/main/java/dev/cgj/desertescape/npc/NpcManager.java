package dev.cgj.desertescape.npc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import dev.cgj.desertescape.Player;
import dev.cgj.desertescape.entity.CarType;

import java.util.List;

public class NpcManager implements Disposable {
  private final List<Npc> npcs;

  public NpcManager(World world) {
    npcs = List.of(
      new NpcCar(world, CarType.VAN),
      new NpcCar(world, CarType.SPORTS),
      new NpcCar(world, CarType.SEDAN),
      new NpcCar(world, CarType.TRUCK),
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
