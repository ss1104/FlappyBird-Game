package Controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.flappybird.FlappyBirdMain;
import UI_and_Actions.StageAction;
import Assets.Bird;
import Assets.GroundBody;
import Assets.Pipes;
import helper.Backgrounds;
import helper.Info;

public class Gameplay implements Screen, ContactListener {

    private FlappyBirdMain flappyBirdMain;
    private OrthographicCamera deBugCamera;
    private Box2DDebugRenderer box2DDebugRenderer;
    private World world;
    private Bird bird;
    private GroundBody groundBody;
    private boolean hasStarted = false;
    private StageAction stageAction;
    private Sprite Background[] = new Sprite[2];
    private Sprite Ground[] = new Sprite[2];
    private Pipes pipe[] = new Pipes[3];
    private boolean ScoreTaken[] = new boolean[3];
    private Sound ScoreSound;

    public Gameplay(FlappyBirdMain flappyBirdMain) {
        this.flappyBirdMain = flappyBirdMain;

        deBugCamera = new OrthographicCamera();
        deBugCamera.setToOrtho(false,Info.getWidth()/Info.getPPM(),Info.getHeight()/Info.getPPM());
        // First parameter is false means (0,0) will Bottom left corner . If it is true then (0,0) will top right corner.
        deBugCamera.position.set(Info.getWidth()/2f,Info.getHeight()/2f,0);
        box2DDebugRenderer = new Box2DDebugRenderer();

        stageAction = new StageAction(flappyBirdMain);
        Gdx.input.setInputProcessor(stageAction.getStage()); // To make buttons working

        setBackground();
        setGround();

        world = new World(new Vector2(0,-9.81f),true);
        world.setContactListener(this);

        bird = new Bird(world,Info.getWidth()/2f - 100f, Info.getHeight()/2);
        CreatePipes();

        ScoreSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Score.mp3"));
        groundBody = new GroundBody(Ground[0],world);
        for(int i=0;i<ScoreTaken.length;i++) ScoreTaken[i] = false;

    }

    private void update(float delta){

        Start();
        OutOfBounds();

        if(bird.getIsAlive()) {
            moveBackground();
            moveGround();
            BirdFlap();
            UpdatePipes();
            MovePipes();
        }
        updateBird();
    }


    private void Start()
    {
        if(!hasStarted && Gdx.input.justTouched())
        {
            hasStarted = true;
            bird.ActivateBird();
        }
    }

    private void GameOver()
    {
        bird.setAlive(false);
        StopPipes();
        stageAction.getStage().clear();
        stageAction.displayScoreAfterGameOver();
        stageAction.createButtons();
        BirdDied();

        Preferences preferences = Gdx.app.getPreferences("HighScore");
        int HighScore = preferences.getInteger("Score");

        if(HighScore < stageAction.getScore())
        {
            HighScore = stageAction.getScore();
            preferences.putInteger("Score",HighScore);
            preferences.flush();
        }
    }


    private void BirdFlap(){

        if(Gdx.input.justTouched()){
            bird.BirdFlap();
        }
    }

    private void BirdDied(){
        bird.BirdDied();
    }

    private void updateBird()
    {
        bird.updateBirdPosition();
    }

    private void OutOfBounds()
    {
        if(bird.getY()+bird.getHeight()>=Info.getHeight()){
            GameOver();
        }

    }

    private void Draw()
    {
        drawBackground(flappyBirdMain.getBatch());
        drawGround(flappyBirdMain.getBatch());
        DrawPipes(flappyBirdMain.getBatch());
    }

    private void setBackground()
    {
        int index = Backgrounds.getRandomIndex();
        String s = Backgrounds.getBackgrounds()[index];
        for(int i=0;i<2;i++)
        {
            Background[i] = new Sprite((new Texture("Background/"+s)));
            Background[i].setPosition(i*Info.getWidth(),0);
        }
    }

    private void moveBackground()
    {
        for(int i=0;i<2;i++)
        {
            Background[i].setPosition(Background[i].getX()-2f,Background[i].getY());

            float x = Background[i].getX();
            if(x + Info.getWidth()  < 0f)
            {
                Background[i].setPosition(x + Info.getWidth()*Background.length,Background[i].getY());
            }
        }
    }

    private void drawBackground(SpriteBatch spriteBatch)
    {
        for(Sprite sprite : Background)
        {
            spriteBatch.draw(sprite,sprite.getX(),sprite.getY(),Info.getWidth(),Info.getHeight());
        }
    }


    private void setGround()
    {
        for(int i=0;i<2;i++)
        {
            Ground[i] = new Sprite((new Texture("Background/Ground.png")));
            Ground[i].setPosition(i*Info.getWidth(),0);
        }
    }

    private void moveGround()
    {
        for(int i=0;i<2;i++)
        {
            Ground[i].setPosition(Ground[i].getX()-3f,Ground[i].getY());

            float x = Ground[i].getX();
            if(x + Info.getWidth() < 0f)
            {
                Ground[i].setPosition(x + Info.getWidth()*Ground.length,Ground[i].getY());
            }
        }
    }

    private void drawGround(SpriteBatch spriteBatch)
    {
        for(Sprite sprite : Ground)
        {
            spriteBatch.draw(sprite,sprite.getX(),sprite.getY(),Info.getWidth(),Info.getHeight()/2f - 850f);
        }
    }

    private void CreatePipes()
    {
        for(int i=0;i<pipe.length;i++) {
            pipe[i] = new Pipes(world, Info.getWidth()+(float) (i + 1) * (Info.getHorizontal_distance()+86f));
        }
    }

    private void DrawPipes(SpriteBatch spriteBatch)
    {
        for(int i=0;i<pipe.length;i++){
            pipe[i].draw(spriteBatch);
        }

    }

    private void UpdatePipes()
    {
        for(int i=0;i<pipe.length;i++)
        {
            pipe[i].updatePipes();
            if (pipe[i].ScoreIncrement(bird.getX() + bird.getWidth()))
            {
                if(!ScoreTaken[i])
                {
                    ScoreSound.play();
                    stageAction.incrementScore();
                    ScoreTaken[i] = true;
                }

            }
        }
    }

    private void MovePipes()
    {
        for(int i=0;i<pipe.length;i++){
            pipe[i].movePipes();
            if(pipe[i].getX() + 2f*pipe[i].getWidth() + 20f < 0f)
            {
                ScoreTaken[i] = false;
                pipe[i].setPipeforOutOfBounds(pipe[(i-1+pipe.length)%pipe.length].getX());
            }
        }
    }

    private void StopPipes()
    {
        for(int i=0;i<pipe.length;i++){
            pipe[i].stopPipes();
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        update(delta);

        ScreenUtils.clear(1, 0, 0, 1);

        flappyBirdMain.getBatch().begin();
        Draw();
        bird.DrawIdleBird(flappyBirdMain.getBatch());
        flappyBirdMain.getBatch().end();

       //box2DDebugRenderer.render(world,deBugCamera.combined);

        flappyBirdMain.getBatch().setProjectionMatrix(stageAction.getStage().getCamera().combined);
        stageAction.getStage().draw();

        world.step(Gdx.graphics.getDeltaTime(),7,3);
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

        world.dispose();
        for(int i=0;i<Background.length;i++) Background[i].getTexture().dispose();
        for(int i=0;i<Ground.length;i++) Ground[i].getTexture().dispose();
        for(Pipes p:pipe) p.disposePipes();
        bird.getTexture().dispose();
        stageAction.getStage().dispose();
    }


    //.........Contact Listener .............//
    @Override
    public void beginContact(Contact contact) {

        Fixture fixture1,fixture2;
        if(contact.getFixtureA().getUserData()=="Bird")
        {
            fixture1 = contact.getFixtureA();
            fixture2 = contact.getFixtureB();
        }
        else
        {
            fixture1 = contact.getFixtureB();
            fixture2 = contact.getFixtureA();
        }

        if(fixture1.getUserData() == "Bird")
        {
            GameOver();
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}