package Screens;

import Engine.GraphicsHandler;
import Engine.Key;
import Engine.Keyboard;
import Engine.ScreenManager;
import SpriteFont.SpriteFont;
import Utils.Stopwatch;

import java.awt.*;

public class OptionsClass {
    static boolean zomAct = true;

    protected static SpriteFont pauseCon;

    public static void initOptions() {
        pauseCon = new SpriteFont("zombies " + zomAct, 200, 150, "Comic Sans", 30, new Color(49, 207, 240));

    }
    public static void drawOptionScreen(GraphicsHandler graphicsHandler, Stopwatch keyTimer){
        graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(),
                ScreenManager.getScreenHeight(), Color.white);
        pauseCon.draw(graphicsHandler);
        pauseCon.setText("zombies " + zomAct);

        if (Keyboard.isKeyDown(Key.ENTER) && keyTimer.isTimeUp()) {
            keyTimer.reset();
            zomAct = !zomAct;
            // System.out.println(currentMenuItemHovered);
        }
    }

    public static boolean getZomAct(){
        return zomAct;
    }
}
