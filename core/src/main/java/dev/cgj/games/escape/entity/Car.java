package dev.cgj.games.escape.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import dev.cgj.games.Constants;
import dev.cgj.games.old.CarType;

public class Car {
    private Texture texture;
    public Sprite sprite;
    private CarType carType;
    private Body body;

    private int health = 100;
    private int fuel = 100;
    private int numRocket = 0;
    private int numShield = 0;
    private int numNitro = 0;

    public boolean nitroActive = false;
    private boolean shieldActive = false;

    public Car(CarType carType, World world) {
        this.carType = carType;
        texture = new Texture("sprites/vehicles/sports_car.png");
        sprite = new Sprite(texture);
        sprite.setSize(texture.getWidth() * Constants.PIXEL_SCALE, texture.getHeight() * Constants.PIXEL_SCALE);
        body = createPhysicsObject(world);
    }

    public void render(SpriteBatch batch) {
        float posX = body.getPosition().x;
        float posY = body.getPosition().y;
        sprite.setCenter(posX, posY);
        sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
        sprite.draw(batch);
    }

    public void handleInput(Input input) {
        float speed = 4f;
        float delta = Gdx.graphics.getDeltaTime();

        if (input.isKeyPressed(Input.Keys.RIGHT)) {
            sprite.translateX(speed * delta);
        }

        else if (input.isKeyPressed(Input.Keys.LEFT)) {
            sprite.translateX(-speed * delta);
        }
    }

    public void updatePhysics() {

    }

    private Body createPhysicsObject(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(10, 10);
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(4f * Constants.PIXEL_SCALE, sprite.getHeight() / 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        body.createFixture(fixtureDef);

        return body;
    }
}
