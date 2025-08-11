package dev.cgj.games.escape;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class TileManager implements Disposable {
  private final Texture roadTexture;
  private final Texture wallTexture;
  private final float tileSize;

  public TileManager() {
    roadTexture = new Texture("sprites/terrain/ground.png");
    wallTexture = new Texture("sprites/terrain/wall.png");
    tileSize = roadTexture.getWidth() * Constants.PIXEL_SCALE;
  }

  @Override
  public void dispose() {
    roadTexture.dispose();
  }

  public void draw(SpriteBatch batch, Vector2 position) {
    for (int i = -1; i <= 1; i++) {
      float y = position.y + i * tileSize;
      drawLeftWall(batch, new Vector2(-tileSize, y));
      drawRoadTile(batch, new Vector2(0, y));
      drawRightWall(batch, new Vector2(tileSize, y));
    }
  }

  private void drawRoadTile(SpriteBatch batch, Vector2 position) {
    Vector2 bottomLeft = roundToTileSize(position);
    batch.draw(roadTexture, bottomLeft.x, bottomLeft.y, tileSize, tileSize);
  }

  public void drawLeftWall(SpriteBatch batch, Vector2 position) {
    Vector2 bottomLeft = roundToTileSize(position);
    batch.draw(wallTexture, bottomLeft.x, bottomLeft.y, tileSize, tileSize);
  }

  public void drawRightWall(SpriteBatch batch, Vector2 position) {
    Vector2 bottomLeft = roundToTileSize(position);
    batch.draw(wallTexture, bottomLeft.x, bottomLeft.y, tileSize, tileSize, 0, 0,
      wallTexture.getWidth(), wallTexture.getHeight(), true, false);
  }

  private Vector2 roundToTileSize(Vector2 position) {
    float x = Math.round(position.x / tileSize) * tileSize;
    float y = Math.round(position.y / tileSize) * tileSize;
    return new Vector2(x, y);
  }
}
