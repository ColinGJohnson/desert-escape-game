package dev.cgj.games.escape.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import dev.cgj.games.Constants;
import dev.cgj.games.old.CarType;

public class Car {
    private Texture texture;
    public Sprite sprite;
    private CarType carType;

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
        createPhysicsObject(world);
    }

    private void createPhysicsObject(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(sprite.getX() / Constants.PIXEL_SCALE, sprite.getY() / Constants.PIXEL_SCALE);
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / 2f, sprite.getHeight() / 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f; // Make it bounce a little bit

        Fixture fixture = body.createFixture(fixtureDef);
    }

    public void move(long delta) {

    }

    public void useRocket() {

    }

    public void useNitro() {

    }

    public void addFuel() {

    }
}
