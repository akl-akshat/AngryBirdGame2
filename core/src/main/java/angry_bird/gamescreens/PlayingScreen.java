package angry_bird.gamescreens;

import angry_bird.main.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import static angry_bird.utils.Constants.UI.PauseMenu.*;
import static angry_bird.utils.Constants.UI.skin.BUTTON_SKIN;

public class PlayingScreen implements Screen {
    private Main main;

    private PauseMenu pauseMenu;
    private WinningScreen winningScreen;
    private LoosingScreen loosingScreen;

    private Texture bgtemp;

    private boolean paused, won, loss;

    public PlayingScreen(Main main){
        this.main = main;
        bgtemp = new Texture("gameplay.png");
        pauseMenu = new PauseMenu(main, this);
        winningScreen = new WinningScreen(main, this);
        loosingScreen = new LoosingScreen(main, this);

        paused = false; won = false; loss = false;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.W)){
            won = !won;
            if(won){
                Gdx.input.setInputProcessor(winningScreen.getStage());
            }
            else{
                Gdx.input.setInputProcessor(null);
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.L)){
            loss = !loss;
            if(loss){
                Gdx.input.setInputProcessor(loosingScreen.getStage());
            }
            else{
                Gdx.input.setInputProcessor(null);
            }
        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            paused = !paused;
            if (paused) {
                Gdx.input.setInputProcessor(pauseMenu.getStage());  // Set the stage to receive input
            } else {
                Gdx.input.setInputProcessor(null);  // Set input processor back to normal
            }
        }

//        if (!paused) {
//            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//            main.spriteBatch.begin();
//            main.spriteBatch.draw(bgtemp, 0, 0);
//            main.spriteBatch.end();
//        }
//        else {
//            pauseMenu.render();
//        }

        if(paused){
            pauseMenu.render();
        }
        else if(won){
            winningScreen.render();
        }
        else if(loss){
            loosingScreen.render();
        }
        else{
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            main.spriteBatch.begin();
            main.spriteBatch.draw(bgtemp, 0, 0);
            main.spriteBatch.end();
        }

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

    public boolean isPaused() {
        return paused;
    }
    public void setPaused(boolean status){
        this.paused = status;
    }
}
