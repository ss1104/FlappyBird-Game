package Assets;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import helper.Info;

public class GroundBody {

    private World world;
    private Body body;
    private Fixture fixture;

    public GroundBody(Sprite sprite, World world) {
        this.world = world;
        createBody(sprite);
    }

    private void createBody(Sprite sprite)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(sprite.getX()/Info.getPPM(),sprite.getY()/Info.getPPM());
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Info.getWidth()/Info.getPPM(),(Info.getHeight()/2f - 850f)/Info.getPPM());

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixture = body.createFixture(fixtureDef);
        fixture.setUserData("Ground");

        shape.dispose();
    }
}
