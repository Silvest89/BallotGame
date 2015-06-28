package eu.silvenia.shipballot;

/**
 * Created by Johnnie Ho on 28-6-2015.
 */
public class HelperTools {
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
