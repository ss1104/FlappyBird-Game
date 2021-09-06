package helper;

public class BirdColors
{
    private static String[] color = {"RedBird","GreenBird","BlueBird"};
    private static int index = 0;

    public static String[] getColor() {
        return color;
    }

    public static int getIndex(){
        return index;
    }

    public static void increment(){
        index = (index+1)%3;
    }
}
