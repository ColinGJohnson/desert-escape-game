package dev.cgj.games.escape.physics;

import com.badlogic.gdx.physics.box2d.*;

public class EntityContactListener implements ContactListener {

  @Override
  public void beginContact(Contact contact) {
    Fixture fixtureA = contact.getFixtureA();
    Fixture fixtureB = contact.getFixtureB();

    if (fixtureA.isSensor() || fixtureB.isSensor()) {
      handleSensorContact(fixtureA, fixtureB, true);
    }
  }

  @Override
  public void endContact(Contact contact) {
    Fixture fixtureA = contact.getFixtureA();
    Fixture fixtureB = contact.getFixtureB();

    if (fixtureA.isSensor() || fixtureB.isSensor()) {
      handleSensorContact(fixtureA, fixtureB, false);
    }
  }

  private void handleSensorContact(Fixture fixtureA, Fixture fixtureB, boolean isBeginning) {
    Fixture sensor = fixtureA.isSensor() ? fixtureA : fixtureB;
    Fixture other = fixtureA.isSensor() ? fixtureB : fixtureA;

    Object sensorData = sensor.getBody().getUserData();
    Object otherData = other.getBody().getUserData();

    if (isBeginning) {
      System.out.println("Sensor collision started");
    } else {
      System.out.println("Sensor collision ended");
    }
  }

  @Override
  public void preSolve(Contact contact, Manifold oldManifold) { }

  @Override
  public void postSolve(Contact contact, ContactImpulse impulse) { }
}
