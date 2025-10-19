package dev.cgj.desertescape.vehicle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import dev.cgj.desertescape.physics.TankBody;
import dev.cgj.desertescape.util.SpriteUtils;

public class Tank implements Disposable {
  private final TankBody tankBody;
  private final TankTurret tankTurret;
  private final Sprite bodySprite;

  private final Sprite treadSprite;
  private final Animation<Texture> treadAnimation;
  private float stateTime = 0f;

  public Tank(World world) {
    treadAnimation = createTreadAnimation();
    treadSprite = SpriteUtils.getScaledSprite(treadAnimation.getKeyFrame(stateTime));
    bodySprite = SpriteUtils.getScaledSprite("sprites/enemies/tankBody.png");
    tankTurret = new TankTurret(this);
    tankBody = new TankBody(world);
  }

  public void update(float delta, Vector2 targetPosition) {
    tankBody.update(delta);
    tankTurret.update(delta, targetPosition);
  }

  public void draw(SpriteBatch batch) {
    SpriteUtils.drawAtBodyPosition(batch, bodySprite, tankBody.getBody());
    SpriteUtils.drawAtBodyPosition(batch, treadSprite, tankBody.getBody());
    tankTurret.draw(batch);
  }

  @Override
  public void dispose() {
    bodySprite.getTexture().dispose();
    for (Texture tread : treadAnimation.getKeyFrames()) {
      tread.dispose();
    }
  }

  public Body getBody() {
    return tankBody.getBody();
  }

  private void updateTreads(float delta) {
    stateTime += delta;
    treadSprite.setTexture(treadAnimation.getKeyFrame(stateTime, true));
  }

  private static Animation<Texture> createTreadAnimation() {
    Texture[] treadTextures = {
      new Texture("sprites/enemies/tread1.png"),
      new Texture("sprites/enemies/tread2.png"),
      new Texture("sprites/enemies/tread3.png")
    };
    return new Animation<>(0.1f, treadTextures);
  }
}
