package angry_bird.gamescreens;

import angry_bird.main.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import static angry_bird.utils.Constants.UI.skin.BUTTON_SKIN;
import static angry_bird.utils.HelperMethods.CREATE_BACKGROUND_RECTANGLE;

public class WinningScreen {
    private Main main;
    private PlayingScreen playingScreen;
    private Stage winningScreenStage;

    public WinningScreen(Main main, PlayingScreen playingScreen) {
        this.main = main;
        this.playingScreen = playingScreen;
        setupStageForWinningScreen();
        AddButtonsToWinningScreenStage();
    }

    private void setupStageForWinningScreen() {
        winningScreenStage = new Stage(new ScreenViewport());
    }

    private void AddButtonsToWinningScreenStage() {
        Skin skin = new Skin(Gdx.files.internal(BUTTON_SKIN));

        //create a table
        Table table = new Table();
        table.setFillParent(true);
        winningScreenStage.addActor(table);

        table.defaults().width(200).height(70).pad(18);

        //Return to Main menu Button
        TextButton MenuButton = new TextButton("MAIN MENU", skin);
        MenuButton.addListener(event -> {
            if(MenuButton.isPressed()){
                main.setScreen(new MainMenuScreen(main));
            }
            return true;
        });
        table.add(MenuButton);
        table.row();

        //return to level selection menu;
        TextButton lvlSelectButton = new TextButton("Select Level", skin);
        lvlSelectButton.addListener(event -> {
            if(lvlSelectButton.isPressed()){
                main.setScreen(new LevelSelectionScreen(main));
            }
            return true;
        });
        table.add(lvlSelectButton);
        table.row();

        //Next Level Button
        TextButton NextLevelButton = new TextButton("NEXT", skin);
        NextLevelButton.addListener(event -> {
            if(NextLevelButton.isPressed()){
                main.setScreen(new PlayingScreen(main));
            }
            return true;
        });
        table.add(NextLevelButton);
        table.row();

        //Replay Button
        TextButton ReplayButton = new TextButton("REPLAY", skin);
        ReplayButton.addListener(event -> {
            if(ReplayButton.isPressed()){
                //yet to implement the logic of restarting the level;
                main.setScreen(new PlayingScreen(main));
            }
            return true;
        });
        table.add(ReplayButton);
        table.row();

        //Quit Button
        TextButton quitButton = new TextButton("QUIT", skin);
        quitButton.addListener(event -> {
            if(quitButton.isPressed()){
                Gdx.app.exit();
            }
            return true;
        });
        table.add(quitButton);
    }

    private void drawBackground(){
        CREATE_BACKGROUND_RECTANGLE(5,50,5,200,70,18);
    }

    public void render(){
        drawBackground();
        winningScreenStage.act();
        winningScreenStage.draw();
    }

    public Stage getStage() {
        return winningScreenStage;
    }
}
