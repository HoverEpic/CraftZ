package io.github.jangobrick.craftz.util;

public class AdditionalCraftZData {

    public int thirst = 20, zombiesKilled, playersKilled, minutesSurvived;
    public boolean bleeding, poisoned, bonesBroken;

    public AdditionalCraftZData(int thirst, int zombiesKilled, int playersKilled, int minutesSurvived,
            boolean bleeding, boolean bonesBroken, boolean poisoned) {

        this.thirst = thirst;
        this.zombiesKilled = zombiesKilled;
        this.playersKilled = playersKilled;
        this.minutesSurvived = minutesSurvived;
        this.bleeding = bleeding;
        this.bonesBroken = bonesBroken;
        this.poisoned = poisoned;

    }

}
