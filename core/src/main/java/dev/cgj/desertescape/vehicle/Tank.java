package dev.cgj.desertescape.vehicle;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import dev.cgj.desertescape.physics.TankBody;
import dev.cgj.desertescape.util.SpriteUtil;

public class Tank implements Disposable {
  private final Sprite bodySprite;
  private final Sprite turretSprite;
  private final TankBody tankBody;

  public Tank(World world) {
    bodySprite = SpriteUtil.getScaledSprite("sprites/enemies/body1.png");
    turretSprite = SpriteUtil.getScaledSprite("sprites/enemies/turret.png");
    tankBody = new TankBody(world);
  }

  public void update(float delta) {
    tankBody.update(delta);
  }

  public void draw(SpriteBatch batch) {
    SpriteUtil.drawAtBodyPosition(batch, bodySprite, tankBody.getBody());
  }

  @Override
  public void dispose() {
    bodySprite.getTexture().dispose();
    turretSprite.getTexture().dispose();
  }
}
