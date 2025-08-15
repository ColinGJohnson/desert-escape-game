package dev.cgj.games.escape.terrain;

import com.badlogic.gdx.physics.box2d.Body;

import java.util.List;

public class RoadTile implements TileDefinition {

  @Override
  public String getTexturePath() {
    return "sprites/terrain/ground.png";
  }

  @Override
  public List<Body> getStaticBodies() {
    return List.of();
  }
}
