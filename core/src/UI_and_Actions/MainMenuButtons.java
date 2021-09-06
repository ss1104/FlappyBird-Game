package UI_and_Actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
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
import helper.BirdColors;
import helper.Info;

public class MainMenuButtons  {

    private FlappyBirdMain flappyBirdMain;
    private Stage stage;
    private Viewport viewport;
    private Label label,title,birdcolor;

    private ImageButton start,showscore,BirdColor;

    public MainMenuButtons(FlappyBirdMain flappyBirdMain) {
        this.flappyBirdMain = flappyBirdMain;

        viewport = new FitViewport(Info.getWidth(),Info.getHeight(),new OrthographicCamera());
        stage = new Stage(viewport,flappyBirdMain.getBatch());

        createButtons();
        showTitle();
        setBirdColorTitle();
    }

    public Stage getStage() {
        return stage;
    }

    private void createButtons()
    {
        start = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Play.png"))));
        showscore = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Score.png"))));
        BirdColor = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Birds/"+BirdColors.getColor()[BirdColors.getIndex()]+"/Idle.png"))));

        start.setPosition(Info.getWidth()/2f-200f,Info.getHeight()/2f, Align.center);
        showscore.setPosition(Info.getWidth()/2f+200f,Info.getHeight()/2f,Align.center);
        BirdColor.setPosition(Info.getWidth()/2f,Info.getHeight()/2f+300f,Align.center);

        start.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                flappyBirdMain.setScreen(new Gameplay(flappyBirdMain));
                stage.dispose();
            }
        });

        showscore.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                    ShowScore();
            }
        });

        BirdColor.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                BirdColors.increment();
                changeColor();
            }
        });

        stage.addActor(start);
        stage.addActor(showscore);
        stage.addActor(BirdColor);
    }

    private void changeColor()
    {
        if(BirdColor!=null) BirdColor.remove();
        BirdColor = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Birds/"+BirdColors.getColor()[BirdColors.getIndex()]+"/Idle.png"))));
        BirdColor.setPosition(Info.getWidth()/2f,Info.getHeight()/2f+300f,Align.center);
        stage.addActor(BirdColor);
        BirdColor.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                BirdColors.increment();
                changeColor();
            }
        });
    }

    private void ShowScore()
    {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Font/MomcakeBold-WyonA.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 120;
        BitmapFont font = generator.generateFont(parameter);

        Preferences preferences = Gdx.app.getPreferences("HighScore");
        String s = String.valueOf(preferences.getInteger("Score"));
        label = new Label("HighScore   " + s,new Label.LabelStyle(font, Color.RED));
        label.setPosition(Info.getWidth()/2f, Info.getHeight()/2f - label.getHeight()/2f - 100f,Align.center);
        stage.addActor(label);
    }

    private void showTitle(){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Font/04b_19.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 190;
        BitmapFont font = generator.generateFont(parameter);

        title = new Label("Flappy Bird",new Label.LabelStyle(font, Color.WHITE));
        title.setPosition(Info.getWidth()/2f, Info.getHeight()-200f,Align.center);
        stage.addActor(title);
    }

    private void setBirdColorTitle(){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Font/MomcakeBold-WyonA.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        BitmapFont font = generator.generateFont(parameter);

        title = new Label("Tap on Bird to change its color",new Label.LabelStyle(font, Color.MAROON));
        title.setPosition(Info.getWidth()/2f, BirdColor.getY() + 100f,Align.center);
        stage.addActor(title);
    }
}
