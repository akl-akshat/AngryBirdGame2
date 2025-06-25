package angry_bird.gamescreens;

import angry_bird.main.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import static angry_bird.utils.Constants.UI.skin.BUTTON_SKIN;
import static angry_bird.utils.Constants.UI.MenuScreen.*;

public class MainMenuScreen implements Screen {
    private Main mainApp;
    private Texture backgroundTexture;
    private Stage mainStage;

    private TextButton playButton;
    private TextButton exitButton;
    private TextButton settingsButton;
    private TextButton rewardsButton;
//    private Music backgroundMusic;
    public MainMenuScreen(Main mainApp) {
        this.mainApp = mainApp;
//        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/background.mp3"));
        initializeStage();
        initializeBackground();
        initializeButtons();
        // Start background music
//        backgroundMusic.setLooping(true);
//        backgroundMusic.play();
        System.out.println("I am background");
        setupButtonListeners();
    }

    private void initializeStage() {
        mainStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(mainStage);
    }

    private void initializeBackground() {
        backgroundTexture = new Texture(BACKGROUND);
    }

    private void initializeButtons() {
        Skin buttonSkin = new Skin(Gdx.files.internal(BUTTON_SKIN));
        Table buttonTable = new Table();
        buttonTable.setFillParent(true);
        mainStage.addActor(buttonTable);
        buttonTable.defaults().width(200).height(70).pad(10);

        playButton = new TextButton("PLAY", buttonSkin);
        settingsButton = new TextButton("SETTINGS", buttonSkin);
        rewardsButton = new TextButton("REWARDS", buttonSkin);
        exitButton = new TextButton("EXIT", buttonSkin);

        buttonTable.add(playButton).row();
        buttonTable.add(settingsButton).row();
        buttonTable.add(rewardsButton).row();
        buttonTable.add(exitButton).row();
    }

    private void setupButtonListeners() {
        playButton.addListener(event -> {
            if (playButton.isPressed()) {
                mainApp.setScreen(new GamesaveScreen(mainApp));
            }
            return true;
        });

        settingsButton.addListener(event -> {
            if (settingsButton.isPressed()) {
                mainApp.setScreen(new SettingsScreen(mainApp));
            }
            return true;
        });

        rewardsButton.addListener(event -> {
            if (rewardsButton.isPressed()) {
                mainApp.setScreen(new RewardsScreen(mainApp));
            }
            return true;
        });

        exitButton.addListener(event -> {
            if (exitButton.isPressed()) {
                Gdx.app.exit();
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

        mainStage.act();
        mainStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        mainStage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        mainStage.dispose();
        backgroundTexture.dispose();
//        backgroundMusic.dispose();
    }
}
