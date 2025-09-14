package dev.cgj.desertescape.terrain;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import dev.cgj.desertescape.entity.Obstacle;

import java.util.List;

import static dev.cgj.desertescape.Constants.SPRITE_TO_WORLD;

public class Tile {

  /** The size of each tile in world units **/
  public static final float TILE_SIZE = 270f * SPRITE_TO_WORLD;

  private final Texture texture;
  private final List<Body> staticBodies;
  private final List<Obstacle> obstacles;
  private final Vector2 position;
  private final World world;

  public Tile(TileDefinition definition, World world) {
    this.texture = definition.getTexturePath();
    this.staticBodies = definition.addStaticBodies(world);
    this.obstacles = definition.addObstacles(world);
    this.position = Vector2.Zero.cpy();
    this.world = world;
  }

  public Texture getTexture() {
    return texture;
  }

  public Vector2 getPosition() {
    return position.cpy();
  }

  /**
   * Sets the position of this tile, including moving the static bodies to have the same position
   * relative to the new position as they did to the old one.
   *
   * @param position The new bottom-left position of the tile, in world coordinates.
   */
  public void setPosition(Vector2 position) {
    Vector2 delta = position.cpy().sub(this.position);

    for (Body body : staticBodies) {
      Vector2 newPosition = body.getPosition().add(delta);
      body.setTransform(newPosition, body.getAngle());
    }

    for (Obstacle obstacle : obstacles) {
      obstacle.move(delta);
    }

    this.position.set(position.cpy());
  }

  public void draw(SpriteBatch batch) {
    Vector2 position = getPosition();
    batch.draw(getTexture(), position.x, position.y, TILE_SIZE, TILE_SIZE);
    for (Obstacle obstacle : obstacles) {
      obstacle.draw(batch);
    }
  }

  public void dispose() {
    for (Body body : staticBodies) {
      world.destroyBody(body);
    }
    for (Obstacle obstacle : obstacles) {
      obstacle.dispose();
    }
    texture.dispose();
  }
}
