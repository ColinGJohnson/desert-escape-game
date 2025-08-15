package dev.cgj.games.escape.terrain;

import com.badlogic.gdx.physics.box2d.Body;

import java.util.List;

public interface TileDefinition {

  String getTexturePath();

  List<Body> getStaticBodies();
}
