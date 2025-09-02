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
        font = new BitmapFont(Gdx.files.internal("fonts/kenney_mini.fnt"));
        viewport = new FitViewport(480 * Constants.SPRITE_TO_WORLD, 270 * Constants.SPRITE_TO_WORLD);
        debugRenderer = new Box2DDebugRenderer();
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
