package angry_bird.gamescreens;

import angry_bird.main.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import static angry_bird.utils.Constants.UI.skin.BUTTON_SKIN;

public class GameOverScreen implements Screen {
    private Main appInstance;
    private Screen playScreen;
    private Stage gameOverStage;
    private SpriteBatch spriteBatch;
    private Texture backgroundTexture;

    public GameOverScreen(Main appInstance, Screen playScreen) {
        this.appInstance = appInstance;
        this.playScreen = playScreen;
        setupStage();
        configureButtons();

        // Initialize background texture and sprite batch
        spriteBatch = new SpriteBatch();
        backgroundTexture = new Texture("screens/Lose/Losescreen.jpg");
    }



    private void setupStage() {
        gameOverStage = new Stage(new ScreenViewport());
    }

    private void configureButtons() {
        Skin skin = new Skin(Gdx.files.internal(BUTTON_SKIN));

        Table buttonLayout = new Table();
        buttonLayout.setFillParent(true);
        gameOverStage.addActor(buttonLayout);

        buttonLayout.defaults().width(250).height(80).pad(21);

        TextButton restartBtn = new TextButton("RESTART LEVEL", skin);
        restartBtn.addListener(event -> {
            if (restartBtn.isPressed()) {
                if(playScreen instanceof Level1Screen){
                    appInstance.setScreen(new Level1Screen(appInstance, 0));
                }
                else if(playScreen instanceof Level2Screen){
                    appInstance.setScreen(new Level2Screen(appInstance, 0));
                }
                else if(playScreen instanceof Level3Screen){
                    appInstance.setScreen(new Level3Screen(appInstance, 0));
                }
            }
            return true;
        });
        buttonLayout.add(restartBtn);
        buttonLayout.row();

//        TextButton restartButton = new TextButton("RESTART LEVEL", buttonSkin);
//        buttonTable.add(restartButton);
//        if (gameScreen instanceof Level1Screen) {
//            restartButton.addListener(new ClickListener() {
//                @Override
//                public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
//                    gameInstance.setScreen(new Level1Screen(gameInstance));
//                }
//            });
//        } else if (gameScreen instanceof Level2Screen) {
//            restartButton.addListener(new ClickListener() {
//                @Override
//                public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
//                    gameInstance.setScreen(new Level2Screen(gameInstance));
//                }
//            });
//        } else if (gameScreen instanceof Level3Screen) {
//            restartButton.addListener(new ClickListener() {
//                @Override
//                public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
//                    gameInstance.setScreen(new Level3Screen(gameInstance));
//                }
//            });
//        }
//        buttonTable.row();

        TextButton levelMapBtn = new TextButton("Level Map", skin);
        levelMapBtn.addListener(event -> {
            if (levelMapBtn.isPressed()) {
                appInstance.setScreen(new Map_of_Levels(appInstance, 0));
            }
            return true;
        });
        buttonLayout.add(levelMapBtn);
        buttonLayout.row();

        TextButton mainMenuBtn = new TextButton("MAIN MENU", skin);
        mainMenuBtn.addListener(event -> {
            if (mainMenuBtn.isPressed()) {
                appInstance.setScreen(new MainMenuScreen(appInstance));
            }
            return true;
        });
        buttonLayout.add(mainMenuBtn);
    }

    @Override
    public void render(float delta) {
        // Draw fullscreen background
        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.end();

        // Render stage
        gameOverStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        gameOverStage.draw();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(gameOverStage);
    }

    @Override
    public void resize(int width, int height) {
        gameOverStage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        gameOverStage.dispose();
        spriteBatch.dispose();
        backgroundTexture.dispose();
    }
}
