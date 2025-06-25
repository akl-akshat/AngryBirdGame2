package angry_bird.gamescreens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Block {
    private World world;
    private Body body; // The physics body of the block
    public Sprite sprite; // The sprite for rendering the block
    private static final float PPM = 100; // Pixels per meter for Box2D scaling
    private int health;

    /**
     * Constructor for the Block class.
     *
     * @param world    The Box2D world where the block's physics body will be created.
     * @param texture  The texture used to visually represent the block.
     * @param position The initial position of the block in world coordinates.
     */
    public Block(World world, Texture texture, Vector2 position) {
        this.world  = world;
        createBody(world, texture, position);
        sprite.setSize(50, 50); // Adjust to the desired size
        sprite.setOriginCenter();
        sprite.setPosition(position.x - sprite.getWidth() / 2, position.y - sprite.getHeight() / 2);
        createSprite(texture, position);
        this.sprite = new Sprite(texture);
        this.health = 100; // Example health value
        body.setUserData(this);
    }

    /**
     * Creates the physics body for the block.
     *
     * @param world    The Box2D world instance.
     * @param texture  The texture used to visually represent the block.
     * @param position The initial position of the block in world coordinates.
     */
    private void createBody(World world, Texture texture, Vector2 position) {
        // Define the body
        this.world = world;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody; // Blocks are static
        bodyDef.position.set(position.x / PPM, position.y / PPM);
        body = world.createBody(bodyDef);

        // Define the shape (rectangle)
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(
            texture.getWidth() / 2f / PPM,  // Half-width in world units
            texture.getHeight() / 2f / PPM // Half-height in world units
        );

        // Define the fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f; // Mass-related property
        fixtureDef.friction = 0.5f; // Surface friction
        fixtureDef.restitution = 0.2f; // Bounciness

        // Attach the fixture to the body
        body.createFixture(fixtureDef);
        shape.dispose(); // Dispose of shape after use
    }

    /**
     * Creates the sprite for the block.
     *
     * @param texture  The texture for the block.
     * @param position The initial position of the block in world coordinates.
     */
    private void createSprite(Texture texture, Vector2 position) {
        sprite = new Sprite(texture);
        sprite.setSize(100, 50); // Adjust size to match visual appearance
        sprite.setOriginCenter();
        sprite.setPosition(position.x - sprite.getWidth() / 2, position.y - sprite.getHeight() / 2);
    }

    /**
     * Updates the block's sprite position to match the physics body.
     */
    public void update() {
        Vector2 bodyPosition = body.getPosition();
        sprite.setPosition(
            bodyPosition.x * PPM - sprite.getWidth() / 2,
            bodyPosition.y * PPM - sprite.getHeight() / 2
        );
        sprite.setRotation((float) Math.toDegrees(body.getAngle())); // Rotate based on physics body
    }

    public void reduceHealth(float d) {
        health -= d;
        if (health <= 0) {
            // Remove block from world
            if (body != null) {
                world.destroyBody(body);
                body = null; // Prevent further updates
            }
        }
    }
    public Body getBody() {
        return body; // Assuming `body` is a field in the `Block` class
    }


    public int getHealth() {
        return health;
    }



    public void render(SpriteBatch batch) {
        sprite.draw(batch); // Render the sprite
    }

    public void dispose() {
        sprite.getTexture().dispose();
    }
    public void setHealth(int health) {
        this.health = health;
    }
}
