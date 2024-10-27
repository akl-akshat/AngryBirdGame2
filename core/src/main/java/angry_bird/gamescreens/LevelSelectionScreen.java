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
import static angry_bird.utils.Constants.UI.LevelScreen.BACKGROUND;

public class LevelSelectionScreen implements Screen {
    private static final int NUM_LEVELS = 9;
    private Texture backgroundImage;
    private Main main;
    private Stage stage;

    public LevelSelectionScreen(Main main){
        this.main = main;
        backgroundImage = new Texture(BACKGROUND);
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
        createLvlButtons(skin);
    }

    private void createLvlButtons(Skin skin) {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        for(int i=1; i<=NUM_LEVELS; i++){
            TextButton lvlButton = new TextButton(String.valueOf(i), skin);
            int lvlNum = i;

            lvlButton.addListener(event -> {
                if (lvlButton.isPressed()){
                    startLvl(lvlNum);
                }
                return true;
            });

            table.defaults().width(100).height(100).pad(100);
            table.add(lvlButton);

            if (i%3==0){
                table.row();
            }
        }
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

    public void startLvl(int lvlNum) {
        if (lvlNum == 1) {
            main.setScreen(new Level1Screen(main));
        } else {
            main.setScreen(new PlayingScreen(main)); // Default for other levels
        }
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
        stage.dispose();
        backgroundImage.dispose();
    }
}
