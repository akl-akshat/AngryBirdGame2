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

import static angry_bird.utils.Constants.UI.Gamesave.*;
import static angry_bird.utils.Constants.UI.skin.BUTTON_SKIN;

public class GamesaveScreen implements Screen {
    private Main main;

    private Texture backgroundImage;
    private Stage stage;

    private TextButton newGame;
    private TextButton loadGame;
    private TextButton backButton;

    public GamesaveScreen(Main main){
        this.main = main;
        setupStage();
        setupBackgroundImageAndButtons();
    }

    private void setupStage(){
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    private void setupBackgroundImageAndButtons() {
        backgroundImage = new Texture(BACKGROUND);

        Skin skin = new Skin(Gdx.files.internal(BUTTON_SKIN));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.defaults().width(200).height(70).pad(100);

        newGame = new TextButton("NEW GAME", skin);
        loadGame = new TextButton("LOAD GAME", skin);

        table.add(newGame);
        table.add(loadGame);

        createBackButton(skin);

        addListenerForButtons();
    }

    private void createBackButton(Skin skin) {
        this.backButton = new TextButton("<", skin);
        stage.addActor(backButton);
        backButton.setHeight(70);
        backButton.setWidth(70);
        backButton.setPosition(50,950);
    }

    private void addListenerForButtons() {
        newGame.addListener(event -> {
            if(newGame.isPressed()){
                main.setScreen(new LevelSelectionScreen(main));
            }
            return true;
        });

        loadGame.addListener(event -> {
            if(loadGame.isPressed()){
                main.setScreen(new LevelSelectionScreen(main));
            }
            return true;
        });

        backButton.addListener(event -> {
            if (backButton.isPressed()){
                main.setScreen(new MainMenuScreen(main));
            }
            return true;
        });
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        main.spriteBatch.begin();
        main.spriteBatch.draw(backgroundImage, 0, 0);
        main.spriteBatch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
