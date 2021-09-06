package helper;

import java.util.Random;

public class Backgrounds {

    private static String[] backgrounds = {"Day.jpg","flappybirdDayImage.png","flappybirdNightImage.png","Night.jpg"};
    private static Random random = new Random();
    private static int index = 0;

    public static void setRandomIndex(){
        index=random.nextInt(4);
    }

    public static int getRandomIndex(){
        return index;
    }
    public static String[] getBackgrounds(){
        return backgrounds;
    }

}
