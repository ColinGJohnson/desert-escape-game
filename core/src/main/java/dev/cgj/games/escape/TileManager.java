package dev.cgj.games.escape;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class TileManager implements Disposable {
  private final Texture dirtTexture;
  private final float tileSize;

  public TileManager() {
    dirtTexture = new Texture("sprites/terrain/ground.png");
    tileSize = dirtTexture.getWidth() * Constants.PIXEL_SCALE;
  }

  @Override
  public void dispose() {
    dirtTexture.dispose();
  }

  public void draw(SpriteBatch batch, Vector2 position) {
    drawBackgroundTile(batch, new Vector2(position.x, position.y + tileSize));
    drawBackgroundTile(batch, new Vector2(position.x, position.y));
    drawBackgroundTile(batch, new Vector2(position.x, position.y - tileSize));
  }

  private void drawBackgroundTile(SpriteBatch batch, Vector2 position) {
    float y = Math.round(position.y / tileSize) * tileSize;
    batch.draw(dirtTexture, 0, y, tileSize, tileSize);
  }
}
