package Screens;

import Engine.GraphicsHandler;
import Engine.Key;
import Engine.Keyboard;
import Engine.ScreenManager;
import Game.GameState;
import SpriteFont.SpriteFont;
import Engine.*;
import Game.ScreenCoordinator;
import Utils.Stopwatch;

import java.awt.*;

public class PauseScreen {
    private static SpriteFont pauseLabel;
    protected static int currentMenuItemHovered = 0;
    protected static int menuItemSelected = -1;
    protected static SpriteFont optionsLabel;
    protected static SpriteFont controlsLabel;
    protected static SpriteFont extrasLabel;
    protected static SpriteFont saveLabel;
    protected static boolean controlOn = false;
    protected static boolean OptionsOn = false;
    protected static boolean SaveOn = false;
    protected static boolean exitOn = false;

    public static void initPause(){
        // pause logic
        pauseLabel = new SpriteFont("PAUSE SCREEN", 325, 50, "Comic Sans", 24, Color.white);
        pauseLabel.setOutlineColor(Color.black);
        pauseLabel.setOutlineThickness(2.0f);

        optionsLabel = new SpriteFont("Options", 380, 100, "Comic Sans", 24, Color.white);
        optionsLabel.setOutlineColor(Color.white);
        optionsLabel.setOutlineThickness(2.0f);

        controlsLabel = new SpriteFont("Controls", 375, 175, "Comic Sans", 24, Color.white);
        controlsLabel.setOutlineColor(Color.white);
        controlsLabel.setOutlineThickness(2.0f);

        extrasLabel = new SpriteFont("Extras", 385, 250, "Comic Sans", 24, Color.white);
        extrasLabel.setOutlineColor(Color.white);
        extrasLabel.setOutlineThickness(2.0f);

        saveLabel = new SpriteFont("Exit", 390, 325, "Comic Sans", 24, Color.white);
        saveLabel.setOutlineColor(Color.white);
        saveLabel.setOutlineThickness(2.0f);
    }




    public static boolean drawPause(GraphicsHandler graphicsHandler, Stopwatch keyTimer, ScreenCoordinator screenCoordinator, KeyLocker keyLocker){
        graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(),
                new Color(0, 0, 0, 100));
        extrasLabel.draw(graphicsHandler);
        pauseLabel.draw(graphicsHandler);
        optionsLabel.draw(graphicsHandler);
        controlsLabel.draw(graphicsHandler);
        saveLabel.draw(graphicsHandler);

        if (Keyboard.isKeyDown(Key.DOWN) && keyTimer.isTimeUp()) {
            keyTimer.reset();
            currentMenuItemHovered++;
            // System.out.println(currentMenuItemHovered);
        } else if (Keyboard.isKeyDown(Key.UP) && keyTimer.isTimeUp()) {
            keyTimer.reset();
            currentMenuItemHovered--;
        }

        if (currentMenuItemHovered > 3) {
            currentMenuItemHovered = 0;
        } else if (currentMenuItemHovered < 0) {
            currentMenuItemHovered = 3;
        }

        if (currentMenuItemHovered == 0) {
            optionsLabel.setColor(new Color(147, 112, 219));
            controlsLabel.setColor(new Color(49, 207, 240));
            extrasLabel.setColor(new Color(49, 207, 240));
            saveLabel.setColor(new Color(49, 207, 240));
        } else if (currentMenuItemHovered == 1) {
            optionsLabel.setColor(new Color(49, 207, 240));
            controlsLabel.setColor(new Color(147, 112, 219));
            extrasLabel.setColor(new Color(49, 207, 240));
            saveLabel.setColor(new Color(49, 207, 240));
        } else if (currentMenuItemHovered == 2) {
            optionsLabel.setColor(new Color(49, 207, 240));
            controlsLabel.setColor(new Color(49, 207, 240));
            extrasLabel.setColor(new Color(147, 112, 219));
            saveLabel.setColor(new Color(49, 207, 240));
        } else if (currentMenuItemHovered == 3) {
            optionsLabel.setColor(new Color(49, 207, 240));
            controlsLabel.setColor(new Color(49, 207, 240));
            extrasLabel.setColor(new Color(49, 207, 240));
            saveLabel.setColor(new Color(147, 112, 219));
        }

        menuItemSelected = currentMenuItemHovered;
        if (Keyboard.isKeyDown(Key.SPACE) && !keyLocker.isKeyLocked(Key.SPACE) && menuItemSelected == 1) {
            controlOn = !controlOn;
            keyLocker.lockKey(Key.SPACE);
        }

        if (controlOn){
            ControlsScreen.drawControlScreen(graphicsHandler);
            menuItemSelected = 1;
        }

        // this unlocks the screen
        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }

        if (Keyboard.isKeyDown(Key.SPACE) && !keyLocker.isKeyLocked(Key.SPACE) && menuItemSelected == 0) {
            OptionsOn = !OptionsOn;
            keyLocker.lockKey(Key.SPACE);
        }

        if (OptionsOn){
            OptionsClass.drawOptionScreen(graphicsHandler);
            menuItemSelected = 2;
        }

        // this unlocks the screen
        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }

        if (Keyboard.isKeyDown(Key.SPACE) && !keyLocker.isKeyLocked(Key.SPACE) && menuItemSelected == 2) {
            SaveOn = !SaveOn;
            keyLocker.lockKey(Key.SPACE);
        }

        if (SaveOn){
            ExtraScreen.drawOptionScreen(graphicsHandler);
            menuItemSelected = 0;
        }

        // this unlocks the screen
        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }

        if (Keyboard.isKeyDown(Key.SPACE) && menuItemSelected == 3) {
            screenCoordinator.setGameState(GameState.MENU);
            return true;
        }

        return false;
    }
}

/*
        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }
        if (!keyLocker.isKeyLocked(Key.SPACE) && Keyboard.isKeyDown(Key.SPACE)) {



        }
 else if (menuItemSelected == 2) {

            } else if (menuItemSelected == 0){

            }
 */
