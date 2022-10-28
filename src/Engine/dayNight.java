package Engine;

import Engine.DayNight.RunState;
import Engine.DayNight.State;
import SpriteFont.SpriteFont;

import java.awt.*;

public class dayNight {
    private static int count_updates = 0;
    protected static SpriteFont timeLabels;

    public static void initDayNight(){
        // time
        timeLabels = new SpriteFont("Time: 3:00", 10, 25, "Comic Sans", 30, Color.black);
        timeLabels.setOutlineColor(Color.white);
        timeLabels.setOutlineThickness(3);

    }


    public static SpriteFont drawDayNight(RunState runState){
        count_updates = (count_updates + 1) % (60 * 24 * 60);
        timeLabels.setText(String.format("Time: %02d:%02d %s", count_updates / 3600,
                (count_updates % 3600) / 60, (count_updates % 3600) / 60 > 30 ? "Day" : "Night"));
        // count_updates = (count_updates + 1) % (60 * 24 * 60);
        // timeLabels.setText(String.format("Time: %02d:%02d %s", count_updates / 3600,
        // (count_updates % 3600) / 60, (count_updates % 3600) / 60 > 30 ? "Day" :
        // "Night"));
        runState.runCycle();
        int s = runState.c.getSecondsOfDay();
        int h = runState.c.getHoursOfDay();
        int m = runState.c.getMinutesOfDay();
        State st = runState.c.getState();
        timeLabels.setText(String.format("Time: %02d:%02d:%02d (%s)\n", h, m, s, st));
        return timeLabels;
    }

    public static boolean isNight(){
        if (((count_updates % 3600) / 60 > 30 ? "Day" : "Night") == "Night"){
            return true;
        }
        return false;
    }


}
