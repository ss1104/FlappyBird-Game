package Assets;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import java.util.Random;
import helper.Info;

public class Pipes
{
    private World world;
    private Body body1,body2;
    private Sprite pipe1,pipe2;
    private Fixture fixture1,fixture2;
    private Random random = new Random();
    private OrthographicCamera camera;

    public Pipes(World world,float x) {
        this.world = world;
        //this.camera = camera;

        pipe1 = new Sprite(new Texture("Pipes/Pipe 1.png"));
        pipe2 = new Sprite(new Texture("Pipes/Pipe 2.png"));

        pipe1.setPosition(x,Ycoordinate());
        pipe2.setPosition(x,Info.getHeight()/2f - 860f);

        createBody();
    }

    private void createBody()
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;

        bodyDef.position.set(pipe1.getX()/Info.getPPM(),((pipe1.getY()+Info.getHeight())/2f)/Info.getPPM());
        body1 = world.createBody(bodyDef);

        bodyDef.position.set(pipe2.getX()/Info.getPPM(),((pipe2.getY()+pipe1.getY()-Info.getVertical_distance())/2f)/Info.getPPM());
        body2 = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((pipe1.getWidth()/1f)/Info.getPPM(),((Info.getHeight()-pipe1.getY())/2f)/Info.getPPM());

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixture1 = body1.createFixture(fixtureDef);
        fixture1.setUserData("Pipe");

        shape.setAsBox((pipe2.getWidth()/1f)/Info.getPPM(),((pipe1.getY() - Info.getVertical_distance() - (Info.getHeight()/2f - 850f))/2f)/Info.getPPM());
        fixtureDef.shape = shape;
        fixture2 = body2.createFixture(fixtureDef);
        fixture2.setUserData("Pipe");


        shape.dispose();

    }

    private float Ycoordinate()
    {
        float lower = Info.getHeight()/2f - 850f + Info.getVertical_distance();
        float upper = Info.getHeight() - 500f;
        return random.nextFloat()*(upper - lower) + lower;
    }

    public void draw(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(pipe1,pipe1.getX()+15f,pipe1.getY()+15f,2f*pipe1.getWidth(),Info.getHeight());
        spriteBatch.draw(pipe2,pipe2.getX()+15f,pipe2.getY()+5f,2f*pipe2.getWidth(),pipe1.getY() - Info.getVertical_distance() - (Info.getHeight()/2f - 850f)+5f);
    }

    public void updatePipes()
    {
        pipe1.setPosition(body1.getPosition().x*Info.getPPM() - 70f,(body1.getPosition().y*Info.getPPM()+(pipe1.getY()-Info.getHeight())/2f));
        pipe2.setPosition(body2.getPosition().x*Info.getPPM() - 70f,(body2.getPosition().y*Info.getPPM()+(pipe2.getY()-pipe1.getY()+Info.getVertical_distance())/2f));
    }

    public void movePipes()
    {
        body1.setLinearVelocity(-3f,0);
        body2.setLinearVelocity(-3f,0);
    }

    public void stopPipes()
    {
        body1.setLinearVelocity(0,0);
        body2.setLinearVelocity(0,0);
    }

    public boolean ScoreIncrement(float x)
    {
        if(pipe1.getX() + pipe1.getWidth() - 3f < x && pipe1.getX() + pipe1.getWidth() + 3f > x) {
            return true;
        }
        return false;
    }

    public void disposePipes()
    {
        pipe1.getTexture().dispose();
        pipe2.getTexture().dispose();
        body1.setActive(false);
        body2.setActive(false);
    }

    public float getX(){
        return pipe1.getX();
    }

    public float getWidth(){
        return pipe1.getWidth();
    }

    public void setPipeforOutOfBounds(float x)
    {
        body1.setActive(false);
        body2.setActive(false);
        pipe1.setPosition(x+Info.getHorizontal_distance()+2f*pipe1.getWidth(),Ycoordinate());
        pipe2.setPosition(x+Info.getHorizontal_distance()+2f*pipe2.getWidth(),Info.getHeight()/2f - 860f);
        createBody();
    }

}
