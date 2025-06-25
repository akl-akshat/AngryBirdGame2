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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import static angry_bird.utils.Constants.UI.skin.BUTTON_SKIN;

public class VictoryScreen implements Screen {
    private Main gameInstance;
    private Screen gameScreen;
    private Stage victoryStage;
    private SpriteBatch spriteBatch;
    private Texture backgroundTexture;

    public VictoryScreen(Main gameInstance, Screen gameScreen) {
        this.gameInstance = gameInstance;
        this.gameScreen = gameScreen;
        initializeStage();
        addButtonsToStage();

        // Initialize the background texture and sprite batch
        spriteBatch = new SpriteBatch();
        backgroundTexture = new Texture("screens/Victory/VictoryScreen1.jpg");
    }

    private void initializeStage() {
        victoryStage = new Stage(new ScreenViewport());
    }

    private void addButtonsToStage() {
        Skin buttonSkin = new Skin(Gdx.files.internal(BUTTON_SKIN));

        Table buttonTable = new Table();
        buttonTable.setFillParent(true);
        victoryStage.addActor(buttonTable);

        buttonTable.defaults().width(225).height(75).pad(21);

        TextButton mainMenuButton = new TextButton("MAIN MENU", buttonSkin);
        mainMenuButton.addListener(event -> {
            if (mainMenuButton.isPressed()) {
                gameInstance.setScreen(new MainMenuScreen(gameInstance));
            }
            return true;
        });
        buttonTable.add(mainMenuButton);
        buttonTable.row();

        TextButton levelSelectButton = new TextButton("Level Map", buttonSkin);
        levelSelectButton.addListener(event -> {
            if (levelSelectButton.isPressed()) {
                gameInstance.setScreen(new Map_of_Levels(gameInstance, 0));
            }
            return true;
        });
        buttonTable.add(levelSelectButton);
        buttonTable.row();

        TextButton nextLevelButton = new TextButton("NEXT LEVEL", buttonSkin);
        nextLevelButton.addListener(event -> {
            if (nextLevelButton.isPressed()) {
                if (gameScreen instanceof Level1Screen) {
                    gameInstance.setScreen(new Level2Screen(gameInstance, 0));
                } else if (gameScreen instanceof Level2Screen) {
                    gameInstance.setScreen(new Level3Screen(gameInstance, 0));
                } else if (gameScreen instanceof Level3Screen) {
                    gameInstance.setScreen(new Level1Screen(gameInstance, 0));
                }
            }
            return true;
        });
        buttonTable.add(nextLevelButton);
        buttonTable.row();

        TextButton restartButton = new TextButton("RESTART LEVEL", buttonSkin);
        buttonTable.add(restartButton);
        if (gameScreen instanceof Level1Screen) {
            restartButton.addListener(new ClickListener() {
                @Override
                public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                    gameInstance.setScreen(new Level1Screen(gameInstance, 0));
                }
            });
        } else if (gameScreen instanceof Level2Screen) {
            restartButton.addListener(new ClickListener() {
                @Override
                public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                    gameInstance.setScreen(new Level2Screen(gameInstance, 0));
                }
            });
        } else if (gameScreen instanceof Level3Screen) {
            restartButton.addListener(new ClickListener() {
                @Override
                public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                    gameInstance.setScreen(new Level3Screen(gameInstance, 0));
                }
            });
        }
        buttonTable.row();
    }

    @Override
    public void render(float delta) {
        // Draw the fullscreen background
        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.end();

        // Render the stage
        victoryStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        victoryStage.draw();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(victoryStage);
    }

    @Override
    public void resize(int width, int height) {
        victoryStage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        victoryStage.dispose();
        spriteBatch.dispose();
        backgroundTexture.dispose();
    }
}
