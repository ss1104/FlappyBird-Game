package Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import helper.BirdColors;
import helper.Info;

public class Bird extends Sprite
{
    private World world; // Refers to Physical World (Where real physics acts)
    private Body body; // Refers to Physical Body exists in Physical World
    private Fixture fixture; // Defines the properties of body like shape,density,mass etc.
    private boolean isAlive;
    private int FlyingPosition = 1;
    private Sound BirdDieSound,BirdFlapSound;
    private boolean DeadSound = false;


    public Bird(World world,float x,float y) {
        super(new Texture("Birds/"+ BirdColors.getColor()[BirdColors.getIndex()]+"/Idle.png"));
        this.setPosition(x,y); // or just setPosition(x,y)
        this.world = world;
        isAlive = false;

        createBody();
        BirdDieSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/hit.mp3"));
        BirdFlapSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/wing.mp3"));
    }

    private void createBody()
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX()/ Info.getPPM(),getY()/Info.getPPM()); //Scaling down the position of by in 1:PPM ratio as , Physics act on body.
        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius((getHeight()/1f)/Info.getPPM());

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixture = body.createFixture(fixtureDef);
        fixture.setUserData("Bird");

        shape.dispose();
        body.setActive(false);
    }

    public void ActivateBird()
    {
        isAlive = true;
        body.setActive(true);
    }

    public void BirdFlap()
    {
        body.setLinearVelocity(0f,5f);
        BirdFlapSound.play();
        if(FlyingPosition == 1)
        {
            FlyingPosition = 2;
            this.setTexture(new Texture("Birds/"+ BirdColors.getColor()[BirdColors.getIndex()]+"/2.png"));
        }
        else
        {
            FlyingPosition = 1;
            this.setTexture(new Texture("Birds/"+ BirdColors.getColor()[BirdColors.getIndex()]+"/3.png"));
        }
    }

    public void DrawIdleBird(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(this,getX()-50f , getY()-45f,2f*getWidth(),2f*getHeight());
    }

    public void updateBirdPosition() //PPM is being multiplied because it is managing the position of an IMAGE not the BODY.
    {
        this.setPosition(body.getPosition().x*Info.getPPM(),body.getPosition().y*Info.getPPM());//IMAGE has no physical effect but the body have.
    }

    public boolean getIsAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public void BirdDied(){
        this.setTexture(new Texture("Birds/"+ BirdColors.getColor()[BirdColors.getIndex()]+"/Dead.png"));
        if(!DeadSound) {
            BirdDieSound.play();
            DeadSound = true;
        }
    }

}
