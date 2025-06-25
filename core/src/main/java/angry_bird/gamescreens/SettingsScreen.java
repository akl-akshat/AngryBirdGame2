package angry_bird.gamescreens;

import angry_bird.main.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import static angry_bird.utils.Constants.UI.skin.BUTTON_SKIN;

public class SettingsScreen implements Screen {
    private final Main main;
    private Stage stage;
    private Texture backgroundTexture;
    private Table settingsTable;

    public SettingsScreen(Main main) {
        this.main = main;
        setupStage();
        setupBackground();
        setupSettingsMenu();
        setupButtons();
    }

    private void setupStage() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    private void setupBackground() {
        backgroundTexture = new Texture("screens/settings/settings.jpg"); // Set the correct path
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

    private void setupSettingsMenu() {
        Skin skin = new Skin(Gdx.files.internal(BUTTON_SKIN));
        settingsTable = new Table();
        settingsTable.setFillParent(true);
        settingsTable.center();

        TextButton volumeButton = new TextButton("Change Volume", skin);
        TextButton accountButton = new TextButton("Change Account", skin);

        settingsTable.add(volumeButton).pad(10).row();
        settingsTable.add(accountButton).pad(10).row();

        stage.addActor(settingsTable);

        volumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showVolumeDialog();
            }
        });

        accountButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showAccountDialog();
            }
        });
    }

    private void showVolumeDialog() {
        Skin skin = new Skin(Gdx.files.internal(BUTTON_SKIN));

        Dialog volumeDialog = new Dialog("Change Volume", skin);
        Slider volumeSlider = new Slider(0, 10, 1, false, skin);
        volumeSlider.setValue(5); // Default volume

        Label volumeLabel = new Label("Volume: " + (int) volumeSlider.getValue(), skin);

        volumeSlider.addListener(event -> {
            volumeLabel.setText("Volume: " + (int) volumeSlider.getValue());
            return true;
        });

        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(event -> {
            volumeDialog.hide();
            return true;
        });

        Table dialogTable = new Table();
        dialogTable.add(volumeLabel).padBottom(10).row();
        dialogTable.add(volumeSlider).padBottom(20).row();
        dialogTable.add(closeButton);

        volumeDialog.getContentTable().add(dialogTable);
        volumeDialog.show(stage);
    }

    private void showAccountDialog() {
        Skin skin = new Skin(Gdx.files.internal(BUTTON_SKIN));

        // Create a dialog
        Dialog accountDialog = new Dialog("Account Options", skin);
        accountDialog.setSize(400, 300); // Set explicit size
        accountDialog.setMovable(false); // Optional: prevent dialog movement

        // Create buttons
        TextButton logoutButton = new TextButton("Log Out", skin);
        TextButton closeButton = new TextButton("Close", skin);

        // Add functionality to buttons
        logoutButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Account", "Logged out"); // Replace with actual logout logic
                accountDialog.hide();
            }
        });

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                accountDialog.hide();
            }
        });

        // Add buttons to the dialog's content table
        Table dialogTable = new Table();
        dialogTable.add(logoutButton).pad(10).row();
        dialogTable.add(closeButton).pad(10);

        // Add the table to the dialog and show it
        accountDialog.getContentTable().add(dialogTable).pad(20);
        accountDialog.show(stage);

        // Enable debug to visualize layout (optional)
        dialogTable.debug();
        accountDialog.debug();
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
