package dev.cgj.desertescape.entity.tank;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;
import dev.cgj.desertescape.physics.BodyUtils;
import dev.cgj.desertescape.physics.WheelBody;
import dev.cgj.desertescape.util.SpriteUtils;

public class TankTread implements Disposable {
  private static final float TRACK_FRICTION = 0.4f;
  private static final float TRACK_MAX_LATERAL_IMPULSE = 30f;
  private static final float TRACK_MAX_BRAKE_IMPULSE = 10f;

  // TODO: Calculate a realistic value for this based on the number of pixels advanced per frame
  private static final float ANIMATION_FRAME_DURATION = 0.1f;

  private final WheelBody tread;
  private final Sprite sprite;
  private final Animation<Texture> animation;
  private float stateTime = 0f;

  public TankTread(Body tankBody, Vector2 anchor) {
    this.tread = new WheelBody(tankBody, anchor, new Vector2(2, 10));
    animation = createTreadAnimation();
    sprite = SpriteUtils.getScaledSprite(animation.getKeyFrame(stateTime));
  }

  private static Animation<Texture> createTreadAnimation() {
    Texture[] treadTextures = {
      new Texture("sprites/enemies/tread1.png"),
      new Texture("sprites/enemies/tread2.png"),
      new Texture("sprites/enemies/tread3.png")
    };
    Animation<Texture> animation = new Animation<>(ANIMATION_FRAME_DURATION, treadTextures);
    animation.setPlayMode(Animation.PlayMode.LOOP);
    return animation;
  }

  public void update(float delta) {
    tread.applyFriction(TRACK_FRICTION);
    tread.cancelLateralVelocity(TRACK_MAX_LATERAL_IMPULSE);
    updateTreadAnimation(delta);
  }

  private void updateTreadAnimation(float delta) {
    float localForwardVelocity = BodyUtils.getForwardNormal(tread.getBody())
      .dot(tread.getBody().getLinearVelocity());

    if (Math.signum(localForwardVelocity) > 0) {
      animation.setPlayMode(Animation.PlayMode.LOOP);
    } else {
      animation.setPlayMode(Animation.PlayMode.LOOP_REVERSED);
    }

    stateTime += delta * Math.abs(localForwardVelocity);
    sprite.setTexture(animation.getKeyFrame(stateTime));
  }

  public void draw(SpriteBatch batch) {
    SpriteUtils.drawAtBodyPosition(batch, sprite, tread.getBody());
  }

  @Override
  public void dispose() {
    sprite.getTexture().dispose();
    for (Texture tread : animation.getKeyFrames()) {
      tread.dispose();
    }
  }

  public void brake(float input) {
    float clampedInput = MathUtils.clamp(input, 0, 1f);
    tread.brake(clampedInput * TRACK_MAX_BRAKE_IMPULSE);
  }

  public void accelerate(float input) {
    float clampedInput = MathUtils.clamp(input, -1, 1f);
  }
}
