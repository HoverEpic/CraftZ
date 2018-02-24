package io.github.jangobrick.craftz.util;

import java.util.Calendar;

import io.github.jangobrick.craftz.CraftZ;

public class Time {

    public static void setToServerTime() {

        int hours = getHour();
        if (!isAM())
            hours = hours + 12;
        int min = getMinutes();

        long totalTime = (hours + 16) * 1000;
        if (totalTime < 0L)
            totalTime += 24000L;
        double tmin = min * 16.666699999999999D;
        totalTime += (int) tmin;

        CraftZ.world().setFullTime(totalTime);

    }

    public static boolean isAM() {
        return Calendar.getInstance().get(Calendar.AM_PM) == 0;
    }

    public static int getHour() {
        return Calendar.getInstance().get(Calendar.HOUR);
    }

    public static int getMinutes() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

}
