package utility;

import java.awt.*;

public class PUtility {
    /**
     * Generates an Integer around some specific value within a specific deviation limit.
     *
     * @param value          The anchor value.
     * @param deviationLimit Absolute value for the divergence limit.
     * @return Generated value.
     */
    public static Integer generateValueAround(int value, int deviationLimit) {
        int deviation = generateRandomNumberWithMax(deviationLimit);
        if (wheelOfFortune() > 0.5) {
            deviation = (-1) * deviation;
        }
        Integer result = value + deviation;
        if (result < 0)
            return 0;
        else
            return result;
    }

    public static Integer getRandomNumber(int min, int max) {
        int retMin = min;
        int retMax = max - retMin + 1;

        int addition = ((int) (Math.random() * retMax));

        return new Integer(retMin + addition);
    }

    public static Double wheelOfFortune() {
        return Math.random();
    }

    public static int generateRandomNumberWithMax(int max) {
        return ((int) (Math.random() * max));
    }

    public static int generateRandomNumberWithMaxAndExclue(int max, int exclude) {
        boolean finished = false;
        int res = 0;
        while (!finished) {
        //    System.out.println("here");
            res = ((int) (Math.random() * max));
            if (res != exclude)
                finished = true;
        }
        return res;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++)
            System.out.println(getRandomNumber(1, 2));
    }

    public static boolean isBooleanString(String b) {
        return b.equalsIgnoreCase("true") || b.equalsIgnoreCase("false");
    }

    public static boolean isIntegerString(String i) {
        try {
            Integer.parseInt(i);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void centreWindow(Window frame) {
        frame.setLocationRelativeTo(null);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }
}
