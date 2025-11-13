package dev.cgj.desertescape.entity.tank;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import dev.cgj.desertescape.physics.UserData;
import dev.cgj.desertescape.util.SpriteUtils;

import static dev.cgj.desertescape.Constants.SPRITE_TO_WORLD;

public class Tank implements Disposable {
  private final Body body;
  private final TankTurret tankTurret;
  private final Sprite bodySprite;
  private final TankTread leftTread;
  private final TankTread rightTread;

  public Tank(World world) {
    bodySprite = SpriteUtils.getScaledSprite("sprites/enemies/tankBody.png");
    tankTurret = new TankTurret(this);
    body = createBody(world);

    leftTread = new TankTread(body, new Vector2(-8, 0));
    rightTread = new TankTread(body, new Vector2(8, 0));
  }

  public void update(float delta, Vector2 targetPosition) {
    tankTurret.update(delta, targetPosition);
    leftTread.update(delta);
    rightTread.update(delta);
  }

  public void draw(SpriteBatch batch) {
    SpriteUtils.drawAtBodyPosition(batch, bodySprite, body);
    leftTread.draw(batch);
    rightTread.draw(batch);
    tankTurret.draw(batch);
  }

  @Override
  public void dispose() {
    bodySprite.getTexture().dispose();
  }

  public Body getBody() {
    return body;
  }

  private Body createBody(World world) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    bodyDef.position.set(15, 10);
    Body car = world.createBody(bodyDef);

    PolygonShape shape = new PolygonShape();
    shape.setAsBox(5f * SPRITE_TO_WORLD, 10f * SPRITE_TO_WORLD);

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.density = 1f;
    car.createFixture(fixtureDef);

    return car;
  }

  public void setUserData(UserData userData) {
    body.setUserData(userData);
  }

  public TankTurret getTurret() {
    return tankTurret;
  }

  public TankTread getLeftTread() {
    return leftTread;
  }

  public TankTread getRightTread() {
    return rightTread;
  }

  public void brake(float input) {
    leftTread.brake(input);
    rightTread.brake(input);
  }

  public Vector2 getPosition() {
    return body.getPosition().cpy();
  }
}
