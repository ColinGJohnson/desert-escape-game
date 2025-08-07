package dev.cgj.games.escape.entity;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import dev.cgj.games.Constants;
import dev.cgj.games.old.CarType;

/// Uses physics adapted from [this iforce2d article](https://www.iforce2d.net/b2dtut/top-down-car).
public class Car {
    float speed = 4f;
    float maxForwardSpeed = 10f;
    float maxBackwardSpeed = -2f;
    float maxDriveForce = 1e10f;

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
        if (input.isKeyPressed(Input.Keys.UP) || input.isKeyPressed(Input.Keys.W)) {
            Vector2 forwardNormal = body.getWorldVector(new Vector2(0, 1));
            float currentSpeed = getForwardVelocity().dot(forwardNormal);
            if (currentSpeed != maxForwardSpeed) {
                float force = (currentSpeed < maxForwardSpeed) ?  maxDriveForce : -maxDriveForce;
                body.applyForceToCenter(forwardNormal.scl(force), true);
            }
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

    private Vector2 getForwardVelocity() {
        Vector2 forwardNormal = body.getWorldVector(new Vector2(0, 1));
        return forwardNormal.scl(forwardNormal.dot(body.getLinearVelocity()));
    }

    private Vector2 getLateralVelocity() {
        Vector2 rightNormal = body.getWorldVector(new Vector2(1, 0));
        return rightNormal.scl(rightNormal.dot(body.getLinearVelocity()));
    }
}
