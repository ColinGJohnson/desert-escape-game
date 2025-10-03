package dev.cgj.desertescape;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import dev.cgj.desertescape.vehicle.CarType;
import dev.cgj.desertescape.vehicle.NpcCar;

import java.util.Collections;
import java.util.List;

public class NpcManager implements Disposable {
  private final List<NpcCar> npcCars;

  public NpcManager(World world) {
    npcCars = Collections.singletonList(new NpcCar(world, CarType.SEDAN));
  }

  public void update(float delta, Player player) {
    for (NpcCar npc : npcCars) {
      npc.clearWaypoints();
      npc.addWaypoints(Collections.singletonList(player.car().body.getPosition().cpy()));
      npc.update(delta);
    }
  }

  public void draw(SpriteBatch batch) {
    for (NpcCar npc : npcCars) {
      npc.draw(batch);
    }
  }

  @Override
  public void dispose() {
    npcCars.forEach(Disposable::dispose);
  }
}
