package angry_bird.main;

import angry_bird.gamescreens.*;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public SpriteBatch spriteBatch;

    public MainMenuScreen mainMenuScreen;
    public GamesaveScreen gamesaveScreen;
    public Map_of_Levels map_of_Levels;
    private Music backgroundMusic;
    @Override
    public void create() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/background.mp3"));
        spriteBatch = new SpriteBatch();
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
        initialiseScreens();
        this.setScreen(new MainMenuScreen(this));
    }

    private void initialiseScreens(){
//        mainMenuScreen = new MainMenuScreen(this);
        gamesaveScreen = new GamesaveScreen(this);
        map_of_Levels = new Map_of_Levels(this, 0);
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
