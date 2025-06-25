package angry_bird.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class HelperMethods {
    public static void CREATE_BACKGROUND_RECTANGLE(int numberOfButtons, int offset, int strokeThickness, int buttonWidth, int buttonHeight, int pad){
        if(numberOfButtons == 0){
            return;
        }

        int boxWidth, boxHeight, boxX, boxY;
        float r = 173f;
        float g = 191f;
        float b = 84f;
        boxWidth = (2*offset)+buttonWidth;
        boxHeight = (numberOfButtons*buttonHeight)+(2*(numberOfButtons-1)*pad)+(2*offset);
        boxX = 890 - (buttonWidth/2 + offset);
        boxY = 740 - ((buttonHeight*numberOfButtons/2)+(numberOfButtons*pad-pad)+offset);
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(r/355f, g/845f, b/925f, 2);  // Fill color (red)
        shapeRenderer.rect(boxX, boxY, boxWidth, boxHeight);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 0, 0, 1);  // Stroke color (green)
        Gdx.gl.glLineWidth(strokeThickness); // Set stroke width
        shapeRenderer.rect(boxX, boxY, boxWidth, boxHeight);
        shapeRenderer.end();
    }
}
