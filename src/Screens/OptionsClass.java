package Screens;

import Engine.GraphicsHandler;
import Engine.Key;
import Engine.Keyboard;
import Engine.ScreenManager;
import Level.base;
import SpriteFont.SpriteFont;
import Utils.Stopwatch;

import java.awt.*;
import java.util.function.BooleanSupplier;

public class OptionsClass {
    static boolean zomAct = true;
    static boolean godMode = false;
    static boolean turnedGodModeOff = false;

    protected static SpriteFont pauseCon;
    protected static SpriteFont god;

    public static void initOptions() {
        pauseCon = new SpriteFont("zombies " + zomAct, 200, 150, "Comic Sans", 30, new Color(49, 207, 240));
        god = new SpriteFont("player/base God Mode (press g)" + godMode, 200, 175, "Comic Sans", 30,
                new Color(49, 207, 240));

    }

    public static void drawOptionScreen(GraphicsHandler graphicsHandler, Stopwatch keyTimer) {
        graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(),
                ScreenManager.getScreenHeight(), Color.white);
        pauseCon.draw(graphicsHandler);
        pauseCon.setText("zombies " + zomAct);
        god.draw(graphicsHandler);
        god.setText("God Mode and base infinite health (press g) " + godMode);
        if (Keyboard.isKeyDown(Key.ENTER) && keyTimer.isTimeUp()) {
            keyTimer.reset();
            zomAct = !zomAct;
            // System.out.println(currentMenuItemHovered);
        }

        if (Keyboard.isKeyDown(Key.G) && keyTimer.isTimeUp()) {
            keyTimer.reset();
            godMode = !godMode;
            if (godMode == false) {
                turnedGodModeOff = true;
                base.setBaseHealth(3000);
            } else {
                turnedGodModeOff = false;
                base.setBaseHealth(99999999);
            }
            // System.out.println(currentMenuItemHovered);
        }

    }

    public static boolean getZomAct() {
        return zomAct;
    }

    public static boolean getGodMode() {
        return godMode;
    }

    public static boolean turnedGodOff() {
        return turnedGodModeOff;
    }

    public static void turnFalseGod() {
        turnedGodModeOff = false;
    }
}
