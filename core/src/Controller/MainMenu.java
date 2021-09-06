package Controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.flappybird.FlappyBirdMain;
import UI_and_Actions.MainMenuButtons;
import helper.Backgrounds;
import helper.Info;

public class MainMenu implements Screen {

    private Texture img = new Texture("Background/Day.jpg");
    private FlappyBirdMain flappyBirdMain;
    private MainMenuButtons mainMenuButtons;

    public MainMenu(FlappyBirdMain flappyBirdMain) {
        this.flappyBirdMain = flappyBirdMain;
        mainMenuButtons = new MainMenuButtons(flappyBirdMain);
        Backgrounds.setRandomIndex();
        Gdx.input.setInputProcessor(mainMenuButtons.getStage());
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(1, 0, 0, 1);

        this.flappyBirdMain.getBatch().begin();
        this.flappyBirdMain.getBatch().draw(img,0,0, Info.getWidth(),Info.getHeight());
        this.flappyBirdMain.getBatch().end();

        flappyBirdMain.getBatch().setProjectionMatrix(mainMenuButtons.getStage().getCamera().combined);
        mainMenuButtons.getStage().draw();
    }

    @Override
    public void resize(int width, int height) {

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
        img.dispose();
        mainMenuButtons.getStage().dispose();
    }
}
