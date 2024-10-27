package angry_bird.main;

import angry_bird.gamescreens.*;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public SpriteBatch spriteBatch;

    public MainMenuScreen mainMenuScreen;
    public GamesaveScreen gamesaveScreen;
    public LevelSelectionScreen levelSelectionScreen;
    public PlayingScreen playingScreen;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        initialiseScreens();
        this.setScreen(new MainMenuScreen(this));
    }

    private void initialiseScreens(){
        mainMenuScreen = new MainMenuScreen(this);
        gamesaveScreen = new GamesaveScreen(this);
        levelSelectionScreen = new LevelSelectionScreen(this);
        playingScreen = new PlayingScreen(this);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        getScreen().dispose();
    }
}
