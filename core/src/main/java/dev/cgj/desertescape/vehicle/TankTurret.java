package dev.cgj.desertescape.vehicle;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;
import dev.cgj.desertescape.physics.BodyUtils;
import dev.cgj.desertescape.util.SpriteUtils;
import dev.cgj.desertescape.util.VectorUtils;

public class TankTurret implements Disposable {

  /**
   * The tank that this turret is mounted on.
   */
  private final Tank tank;

  /**
   * Tank turret sprite.
   */
  private final Sprite sprite;

  /**
   * Angle in radians of the tank's turret relative to the world space angle of the tank's body.
   */
  private float angle;

  public TankTurret(Tank tank) {
    this.tank = tank;
    sprite = SpriteUtils.getScaledSprite("sprites/enemies/turret.png");
    angle = MathUtils.HALF_PI / 2f;
  }

  public void update(float delta, Vector2 targetPosition) {
    faceTarget(targetPosition);
  }

  public void faceTarget(Vector2 targetPosition) {
    Vector2 targetHeading = targetPosition.cpy().sub(tank.getBody().getPosition()).nor();
    Vector2 currentHeading = BodyUtils.getForwardNormal(tank.getBody());
    angle = -VectorUtils.angleBetween(targetHeading, currentHeading);
  }

  public void draw(SpriteBatch batch) {
    Body body = tank.getBody();
    sprite.setCenter(body.getPosition().x, body.getPosition().y);
    sprite.setRotation((body.getAngle() + angle) * MathUtils.radiansToDegrees);
    sprite.draw(batch);
  }

  @Override
  public void dispose() {
    sprite.getTexture().dispose();
  }
}
