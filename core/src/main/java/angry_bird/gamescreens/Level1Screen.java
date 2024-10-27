package angry_bird.gamescreens;

import angry_bird.main.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import static angry_bird.utils.Constants.UI.skin.BUTTON_SKIN;

public class Level1Screen implements Screen {
    private Main main;
    private Stage stage;
    private Texture backgroundTexture;
    private Texture birdTexture;
    private Texture pigTexture;
    private Texture blockTexture;
    private Image birdImage;
    private Image pigImage;
    private Image blockImage;

    public Level1Screen(Main main) {
        this.main = main;
        setupStage();
        setupTextures();
        setupButtons();
    }

    private void setupStage() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    private void setupTextures() {
        backgroundTexture = new Texture("C:\\Users\\UTKARSH\\Downloads\\angybirb\\angybirb\\assets\\screens\\level_1bg\\level1.png"); // Update with your actual path

    }

    @Override
    public void show() {
        // Initialize SpriteBatch and the background image texture
        //batch = new SpriteBatch();
        //background = new Texture(Gdx.files.internal("C:\\Users\\UTKARSH\\Downloads\\AGB\\AGB\\assets\\screens\\level_1bg\\level1.png"));
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        main.spriteBatch.begin();
        main.spriteBatch.draw(backgroundTexture, 0, 0);
        main.spriteBatch.end();

        stage.act();
        stage.draw();
    }

    private void createBackButton(Skin skin) {
        TextButton backButton = new TextButton("<", skin);
        stage.addActor(backButton);
        backButton.setHeight(70);
        backButton.setWidth(70);
        backButton.setPosition(50,950);
        backButton.addListener(event -> {
            if (backButton.isPressed()){
                main.setScreen(new GamesaveScreen(main));
            }
            return true;
        });
    }
    private void setupButtons(){
        Skin skin = new Skin(Gdx.files.internal(BUTTON_SKIN));
        createBackButton(skin);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        birdTexture.dispose();
        pigTexture.dispose();
        blockTexture.dispose();
    }
}
