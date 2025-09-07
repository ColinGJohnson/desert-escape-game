package dev.cgj.games.escape.terrain;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dev.cgj.games.escape.terrain.Tile.TILE_SIZE;

public class TileManager implements Disposable {
  record TilePosition(int x, int y) { }
  private final Map<TilePosition, Tile> tiles = new HashMap<>();
  private final World world;

  public TileManager(World world) {
    this.world = world;
  }

  public void update(Vector2 playerPosition) {
    List<TilePosition> requiredTiles = getVisibleTilePositions(playerPosition);

    // Add tiles that should be visible but haven't yet been created
    for (TilePosition requiredTile : requiredTiles) {
      if (!tiles.containsKey(requiredTile)) {
        Tile tile = new Tile(getTileDefinition(requiredTile), world);
        tile.setPosition(new Vector2(requiredTile.x, requiredTile.y));
        tiles.put(requiredTile, tile);
      }
    }

    // Remove tiles which have been created but are no longer visible
    for (TilePosition existingTile : new ArrayList<>(tiles.keySet())) {
      if (!requiredTiles.contains(existingTile)) {
        Tile tile = tiles.get(existingTile);
        tile.dispose();
        tiles.remove(existingTile);
      }
    }
  }

  private TileDefinition getTileDefinition(TilePosition position) {
    if (position.x < 0) {
      return new WallTile(WallTile.Direction.LEFT);
    } else if (position.x > 0) {
      return new WallTile(WallTile.Direction.RIGHT);
    }
    return new RoadTile();
  }

  private List<TilePosition> getVisibleTilePositions(Vector2 playerPosition) {
    List<TilePosition> visibleTiles = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      float y = playerPosition.y + (i - 1) * TILE_SIZE;
      visibleTiles.add(roundToTileSize(new Vector2(0, y)));
      visibleTiles.add(roundToTileSize(new Vector2(-TILE_SIZE, y)));
      visibleTiles.add(roundToTileSize(new Vector2(TILE_SIZE, y)));
    }
    return visibleTiles;
  }

  private TilePosition roundToTileSize(Vector2 position) {
    int x = Math.round(position.x / TILE_SIZE) * (int) TILE_SIZE;
    int y = Math.round(position.y / TILE_SIZE) * (int) TILE_SIZE;
    return new TilePosition(x, y);
  }

  public void draw(SpriteBatch batch) {
    for (Tile tile : tiles.values()) {
      tile.draw(batch);
    }
  }

  @Override
  public void dispose() {
    tiles.values().forEach(Tile::dispose);
  }
}
