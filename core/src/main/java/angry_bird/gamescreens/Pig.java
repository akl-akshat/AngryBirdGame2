package angry_bird.gamescreens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Pig {
    private World world;
    public Body body; // The physics body of the pig
    private Sprite sprite; // The sprite for rendering the pig
    private static final float PPM = 100; // Pixels per meter for Box2D scaling
    private float health;
    private boolean isMarkedForRemoval = false;
    private boolean isDestroyed; // To track if the pig has been destroyed
    /**
     * Constructor for the Pig class.
     *
     * @param world    The Box2D world where the pig's physics body will be created.
     * @param texture  The texture used to visually represent the pig.
     * @param position The initial position of the pig in world coordinates.
     */
    public Pig(World world, Texture texture, Vector2 position) {
        this.health = 100f; // Set the pig's initial health
        this.isDestroyed = false;

        // Create the pig's physics body
        this.sprite = new Sprite(texture);
        this.sprite.setPosition(position.x, position.y);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x / PPM, position.y / PPM);
        this.body = world.createBody(bodyDef);

        // Define the pig's shape
        CircleShape shape = new CircleShape();
        shape.setRadius(texture.getWidth() / 2f / PPM);

        // Define the pig's fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.3f;
        this.world = world;
        sprite = new Sprite(texture); // Create a sprite from the texture
        createBody(world, texture, position);
        this.sprite.setSize(texture.getWidth(), texture.getHeight()); // Adjust to the desired size
        sprite.setOriginCenter();
        sprite.setPosition(position.x - sprite.getWidth() / 2, position.y - sprite.getHeight() / 2);
        this.sprite = new Sprite(texture);
        this.health = 50;
        body.setUserData(this);
    }
    public void applyDamage(float damage) {
        this.health -= damage;
        if (this.health <= 0 && !isMarkedForRemoval) {
            isMarkedForRemoval = true; // Mark for removal
        }
    }
    public boolean isMarkedForRemoval() {
        return isMarkedForRemoval;
    }
    /**
     * Creates the physics body for the pig.
     *
     * @param world    The Box2D world instance.
     * @param texture  The texture used to visually represent the pig.
     * @param position The initial position of the pig in world coordinates.
     */
    private void createBody(World world, Texture texture, Vector2 position) {
        // Define the body
        this.world = world;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody; // Pigs are dynamic (moveable)
        bodyDef.position.set(position.x / PPM, position.y / PPM);
        body = world.createBody(bodyDef);

        // Define the shape (circle)
        CircleShape shape = new CircleShape();
        shape.setRadius(texture.getWidth() / 2f / PPM); // Use half the texture width as radius

        // Define the fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f; // Mass-related property
        fixtureDef.friction = 0.5f; // Surface friction
        fixtureDef.restitution = 0.3f; // Bounciness

        // Attach the fixture to the body
        body.createFixture(fixtureDef);
        this.body.setUserData(this);
        this.body.setAwake(false);
        shape.dispose(); // Dispose the shape after use
    }
    public Body getBody() {
        return body;
    }

    public void destroyBody() {
        if (body != null) {
            world.destroyBody(body); // Safely destroy the body
            body = null;
        }
    }
    /**
     * Updates the pig's sprite position to match the physics body.
     */
    public void update() {
        if (body != null) {
            Vector2 bodyPosition = body.getPosition();
            sprite.setPosition(
                bodyPosition.x * PPM - sprite.getWidth() / 2,
                bodyPosition.y * PPM - sprite.getHeight() / 2
            );
            sprite.setRotation((float) Math.toDegrees(body.getAngle()));
        }
    }



    public void setHealth(float health) {
        this.health = health;
    }

    public void reduceHealth(float d) {
        health -= d;
        if (health <= 0) {
            // Remove pig from world
            if (body != null) {
                world.destroyBody(body);
                body = null; // Prevent further updates
            }
        }
    }

    /**
     * Renders the pig's sprite using the provided SpriteBatch.
     *
     * @param spriteBatch The SpriteBatch used for rendering.
     */
    public void render(SpriteBatch spriteBatch) {
        if (body != null) {
            sprite.draw(spriteBatch);
        }
    }

    private boolean isDestroyed() {
        return isDestroyed;
    }

    /**
     * Disposes of resources related to the pig.
     */
    public void dispose() {
        sprite.getTexture().dispose();
    }

    public float getHealth() {

        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
