package dev.cgj.games.escape.terrain;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static dev.cgj.games.escape.terrain.Tile.TILE_SIZE;

public class TileManager implements Disposable {
  List<Tile> leftWallTiles;
  List<Tile> rightWallTiles;
  List<Tile> roadTiles;

  public TileManager() {
    leftWallTiles = Stream.generate(WallTile::new).map(Tile::new).limit(3).toList();
    rightWallTiles = Stream.generate(WallTile::new).map(Tile::new).limit(3).toList();
    roadTiles = Stream.generate(RoadTile::new).map(Tile::new).limit(3).toList();
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
      leftWallTiles.get(i).setPosition(roundToTileSize(new Vector2(-TILE_SIZE, y)));
      rightWallTiles.get(i).setPosition(roundToTileSize(new Vector2(TILE_SIZE, y)));
      roadTiles.get(i).setPosition(roundToTileSize(new Vector2(0, y)));
    }
  }

  public void draw(SpriteBatch batch) {
    drawTiles(batch, leftWallTiles);
    drawTiles(batch, rightWallTiles);
    drawTiles(batch, roadTiles);
  }

  private void drawTiles(SpriteBatch batch, List<Tile> tiles) {
    for (Tile tile : tiles) {
      Vector2 position = tile.getPosition();
      batch.draw(tile.getTexture(), position.x, position.y, TILE_SIZE, TILE_SIZE);
    }
  }

  private Vector2 roundToTileSize(Vector2 position) {
    float x = Math.round(position.x / TILE_SIZE) * TILE_SIZE;
    float y = Math.round(position.y / TILE_SIZE) * TILE_SIZE;
    return new Vector2(x, y);
  }
}
