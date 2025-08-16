package dev.cgj.games.escape.terrain;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import java.util.List;

import static dev.cgj.games.escape.Constants.SPRITE_TO_WORLD;

public class Tile {

  /** The size of each tile in world units **/
  public static final float TILE_SIZE = 270f * SPRITE_TO_WORLD;

  private final Texture texture;
  private final List<Body> staticBodies;
  private final Vector2 position;

  public Tile(TileDefinition definition, World world) {
    this.texture = definition.getTexturePath();
    this.staticBodies = definition.addStaticBodies(world);
    this.position = Vector2.Zero.cpy();
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
    for (Body body : staticBodies) {
      Vector2 newPosition = body.getPosition().add(position.cpy().sub(this.position));
      body.setTransform(newPosition, body.getAngle());
    }
    this.position.set(position.cpy());
  }

  public void dispose() {
    texture.dispose();
  }
}
