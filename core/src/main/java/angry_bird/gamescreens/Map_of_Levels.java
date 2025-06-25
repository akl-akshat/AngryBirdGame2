package angry_bird.gamescreens;

import angry_bird.main.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import static angry_bird.utils.Constants.UI.skin.BUTTON_SKIN;
import static angry_bird.utils.Constants.UI.LevelScreen.BACKGROUND;

public class Map_of_Levels implements Screen {
    private Texture backgroundImage;
    private Main main;
    int load;
    private Stage stage;
    private ShapeRenderer shapeRenderer;

    public Map_of_Levels(Main main, int load){
        this.main = main;
        this.load = load;
        backgroundImage = new Texture(BACKGROUND);
        shapeRenderer = new ShapeRenderer();
        setupStage();
        setupButtons();
    }

    private void setupStage(){
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    private void setupButtons(){
        Skin skin = new Skin(Gdx.files.internal(BUTTON_SKIN));
        createBackButton(skin);
        createLevelButtons(skin);
    }

    private void createLevelButtons(Skin skin) {
        Table table = new Table();
        table.setFillParent(true);

        int level = 1;
        float x = 100, y = 500;
        float xOffset = 300, yOffset = 150;

        for (int i = 1; i <= 100; i++) { // Example for 100 levels
            TextButton levelButton = new TextButton(String.valueOf(level), skin);
            int levelNum = level;

            levelButton.setPosition(x, y);
            levelButton.addListener(event -> {
                if (levelButton.isPressed()) {
                    startLevel(levelNum);
                }
                return true;
            });

            table.addActor(levelButton);

            if (i % 2 == 0) {
                y -= yOffset;
            } else {
                y += yOffset;
            }
            x += xOffset;
            level++;
        }

        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setFillParent(true);
        stage.addActor(scrollPane);
    }



    public void startLevel(int levelNum) {
        if (levelNum == 1) {
            main.setScreen(new Level1Screen(main, load));
        } else if (levelNum == 2){
            main.setScreen(new Level2Screen(main, load)); // Default for other levels
        } else{
            main.setScreen(new Level3Screen(main, load));
        }
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        main.spriteBatch.begin();
        main.spriteBatch.draw(backgroundImage, 0, 0);
        main.spriteBatch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.ORANGE);
        drawCurvyLines();
        shapeRenderer.end();

        stage.act();
        stage.draw();
    }

    private void drawCurvyLines() {
        float x = 100, y = 500;
        float xOffset = 300, yOffset = 150;

        for (int i = 1; i < 100; i++) { // Example for 100 levels
            float nextX = x + xOffset;
            float nextY = (i % 2 == 0) ? y - yOffset : y + yOffset;

            Vector2 start = new Vector2(x + 100, y + 35);
            Vector2 end = new Vector2(nextX + 100, nextY + 35);
            Vector2 control1 = new Vector2((x + nextX) / 2, y + 100);
            Vector2 control2 = new Vector2((x + nextX) / 2, nextY - 100);

            Bezier<Vector2> bezier = new Bezier<>(start, control1, control2, end);
            drawBezierCurve(bezier);

            x = nextX;
            y = nextY;
        }
    }

    private void drawBezierCurve(Bezier<Vector2> bezier) {
        Vector2 out = new Vector2();
        Vector2 prev = new Vector2();
        bezier.valueAt(prev, 0);
        float thickness = 10.0f; // Adjust the thickness as needed

        for (float t = 0.01f; t <= 1; t += 0.01) {
            bezier.valueAt(out, t);
            for (float offset = -thickness; offset <= thickness; offset += 1.0f) {
                shapeRenderer.line(prev.x + offset, prev.y, out.x + offset, out.y);
                shapeRenderer.line(prev.x, prev.y + offset, out.x, out.y + offset);
            }
            prev.set(out);
        }
    }

    private void createBackButton(Skin skin) {
        TextButton backButton = new TextButton("<", skin);
        stage.addActor(backButton);
        backButton.setHeight(70);
        backButton.setWidth(70);
        backButton.setPosition(50, 950);
        backButton.addListener(event -> {
            if (backButton.isPressed()) {
                main.setScreen(new GamesaveScreen(main));
            }
            return true;
        });
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        backgroundImage.dispose();
        shapeRenderer.dispose();
    }
}
