package UI_and_Actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.flappybird.FlappyBirdMain;
import Controller.Gameplay;
import Controller.MainMenu;
import helper.Info;

public class StageAction
{
    private FlappyBirdMain flappyBirdMain;
    private Stage stage;
    private Viewport viewport;
    private int score = 0;
    private Label label;
    private ImageButton restart,quit;

    public StageAction(FlappyBirdMain flappyBirdMain) {
        this.flappyBirdMain = flappyBirdMain;

        viewport = new FitViewport(Info.getWidth(),Info.getHeight(),new OrthographicCamera());
        stage = new Stage(viewport,flappyBirdMain.getBatch());

        createScoreLabel();
    }

    public Stage getStage() {
        return stage;
    }

    private void createScoreLabel()
    {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Font/04b_19.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 200;
        BitmapFont font = generator.generateFont(parameter);
        label = new Label(String.valueOf(score),new Label.LabelStyle(font,Color.WHITE));
        label.setPosition(Info.getWidth()/2f - label.getWidth()/2f, Info.getHeight()-label.getHeight()-20f);
        stage.addActor(label);
    }

    public void createButtons()
    {
        restart = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Retry.png"))));
        quit = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Quit.png"))));

        restart.setPosition(Info.getWidth()/2f-200f,Info.getHeight()/2f, Align.center);
        quit.setPosition(Info.getWidth()/2f+200f,Info.getHeight()/2f,Align.center);

        restart.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                  flappyBirdMain.setScreen(new Gameplay(flappyBirdMain));
                  stage.dispose();
            }
        });

        quit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                 flappyBirdMain.setScreen(new MainMenu(flappyBirdMain));
                 stage.dispose();
            }
        });

        stage.addActor(restart);
        stage.addActor(quit);
    }

    public void incrementScore()
    {
        score++;
        label.setText(String.valueOf(score));
    }

    public void displayScoreAfterGameOver()
    {
        label.setText(String.valueOf(score));
        stage.addActor(label);
    }

    public int getScore() {
        return score;
    }
}
