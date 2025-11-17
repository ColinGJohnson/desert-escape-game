package dev.cgj.desertescape.npc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import dev.cgj.desertescape.Player;
import dev.cgj.desertescape.npc.pathfinding.FollowPlayerPath;
import dev.cgj.desertescape.npc.pathfinding.NavigationStrategy;
import dev.cgj.desertescape.screens.GameScreen;

import java.util.List;

public class NpcManager implements Disposable {
  private final GameScreen gameScreen;
  private final List<Npc> npcs;

  public NpcManager(GameScreen gameScreen, World world) {
    this.gameScreen = gameScreen;
    NavigationStrategy navigationStrategy = new FollowPlayerPath(gameScreen.getPlayer());
    npcs = List.of(
      // new NpcCar(world, CarType.VAN, navigationStrategy),
      // new NpcCar(world, CarType.SPORTS, navigationStrategy),
      // new NpcCar(world, CarType.SEDAN, navigationStrategy),
      // new NpcCar(world, CarType.TRUCK, navigationStrategy),
      new NpcTank(world, navigationStrategy)
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
