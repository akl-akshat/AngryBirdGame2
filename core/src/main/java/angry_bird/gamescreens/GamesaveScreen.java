package angry_bird.gamescreens;

import angry_bird.main.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import static angry_bird.utils.Constants.UI.Gamesave.*;
import static angry_bird.utils.Constants.UI.skin.BUTTON_SKIN;

import com.badlogic.gdx.utils.Logger;

public class GamesaveScreen implements Screen {
    private static final Logger logger = new Logger(GamesaveScreen.class.getName(), Logger.DEBUG);
    private Main mainApp;
    private Texture backgroundTexture;
    private Stage uiStage;
    private TextButton newGameButton;
    private TextButton loadGameButton;
    private TextButton backButton;
    private int a;

    public GamesaveScreen(Main mainApp){
        this.mainApp = mainApp;
        initializeStage();
        initializeUIComponents();
    }

    private void initializeStage(){
        uiStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(uiStage);
    }

    private void initializeUIComponents() {
        backgroundTexture = new Texture(BACKGROUND);
        Skin skin = new Skin(Gdx.files.internal(BUTTON_SKIN));

        newGameButton = new TextButton("NEW GAME", skin);
        loadGameButton = new TextButton("LOAD GAME", skin);
        backButton = new TextButton("<", skin);

        // Set button sizes
        newGameButton.setSize(200, 100);
        loadGameButton.setSize(200, 100);
        backButton.setSize(70, 70);

        // Set button positions
        newGameButton.setPosition(500, 450);
        loadGameButton.setPosition(1200, 450);
        backButton.setPosition(50, 950);

        // Add buttons to the stage
        uiStage.addActor(newGameButton);
        uiStage.addActor(loadGameButton);
        uiStage.addActor(backButton);

        addEventListeners();
    }

    private void addEventListeners() {
        newGameButton.addListener(event -> {
            if(newGameButton.isPressed()){
                a=0;
                try {
                    mainApp.setScreen(new Map_of_Levels(mainApp, a));
                } catch (Exception e) {
                    logger.error("Error transitioning to Map_of_Levels screen", e);
                }
            }
            return true;
        });

        loadGameButton.addListener(event -> {
            if(loadGameButton.isPressed()){
                a=1;
                try {
                    mainApp.setScreen(new Map_of_Levels(mainApp, 1));
                } catch (Exception e) {
                    logger.error("Error transitioning to Map_of_Levels screen", e);
                }
            }
            return true;
        });

        backButton.addListener(event -> {
            if (backButton.isPressed()){
                try {
                    mainApp.setScreen(new MainMenuScreen(mainApp));
                } catch (Exception e) {
                    logger.error("Error transitioning to MainMenuScreen", e);
                }
            }
            return true;
        });
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mainApp.spriteBatch.begin();
        mainApp.spriteBatch.draw(backgroundTexture, 0, 0);
        mainApp.spriteBatch.end();

        uiStage.act();
        uiStage.draw();
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
        uiStage.dispose();
        backgroundTexture.dispose();
    }
}
