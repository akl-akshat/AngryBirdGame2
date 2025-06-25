package angry_bird.gamescreens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Bird {
    private World world;
    public Body body;// The physics body of the bird
    private String type;
    private Sprite sprite; // The sprite for rendering the bird
    private static final float PPM = 100; // Pixels per meter for Box2D scaling
    private static float health  = 100;
    private int power;
//    private String type = "Bird";
    /**
     * Constructor for the Bird class.
     *
     * @param world   The Box2D world instance where the bird's physics body will be created.
     * @param texture The texture used to represent the bird visually.
     * @param position The initial position of the bird in world coordinates.
     */
    public Bird(World world, Texture texture, Vector2 position) {
        this.world = world;
        createBody(world, position);
        createSprite(texture, position);
        this.health = health; // Example health value
        this.power = 1000;
        body.setUserData(this);
        this.type = texture.toString(); // Example type assignment
        this.sprite.setPosition(position.x, position.y);
    }



    public String getType() {
        return type;
    }
    public float getHealth() {
        return health;
    }

    public static void setHealth(float health) {
        Bird.health = health;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void reduceHealth(float hitPoint) {
        health -= hitPoint;
        if (health <= 0) {
            // Remove bird from world
            if (body != null) {
                world.destroyBody(body);
                body = null; // Prevent further updates
            }
        }
    }


    public int getPower() {
        return power;
    }


    /**
     * Creates the physics body for the bird.
     *
     * @param world    The Box2D world instance.
     * @param position The initial position of the bird in world coordinates.
     */
    private void createBody(World world, Vector2 position) {
        // Define the body
        this.world = world;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x / PPM, position.y / PPM);
        body = world.createBody(bodyDef);

        // Define the shape
        CircleShape shape = new CircleShape();
        shape.setRadius(25 / PPM); // Adjust radius based on sprite size

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
        body.setLinearDamping(0.3f); // Reduces the speed gradually
        shape.dispose(); // Dispose the shape after use

    }

    /**
     * Creates the sprite for the bird.
     *
     * @param texture  The texture for the bird.
     * @param position The initial position of the bird in world coordinates.
     */
    private void createSprite(Texture texture, Vector2 position) {
        sprite = new Sprite(texture);
        sprite.setSize(50, 50); // Adjust size to match visual appearance
        sprite.setOriginCenter();
        sprite.setPosition(position.x - sprite.getWidth() / 2, position.y - sprite.getHeight() / 2);
    }

    /**
     * Updates the bird's sprite position and rotation based on its physics body.
     */
    public void update() {
        Vector2 bodyPosition = body.getPosition();
        sprite.setPosition(
            bodyPosition.x * PPM - sprite.getWidth() / 2,
            bodyPosition.y * PPM - sprite.getHeight() / 2
        );
        sprite.setRotation((float) Math.toDegrees(body.getAngle()));
    }

    /**
     * Draws the bird's sprite.
     *
     * @param batch The SpriteBatch used for rendering.
     */
    public void render(com.badlogic.gdx.graphics.g2d.SpriteBatch batch) {
        sprite.draw(batch);
    }

    /**
     * Applies an impulse to the bird's physics body.
     *
     * @param force The force vector to apply.
     */
    public void applyImpulse(Vector2 force) {
        body.setActive(true);
        body.applyLinearImpulse(force, body.getWorldCenter(), true);
    }

    /**
     * Resets the bird's position and deactivates it.
     *
     * @param position The position to reset the bird to.
     */
    public void reset(Vector2 position) {
        body.setTransform(position.x / PPM, position.y / PPM, 0);
        body.setLinearVelocity(0, 0);
        body.setAngularVelocity(0);
        body.setActive(false);
    }

    /**
     * Disposes of resources related to the bird.
     */
    public void dispose() {
        sprite.getTexture().dispose();
    }

    /**
     * Gets the Box2D body of the bird.
     *
     * @return The Box2D body of the bird.
     */
    public Body getBody() {
        return body;
    }

    /**
     * Sets the Box2D body of the bird.
     *
     * @param body The Box2D body of the bird.
     */
    public void setBody(Body body) {
        this.body = body;
    }
}
