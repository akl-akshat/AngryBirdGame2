package angry_bird.gamescreens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;

public class Slingshot {
    private World world;
    private Sprite slingshotSprite; // Sprite for the slingshot base
    private Body anchorBody;
    private Body birdBody1;
    private Body birdBody2;
    private Body birdBody3;
    private Vector2 anchorPoint; // Fixed point of the slingshot
    private Vector2 stretchedPosition; // Stretched position when pulling
    public boolean isStretching = false; // Track if the slingshot is being pulled
    private float maxStretchDistance = 150f; // Maximum stretch distance in pixels
    private float powerFactor = 10f; // Determines the force applied when launching the bird

    private ShapeRenderer shapeRenderer; // For drawing the slingshot strings
    private Vector2 leftAttachmentPoint; // Left side of the slingshot's base
    private Vector2 rightAttachmentPoint; // Right side of the slingshot's base

    public Slingshot(World world, Texture texture, Vector2 anchorPoint) {
        this.world = world;
        this.anchorPoint = anchorPoint;

        // Initialize the sprite for the slingshot base
        this.slingshotSprite = new Sprite(texture);
        this.slingshotSprite.setSize(100, 100); // Default size
        this.slingshotSprite.setOriginCenter();
        this.slingshotSprite.setPosition(anchorPoint.x - slingshotSprite.getWidth() / 2,
            anchorPoint.y - slingshotSprite.getHeight()/1.1f);

        // Define the attachment points for the slingshot strings dynamically
        float slingshotWidth = slingshotSprite.getWidth();
        float slingshotHeight = slingshotSprite.getHeight();

        // Adjusting attachment points to match the ends of the slingshot image
        this.leftAttachmentPoint = new Vector2(
            slingshotSprite.getX() + slingshotWidth * 0.25f,  // Left edge, 25% of width
            slingshotSprite.getY() + slingshotHeight * 0.85f  // Near top edge, 85% of height
        );

        this.rightAttachmentPoint = new Vector2(
            slingshotSprite.getX() + slingshotWidth * 0.75f,  // Right edge, 75% of width
            slingshotSprite.getY() + slingshotHeight * 0.85f  // Near top edge, 85% of height
        );

        //Create an anchor body (static, represents the fixed point of the slingshot)
//        BodyDef anchorDef = new BodyDef();
//        anchorDef.type = BodyDef.BodyType.StaticBody;
//        anchorDef.position.set(anchorPoint.x / 100, anchorPoint.y / 100); // Convert to Box2D meters
//        anchorBody = world.createBody(anchorDef);
//
//        // Add a fixture for the anchor (visual only, no collision)
//        CircleShape shape = new CircleShape();
//        shape.setRadius(0.1f); // Small radius for anchor visualization
//        anchorBody.createFixture(shape, 0);
//        shape.dispose();
        // Initialize ShapeRenderer for drawing the strings
        this.shapeRenderer = new ShapeRenderer();
    }



    public void setScale(float scaleX, float scaleY) {
        slingshotSprite.setSize(slingshotSprite.getWidth() * scaleX, slingshotSprite.getHeight() * scaleY);
        slingshotSprite.setOriginCenter();
        slingshotSprite.setPosition(anchorPoint.x - slingshotSprite.getWidth() /2,
            anchorPoint.y - slingshotSprite.getHeight()/1.1f );

        // Update the attachment points based on new size and position
        float slingshotWidth = slingshotSprite.getWidth();
        float slingshotHeight = slingshotSprite.getHeight();

        this.leftAttachmentPoint = new Vector2(
            slingshotSprite.getX() + slingshotWidth * 0.35f,  // Left edge, 25% of width
            slingshotSprite.getY() + slingshotHeight * 0.85f  // Near top edge, 85% of height
        );

        this.rightAttachmentPoint = new Vector2(
            slingshotSprite.getX() + slingshotWidth * 0.60f,  // Right edge, 75% of width
            slingshotSprite.getY() + slingshotHeight * 0.85f  // Near top edge, 85% of height
        );
    }


    public void attachBird(Body birdBody) {
        this.birdBody1 = birdBody;
    }

    public void startStretching(Vector2 touchPosition) {
        if (birdBody1 != null) {
            isStretching = true;
            stretchedPosition = touchPosition;
        }
    }

    public void updateStretching(Vector2 touchPosition) {
        if (isStretching) {
            // Calculate the stretch vector and limit it to the maximum stretch distance
            Vector2 stretchVector = touchPosition.sub(anchorPoint);
            if (stretchVector.len() > maxStretchDistance) {
                stretchVector.setLength(maxStretchDistance);
            }
            stretchedPosition = anchorPoint.cpy().add(stretchVector);
            stretchVector.y = stretchVector.y * 2f;

            // Align the bird's body with the stretched position
            birdBody1.setTransform(stretchedPosition.x / 100, stretchedPosition.y / 100, birdBody1.getAngle());
        }
    }

    public Vector2 calculateLaunchVelocity(float powerFactor) {
        // Calculate velocity based on the stretch vector and power factor
        if (stretchedPosition == null) return new Vector2(0, 0);

        Vector2 launchVelocity = anchorPoint.cpy().sub(stretchedPosition).scl(powerFactor);
        return launchVelocity;
    }

     //getposition
    public Vector2 getPosition() {
        return anchorPoint;
    }


    public void release() {
        if (isStretching && birdBody1 != null) {
            isStretching = false;
            // Calculate the stretch vector (stretchedPosition - anchorPoint)
            Vector2 stretchVector = stretchedPosition.cpy().sub(anchorPoint);


            // Invert the direction of the stretch vector
            stretchVector.scl(-1); // Reverse direction

            // Scale the stretch vector to calculate the launch force
            float adjustedPowerFactor = 0.05f; // Adjust to control launch speed
            Vector2 launchForce = stretchVector.scl(adjustedPowerFactor);

            // Apply a minimum velocity threshold
            float minVelocity = 1.0f; // Minimum velocity magnitude
            if (launchForce.len() < minVelocity) {
                launchForce.setLength(minVelocity);
            }

            // Apply the calculated launch force to the bird body
            birdBody1.applyLinearImpulse(launchForce, birdBody1.getWorldCenter(), true);
//            Timer.schedule(new Timer.Task() {
//                @Override
//                public void run() {
//                    if (birdBody1 != null) {
//                        world.destroyBody(birdBody1);
//                        birdBody1 = null;
//                    }
//                }
//            }, 5); // 5 seconds delay
            // Detach bird
//            birdBody1.destroyFixture(birdBody1.getFixtureList().first());
            birdBody1 = null;

        }
    }



    public void render(SpriteBatch batch) {
        // Draw the slingshot base sprite
        slingshotSprite.draw(batch);

        // If stretching, draw the slingshot strings
        if (isStretching && stretchedPosition != null) {
            batch.end(); // End SpriteBatch to use ShapeRenderer

            // Start ShapeRenderer with a filled rectangle for thicker lines
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0.3f, 0.2f, 0.1f, 1); // Brown color for the strings

            // Thickness of the string
            float stringThickness = 10f;

            // Draw the left string
            drawThickLine(shapeRenderer, leftAttachmentPoint, stretchedPosition, stringThickness);

            // Draw the right string
            drawThickLine(shapeRenderer, rightAttachmentPoint, stretchedPosition, stringThickness);

            shapeRenderer.end();
            batch.begin(); // Restart SpriteBatch
        }
    }

    // Helper method to draw a thick line using rectangles
    private void drawThickLine(ShapeRenderer renderer, Vector2 start, Vector2 end, float thickness) {
        Vector2 direction = new Vector2(end).sub(start).nor(); // Direction of the line
        Vector2 perpendicular = new Vector2(-direction.y, direction.x).scl(thickness / 2f); // Perpendicular vector

        // Compute the four corners of the rectangle
        Vector2 topLeft = new Vector2(start).add(perpendicular);
        Vector2 topRight = new Vector2(end).add(perpendicular);
        Vector2 bottomLeft = new Vector2(start).sub(perpendicular);
        Vector2 bottomRight = new Vector2(end).sub(perpendicular);

        // Draw the rectangle
        renderer.triangle(topLeft.x, topLeft.y, topRight.x, topRight.y, bottomLeft.x, bottomLeft.y);
        renderer.triangle(bottomLeft.x, bottomLeft.y, topRight.x, topRight.y, bottomRight.x, bottomRight.y);
    }

    public void dispose() {
        slingshotSprite.getTexture().dispose();
        shapeRenderer.dispose();
    }

    public boolean isStretching() {
        return isStretching;
    }

    public void setStretching(boolean isStretching) {
        this.isStretching = isStretching;
    }
}
