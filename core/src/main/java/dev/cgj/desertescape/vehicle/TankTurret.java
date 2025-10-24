package dev.cgj.desertescape.vehicle;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;
import dev.cgj.desertescape.entity.Entity;
import dev.cgj.desertescape.physics.BodyUtils;
import dev.cgj.desertescape.util.Delay;
import dev.cgj.desertescape.util.LaggedFloat;
import dev.cgj.desertescape.util.SpriteUtils;
import dev.cgj.desertescape.util.VectorUtils;

import java.util.ArrayList;
import java.util.List;

import static dev.cgj.desertescape.Constants.SPRITE_TO_WORLD;

public class TankTurret implements Disposable {
  private static final float TURRET_TURN_SPEED = 1f;
  private static final float MIN_SHOT_INTERVAL_MS = 1;

  /**
   * The tank that this turret is attached to.
   */
  private final Tank tank;

  /**
   * Tank turret sprite.
   */
  private final Sprite sprite;

  /**
   * Angle in radians of the tank's turret relative to the X-axis of the tank's body.
   */
  private final LaggedFloat angle = new LaggedFloat(0, TURRET_TURN_SPEED);

  private final Delay delay = new Delay(MIN_SHOT_INTERVAL_MS);

  private final List<TankShell> shells = new ArrayList<>();

  public TankTurret(Tank tank) {
    this.tank = tank;
    sprite = SpriteUtils.getScaledSprite("sprites/enemies/turret.png");
  }

  public void update(float delta, Vector2 targetPosition) {
    angle.setTarget(getTargetAngle(targetPosition));
    angle.update(delta);

    delay.update(delta);
    delay.tryRun(this::shoot);

    for (TankShell shell : shells) {
      shell.update(delta);
    }

    Entity.removeDestroyed(shells);
  }

  public void shoot() {
    Body body = tank.getBody();
    Vector2 direction = new Vector2(0, 1).setAngleRad(getWorldAngle() + MathUtils.PI / 2f);
    Vector2 position = body.getPosition().add(direction.cpy().scl(16f * SPRITE_TO_WORLD));
    Vector2 velocity = direction.scl(TankShell.SPEED).add(body.getLinearVelocity());
    shells.add(new TankShell(body.getWorld(), position, velocity));
  }

  public float getTargetAngle(Vector2 targetPosition) {
    Vector2 targetHeading = targetPosition.cpy().sub(tank.getBody().getPosition()).nor();
    Vector2 currentHeading = BodyUtils.getForwardNormal(tank.getBody());
    return VectorUtils.angleBetween(targetHeading, currentHeading);
  }

  public void draw(SpriteBatch batch) {
    Body body = tank.getBody();
    sprite.setCenter(body.getPosition().x, body.getPosition().y);
    sprite.setRotation(getWorldAngle() * MathUtils.radiansToDegrees);
    sprite.draw(batch);
    for (TankShell shell : shells) {
      shell.draw(batch);
    }
  }

  /**
   * The direction pointed to by the tank's turret, relative to the world's X axis.
   *
   * @return An angle in radians.
   */
  private float getWorldAngle() {
    return tank.getBody().getAngle() - angle.getValue();
  }

  @Override
  public void dispose() {
    sprite.getTexture().dispose();
    for (TankShell shell : shells) {
      shell.dispose();
    }
  }
}
