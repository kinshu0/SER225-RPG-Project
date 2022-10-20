package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;

import java.awt.*;
import java.awt.image.BufferedImage;

// This class is for the inventory screen
public class InventoryScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;

    protected KeyLocker keyLocker = new KeyLocker();
    BufferedImage circleImg = ImageLoader.load("Inv_cir.png");

    public InventoryScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    public void initialize() {
        keyLocker.lockKey(Key.ESC);
    }

    public void update(){

        if (Keyboard.isKeyUp(Key.ESC)) {
            keyLocker.unlockKey(Key.ESC);
        }

        // if space is pressed, go back to main menu
        if (!keyLocker.isKeyLocked(Key.ESC) && Keyboard.isKeyDown(Key.ESC)) {
            screenCoordinator.setGameState(GameState.LEVEL);
        }
    }

    public void draw(GraphicsHandler graphicsHandler){
        // set the background color
        graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(), new Color(0, 0, 0, 100));
        for (int x = 0; x < 2000; x += 87){
            for (int y = 0; y < 2000; y += 80) {
                graphicsHandler.drawImage(circleImg, x, y, 90, 80);
            }
        }
    }
}
