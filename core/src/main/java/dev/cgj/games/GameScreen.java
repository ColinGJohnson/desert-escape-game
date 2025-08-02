package dev.cgj.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import dev.cgj.games.escape.entity.Car;
import dev.cgj.games.old.CarType;

public class GameScreen implements Screen {
    final DesertEscape game;
    private float accumulator = 0;

    Texture backgroundTexture;

    Texture dropTexture;
    Vector2 touchPos;
    Array<Sprite> dropSprites;
    float dropTimer;
    Rectangle bucketRectangle;
    Rectangle dropRectangle;
    int dropsGathered;

    Car car;

    public GameScreen(final DesertEscape game) {
        this.game = game;

        // load the images for the background, bucket and droplet
        backgroundTexture = new Texture("sprites/terrain/startGround2.png");
        System.out.println("Background texture loaded: " + backgroundTexture.getWidth() + "x" + backgroundTexture.getHeight());

        dropTexture = new Texture("sprites/obstacles/skull.png");
        System.out.println("Drop texture loaded: " + dropTexture.getWidth() + "x" + dropTexture.getHeight());

        touchPos = new Vector2();

        bucketRectangle = new Rectangle();
        dropRectangle = new Rectangle();

        dropSprites = new Array<>();

        car = new Car(CarType.SPORTS, game.world);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        input();
        doPhysicsStep(delta);
        logic();
        draw();
    }

    /**
     * Fixed time step with max frame time to avoid a spiral of death on slow devices.
     */
    private void doPhysicsStep(float deltaTime) {
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= Constants.TIME_STEP) {
            game.world.step(Constants.TIME_STEP, Constants.VELOCITY_ITERATIONS, Constants.POSITION_ITERATIONS);
            accumulator -= Constants.TIME_STEP;
        }
    }

    private void input() {
        float speed = 4f;
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            car.sprite.translateX(speed * delta);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            car.sprite.translateX(-speed * delta);
        }

        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            game.viewport.unproject(touchPos);
            car.sprite.setCenterX(touchPos.x);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            System.out.println("Ending game...");
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    private void logic() {
        float worldWidth = game.viewport.getWorldWidth();
        float bucketWidth = car.sprite.getWidth();
        float bucketHeight = car.sprite.getHeight();
        float delta = Gdx.graphics.getDeltaTime();

        car.sprite.setX(MathUtils.clamp(car.sprite.getX(), 0, worldWidth - bucketWidth));
        bucketRectangle.set(car.sprite.getX(), car.sprite.getY(), bucketWidth, bucketHeight);

        for (int i = dropSprites.size - 1; i >= 0; i--) {
            Sprite dropSprite = dropSprites.get(i);
            float dropWidth = dropSprite.getWidth();
            float dropHeight = dropSprite.getHeight();

            dropSprite.translateY(-2f * delta);
            dropRectangle.set(dropSprite.getX(), dropSprite.getY(), dropWidth, dropHeight);

            if (dropSprite.getY() < -dropHeight) dropSprites.removeIndex(i);
            else if (bucketRectangle.overlaps(dropRectangle)) {
                dropsGathered++;
                dropSprites.removeIndex(i);
            }
        }

        dropTimer += delta;
        if (dropTimer > 1f) {
            dropTimer = 0;
            createDroplet();
        }
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.batch.begin();

        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();

        // Draw all sprites first (same texture binding)
        game.batch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);
        car.sprite.draw(game.batch);

        for (Sprite dropSprite : dropSprites) {
            dropSprite.draw(game.batch);
        }

        // Flush batch before switching to font rendering
        game.batch.flush();

        // Now draw text
        game.font.draw(game.batch, "Drops collected: " + dropsGathered, 0, worldHeight);
        game.batch.end();

        // Render Box2D debug AFTER batch.end()
        game.debugRenderer.render(game.world, game.viewport.getCamera().combined);
    }

    private void createDroplet() {
        Sprite dropSprite = new Sprite(dropTexture);
        dropSprite.setSize(dropTexture.getWidth() * Constants.PIXEL_SCALE, dropTexture.getHeight() * Constants.PIXEL_SCALE);
        dropSprite.setX(MathUtils.random(0F, game.viewport.getWorldWidth() - dropSprite.getWidth()));
        dropSprite.setY(game.viewport.getWorldHeight());
        dropSprites.add(dropSprite);
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        dropTexture.dispose();
        car.sprite.getTexture().dispose();
    }
}
