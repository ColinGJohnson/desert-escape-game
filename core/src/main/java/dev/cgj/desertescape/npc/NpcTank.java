package dev.cgj.desertescape.npc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import dev.cgj.desertescape.Player;
import dev.cgj.desertescape.entity.tank.Tank;

public class NpcTank implements Npc {

  private final Tank tank;

  public NpcTank(World world) {
    this.tank = new Tank(world);
  }

  @Override
  public void update(float delta, Player player) {
    Vector2 target = player.car().getPosition();
//    tank.setTarget(target);
    tank.update(delta, target);

//    float angleToTarget = tank.getAngleToTarget(target);
//    if (angleToTarget < MathUtils.HALF_PI) {
//      tank.tryShoot();
//    }
  }

  public void draw(SpriteBatch batch) {
    tank.draw(batch);
  }

  @Override
  public void dispose() {
    tank.dispose();
  }
}
