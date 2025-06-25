package angry_bird.gamescreens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.List;
public class ProjectilePathSimulator {
    private static final float PPM = 100; // Pixels per meter
    private static final float TIME_STEP = 0.00005f; // Smaller time step for smoother curve
    private static final float DOT_RADIUS = 4f; // Radius for each dot in the path
    private static final int MAX_PATH_POINTS = 100; // Max path points for the simulation
    private ShapeRenderer shapeRenderer; // For rendering the path
    private List<Vector2> pathPoints; // List to store path points

    public ProjectilePathSimulator() {
        shapeRenderer = new ShapeRenderer(); // Initialize ShapeRenderer
        pathPoints = new ArrayList<>(); // Initialize path points list
    }

    public void calculatePath(Vector2 startPosition, Vector2 launchVelocity, float gravity, float timeStep) {
        pathPoints.clear();
        Vector2 position = new Vector2(startPosition);
        Vector2 velocity = new Vector2(launchVelocity);

        for (int i = 0; i < MAX_PATH_POINTS; i++) { // Simulate for MAX_PATH_POINTS steps
            position.add(velocity.x * timeStep, velocity.y * timeStep);
            velocity.y += gravity * timeStep;
            pathPoints.add(new Vector2(position));

            // Stop if the projectile hits the ground (y <= 0)
            if (position.y <= 0) {
                break;
            }
        }
    }

    public void renderPath(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED); // Set color for path

        for (int i = 0; i < pathPoints.size() - 1; i++) {
            Vector2 startPoint = pathPoints.get(i);
            Vector2 endPoint = pathPoints.get(i + 1);

            // Draw a dotted line by creating segments
            float segmentLength = 10f; // Length of each segment
            float distance = startPoint.dst(endPoint); // Get the distance between points
            float numSegments = distance / segmentLength;

            for (int j = 0; j < numSegments; j++) {
                float t = j / numSegments;
                Vector2 segmentStart = new Vector2(
                    startPoint.x + t * (endPoint.x - startPoint.x),
                    startPoint.y + t * (endPoint.y - startPoint.y)
                );
                Vector2 segmentEnd = new Vector2(
                    startPoint.x + (t + 1 / numSegments) * (endPoint.x - startPoint.x),
                    startPoint.y + (t + 1 / numSegments) * (endPoint.y - startPoint.y)
                );
                shapeRenderer.line(segmentStart, segmentEnd); // Draw segment
            }
        }

        shapeRenderer.end();
    }

    /**
     * Calculate and render the projectile path based on initial conditions.
     *
     * @param startPosition   Initial position of the projectile (world coordinates).
     * @param launchVelocity  Initial velocity of the projectile.
     * @param gravity         Gravity applied to the projectile.
     * @param maxTime         Maximum time to simulate the path.
     */
    public void calculateAndRenderPath(Vector2 startPosition, Vector2 launchVelocity, float gravity, float maxTime) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED); // Set path color to red

        Vector2 position = new Vector2(startPosition);
        Vector2 velocity = new Vector2(launchVelocity);

        float t = 0f; // Time elapsed
        while (t <= maxTime) {
            position.set(
                startPosition.x + velocity.x * t,
                startPosition.y + velocity.y * t + 0.5f * gravity * t * t
            );

            // Check if the position is out of screen bounds
            if (position.x * PPM > Gdx.graphics.getWidth() || position.y * PPM > Gdx.graphics.getHeight()) {
                break; // Stop rendering if out of bounds
            }

            // Stop if the projectile hits the ground
            if (position.y <= 0) break;

            // Draw the path as red dots
            shapeRenderer.circle(position.x * PPM, position.y * PPM, DOT_RADIUS);

            t += TIME_STEP; // Increment time
        }

        shapeRenderer.end();
    }

    /**
     * Dispose of the ShapeRenderer when no longer needed.
     */
    public void dispose() {
        shapeRenderer.dispose();
    }
}
