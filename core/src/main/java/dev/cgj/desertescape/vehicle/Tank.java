package dev.cgj.desertescape.vehicle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import dev.cgj.desertescape.physics.TankBody;
import dev.cgj.desertescape.util.SpriteUtil;

public class Tank implements Disposable {
  private final TankBody tankBody;

  private final Sprite bodySprite;
  private final Sprite turretSprite;
  private final Sprite treadSprite;

  private final Animation<Texture> treadAnimation;
  private float stateTime = 0f;

  public Tank(World world) {
    treadAnimation = createTreadAnimation();
    treadSprite = SpriteUtil.getScaledSprite(treadAnimation.getKeyFrame(stateTime));
    bodySprite = SpriteUtil.getScaledSprite("sprites/enemies/tankBody.png");
    turretSprite = SpriteUtil.getScaledSprite("sprites/enemies/turret.png");
    tankBody = new TankBody(world);
  }

  public void update(float delta) {
    tankBody.update(delta);
    stateTime += delta;
    treadSprite.setTexture(treadAnimation.getKeyFrame(stateTime, true));
  }

  public void draw(SpriteBatch batch) {
    SpriteUtil.drawAtBodyPosition(batch, bodySprite, tankBody.getBody());
    SpriteUtil.drawAtBodyPosition(batch, turretSprite, tankBody.getBody());
    SpriteUtil.drawAtBodyPosition(batch, treadSprite, tankBody.getBody());
  }

  @Override
  public void dispose() {
    bodySprite.getTexture().dispose();
    turretSprite.getTexture().dispose();
    for (Texture tread : treadAnimation.getKeyFrames()) {
      tread.dispose();
    }
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
