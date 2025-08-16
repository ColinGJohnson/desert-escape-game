package dev.cgj.games.escape.terrain;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import java.util.List;

import static dev.cgj.games.escape.terrain.Tile.TILE_SIZE;

public class WallTile implements TileDefinition {
  public enum Direction { LEFT, RIGHT }
  private final Direction direction;

  public WallTile(Direction direction) {
    this.direction = direction;
  }

  @Override
  public Texture getTexturePath() {
  String spriteName = direction == Direction.LEFT ? "wall_left" : "wall_right";
    return new Texture(String.format("sprites/terrain/%s.png", spriteName));
  }

  @Override
  public List<Body> addStaticBodies(World world) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.StaticBody;
    bodyDef.position.set(direction == Direction.RIGHT ? 0 : TILE_SIZE, 0);
    Body body = world.createBody(bodyDef);

    EdgeShape edgeShape = new EdgeShape();
    edgeShape.set(new Vector2(0, 0), new Vector2(0, TILE_SIZE));

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = edgeShape;
    body.createFixture(fixtureDef);

    return List.of(body);
  }
}
