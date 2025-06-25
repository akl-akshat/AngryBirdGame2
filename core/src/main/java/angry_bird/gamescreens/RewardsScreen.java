package angry_bird.gamescreens;

import angry_bird.main.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import static angry_bird.utils.Constants.UI.skin.BUTTON_SKIN;

public class RewardsScreen implements Screen {
    private Main main;
    private Stage stage;
    private Texture backgroundTexture;
    private Table rewardsTable;

    public RewardsScreen(Main main) {
        this.main = main;
        setupStage();
        setupBackground();
        setupRewardsMenu();
        setupButtons();
    }

    private void setupStage() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    private void setupButtons() {
        Skin skin = new Skin(Gdx.files.internal(BUTTON_SKIN));
        createBackButton(skin);
    }

    private void createBackButton(Skin skin) {
        TextButton backButton = new TextButton("<", skin);
        stage.addActor(backButton);
        backButton.setHeight(70);
        backButton.setWidth(70);
        backButton.setPosition(50, 950);
        backButton.addListener(event -> {
            if (backButton.isPressed()) {
                main.setScreen(new MainMenuScreen(main));
            }
            return true;
        });
    }

    private void setupBackground() {
        backgroundTexture = new Texture("screens/rewards/rewards.jpg"); // Set the correct path
    }

    private void setupRewardsMenu() {
        Skin skin = new Skin(Gdx.files.internal(BUTTON_SKIN));
        rewardsTable = new Table();
        rewardsTable.setFillParent(true);
        rewardsTable.center();

        TextButton collectButton = new TextButton("Collect Rewards", skin);

        rewardsTable.add(collectButton).pad(10).row();
        stage.addActor(rewardsTable);

        collectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showCollectedDialog();
            }
        });
    }

    private void showCollectedDialog() {
        Skin skin = new Skin(Gdx.files.internal(BUTTON_SKIN));

        // Create a dialog to show the reward collection confirmation
        Dialog collectedDialog = new Dialog("Reward Collected", skin);
        collectedDialog.text("Your reward has been collected!");

        // Add a "Close" button to dismiss the dialog
        collectedDialog.button("OK", true);

        // Display the dialog
        collectedDialog.show(stage);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        main.spriteBatch.begin();
        main.spriteBatch.draw(backgroundTexture, 0, 0);
        main.spriteBatch.end();
        stage.act();
        stage.draw();
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
    }
}
