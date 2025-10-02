package dev.cgj.desertescape.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import static dev.cgj.desertescape.Constants.SPRITE_TO_WORLD;

public abstract class Entity implements Disposable {
  private final Sprite sprite;
  private final Body body;
  private final World world;

  public Entity(String spritePath, World world, Vector2 position) {
    this.world = world;
    sprite = createSprite(spritePath);
    body = createBody(position);
  }

  public Body createBody(Vector2 position) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.StaticBody;
    bodyDef.position.set(position);
    Body body = world.createBody(bodyDef);

    PolygonShape shape = new PolygonShape();
    shape.setAsBox(getSprite().getWidth() / 2, getSprite().getHeight() / 2);
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.isSensor = true;

    body.createFixture(fixtureDef);
    return body;
  }

  @Override
  public void dispose() {
    sprite.getTexture().dispose();
    world.destroyBody(body);
  }

  private Sprite createSprite(String spritePath) {
    Texture texture = new Texture(spritePath);
    Sprite sprite = new Sprite(texture);
    sprite.setSize(texture.getWidth() * SPRITE_TO_WORLD,
      texture.getHeight() * SPRITE_TO_WORLD);
    sprite.setOriginCenter();
    return sprite;
  }

  public void draw(SpriteBatch batch) {
    float posX = body.getPosition().x;
    float posY = body.getPosition().y;
    sprite.setCenter(posX, posY);
    sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
    sprite.draw(batch);
  }

  public void move(Vector2 delta) {
    Vector2 newPosition = getBody().getPosition().add(delta);
    getBody().setTransform(newPosition, getBody().getAngle());
  }

  public Sprite getSprite() {
    return sprite;
  }

  public Body getBody() {
    return body;
  }

  public World getWorld() {
    return world;
  }
}
