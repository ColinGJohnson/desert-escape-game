package dev.cgj.desertescape.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class CollisionListener implements ContactListener {

  @Override
  public void beginContact(Contact contact) {
    Object userDataA = contact.getFixtureA().getBody().getUserData();
    Object userDataB = contact.getFixtureB().getBody().getUserData();

    if (userDataA instanceof UserData && userDataB instanceof UserData) {
      handleCollision((UserData) userDataA, (UserData) userDataB);
    }
  }

  private static void handleCollision(UserData entityA, UserData entityB) {
    entityA.handleCollision(entityB.getCollisionData());
    entityB.handleCollision(entityA.getCollisionData());
  }

  @Override
  public void endContact(Contact contact) { }

  @Override
  public void preSolve(Contact contact, Manifold oldManifold) { }

  @Override
  public void postSolve(Contact contact, ContactImpulse impulse) { }
}
