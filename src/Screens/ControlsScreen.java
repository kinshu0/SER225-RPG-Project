package Screens;

import Engine.GraphicsHandler;
import Engine.ScreenManager;

import java.awt.*;

public class ControlsScreen {
    public static void drawControlScreen(GraphicsHandler graphicsHandler){
        graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(),
                ScreenManager.getScreenHeight(), Color.white);

    }
}
