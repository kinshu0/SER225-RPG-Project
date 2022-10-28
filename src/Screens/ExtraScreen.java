package Screens;

import Engine.GraphicsHandler;
import Engine.ScreenManager;

import java.awt.*;

public class ExtraScreen {
    public static void drawOptionScreen(GraphicsHandler graphicsHandler){
        graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(),
                ScreenManager.getScreenHeight(), Color.yellow);

    }
}
