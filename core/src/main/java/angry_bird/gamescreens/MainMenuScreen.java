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

import static angry_bird.utils.Constants.UI.skin.BUTTON_SKIN;
import static angry_bird.utils.Constants.UI.MenuScreen.*;

public class MainMenuScreen implements Screen {
    private Main main;
    private Texture backgroundImage;
    private Stage stage;
    private TextButton playButton;
    private TextButton quitButton;

    public MainMenuScreen(Main main){
        this.main = main;
        setupStageForMenuButtons();
        setupBackgroundImageAndButtons();
    }

    private void setupStageForMenuButtons(){
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    private void setupBackgroundImageAndButtons(){
        backgroundImage = new Texture(BACKGROUND);
        Skin skin = new Skin(Gdx.files.internal(BUTTON_SKIN));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.defaults().width(200).height(70).padTop(900).padBottom(100).padLeft(100).padRight(100);

        playButton = new TextButton("PLAY",skin);
        quitButton = new TextButton("QUIT", skin);

        table.add(playButton);
        table.add(quitButton);

        addListenerForButtons();
    }

    private void addListenerForButtons() {
        playButton.addListener(event -> {
            if(playButton.isPressed()){
                main.setScreen(new GamesaveScreen(main));
            }
            return true;
        });

        quitButton.addListener(event -> {
            if(quitButton.isPressed()){
                Gdx.app.exit();
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
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
        backgroundImage.dispose();
    }
}
