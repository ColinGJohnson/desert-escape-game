package dev.cgj.games.escape;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class DesertEscape extends Game {
    public FitViewport viewport;
    public SpriteBatch batch;
    public BitmapFont font;
    public Box2DDebugRenderer debugRenderer;

    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(); // use libGDX's default font
        viewport = new FitViewport(200 * Constants.PIXEL_SCALE, 200 * Constants.PIXEL_SCALE);
        debugRenderer = new Box2DDebugRenderer();

        // font has 15 pt, but we need to scale it to our viewport by ratio of viewport height to screen height
        font.setUseIntegerPositions(false);
        font.getData().setScale(viewport.getWorldHeight() / Gdx.graphics.getHeight());

        this.setScreen(new GameScreen(this));
    }

    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
