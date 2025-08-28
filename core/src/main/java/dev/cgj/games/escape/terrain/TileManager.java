package dev.cgj.games.escape.terrain;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import dev.cgj.games.escape.terrain.WallTile.Direction;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static dev.cgj.games.escape.terrain.Tile.TILE_SIZE;

public class TileManager implements Disposable {
  List<Tile> leftWallTiles;
  List<Tile> rightWallTiles;
  List<Tile> roadTiles;

  public TileManager(World world) {
    roadTiles = generateTiles(RoadTile::new, world);
    leftWallTiles = generateTiles(() -> new WallTile(Direction.LEFT), world);
    rightWallTiles = generateTiles(() -> new WallTile(Direction.RIGHT), world);
  }

  @Override
  public void dispose() {
    Stream.of(leftWallTiles, roadTiles, rightWallTiles)
      .flatMap(Collection::stream)
      .forEach(Tile::dispose);
  }

  public void update(Vector2 playerPosition) {
    for (int i = 0; i < 3; i++) {
      float y = playerPosition.y + (i - 1) * TILE_SIZE;
      roadTiles.get(i).setPosition(roundToTileSize(new Vector2(0, y)));
      leftWallTiles.get(i).setPosition(roundToTileSize(new Vector2(-TILE_SIZE, y)));
      rightWallTiles.get(i).setPosition(roundToTileSize(new Vector2(TILE_SIZE, y)));
    }
  }

  public void draw(SpriteBatch batch) {
    drawTiles(batch, roadTiles);
    drawTiles(batch, leftWallTiles);
    drawTiles(batch, rightWallTiles);
  }

  private void drawTiles(SpriteBatch batch, List<Tile> tiles) {
    for (Tile tile : tiles) {
      tile.draw(batch);
    }
  }

  private Vector2 roundToTileSize(Vector2 position) {
    float x = Math.round(position.x / TILE_SIZE) * TILE_SIZE;
    float y = Math.round(position.y / TILE_SIZE) * TILE_SIZE;
    return new Vector2(x, y);
  }

  <T extends TileDefinition> List<Tile> generateTiles(Supplier<T> supplier, World world) {
    return Stream.generate(supplier)
      .map(tileDefinition -> new Tile(tileDefinition, world))
      .limit(3)
      .toList();
  }
}
