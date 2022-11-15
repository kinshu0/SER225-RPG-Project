package Screens;

import Engine.GraphicsHandler;
import Engine.ScreenManager;
import SpriteFont.SpriteFont;

import java.awt.*;

public class ControlsScreen {

    protected static SpriteFont pauseCon;
    protected static SpriteFont ArrowCon;
    protected static SpriteFont numCon;

    public static void initControl() {
        pauseCon = new SpriteFont("P: pause screen ", 200, 150, "Comic Sans", 30, new Color(49, 207, 240));
        ArrowCon = new SpriteFont("Arrow Keys: move player", 200, 180, "Comic Sans", 30, new Color(49, 207, 240));
        numCon = new SpriteFont("Number keys: select weapon ", 200, 210, "Comic Sans", 30, new Color(49, 207, 240));
    }
    public static void drawControlScreen(GraphicsHandler graphicsHandler){
        graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(),
                ScreenManager.getScreenHeight(), Color.white);
        pauseCon.draw(graphicsHandler);
        ArrowCon.draw(graphicsHandler);
        numCon.draw(graphicsHandler);


    }
}
