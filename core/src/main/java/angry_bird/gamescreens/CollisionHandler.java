package angry_bird.gamescreens;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

class CollisionHandler implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        // Get the fixtures involved in the collision
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        // Retrieve user data associated with each fixture
        Object userDataA = fixtureA.getBody().getUserData();
        Object userDataB = fixtureB.getBody().getUserData();

        // Check if a bird is involved
        if (userDataA instanceof Bird && (userDataB instanceof Pig || userDataB instanceof Block)) {
            handleCollision((Bird) userDataA, userDataB);
        } else if (userDataB instanceof Bird && (userDataA instanceof Pig || userDataA instanceof Block)) {
            handleCollision((Bird) userDataB, userDataA);
        }
    }

    @Override
    public void endContact(Contact contact) {
        // Optional: Handle end of contact
    }


    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // Optional: Handle pre-solve logic
    }

    @Override
    public void postSolve(Contact contact, com.badlogic.gdx.physics.box2d.ContactImpulse impulse) {
        // Optional: Handle post-solve logic
    }


    private void handleCollision(Bird bird, Object other) {
        System.out.println("Collision Detected: Bird with " + other.getClass().getSimpleName());

        // Calculate velocity magnitude
        float velocity = bird.getBody().getLinearVelocity().len();

        // Calculate hit points
        // float hitPoint = bird.getPower() * velocity;

       // System.out.println("Hit Point: " + hitPoint);

        // Apply damage
        if (other instanceof Pig) {
            ((Pig) other).reduceHealth(100);
            System.out.println("Pig Health: " + ((Pig) other).getHealth());
        } else if (other instanceof Block) {
            ((Block) other).reduceHealth(100);
            System.out.println("Block Health: " + ((Block) other).getHealth());
        }

        // Reduce bird health
        bird.reduceHealth(100);
        System.out.println("Bird Health: " + bird.getHealth());
    }

}

