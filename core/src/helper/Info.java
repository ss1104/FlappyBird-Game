package helper;

import com.badlogic.gdx.Gdx;


public class Info
{
    private static final int width  = Gdx.graphics.getWidth();
    private static final int height = Gdx.graphics.getHeight();
    private static final int PPM = 100; // Pixels Per Meter
    private static final float vertical_distance = 450f;
    private static final float horizontal_distance = 450f;

    public static int getWidth()
    {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static int getPPM() {
        return PPM;
    }


    public static float getVertical_distance() {
        return vertical_distance;
    }

    public static float getHorizontal_distance() {
        return horizontal_distance;
    }
}
