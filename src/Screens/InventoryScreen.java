package Screens;

import Engine.*;
import EnhancedMapTiles.Axe;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.Player;
import java.awt.*;
import java.awt.image.BufferedImage;
import Level.Inventory;

// This class is for the inventory screen
public class InventoryScreen extends Screen {
    BufferedImage Axe = ImageLoader.load("Axe.png");
    BufferedImage Katana = ImageLoader.load("Katana.png");
    BufferedImage Machete = ImageLoader.load("machete.png");
    BufferedImage Spear = ImageLoader.load("Spear.png");

    protected ScreenCoordinator screenCoordinator;

    protected KeyLocker keyLocker = new KeyLocker();
    BufferedImage circleImg = ImageLoader.load("Inv_cir.png");

    public InventoryScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    public void initialize() {
        keyLocker.lockKey(Key.ESC);
    }

    public void update() {

        if (Keyboard.isKeyUp(Key.ESC)) {
            keyLocker.unlockKey(Key.ESC);
        }

        // if space is pressed, go back to main menu
        if (!keyLocker.isKeyLocked(Key.ESC) && Keyboard.isKeyDown(Key.ESC)) {
            screenCoordinator.setGameState(GameState.LEVEL);
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        // set the background color
        graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(),
                new Color(0, 0, 0, 100));

        //
        // for(int i = 0; i < pinventorySize(); )

        boolean goneThroughItems = false;
        for (int x = 0; x < 2000; x += 87) {
            for (int y = 0; y < 2000; y += 80) {

                graphicsHandler.drawImage(circleImg, x, y, 90, 80);
                // System.out.println(Inventory.getItem(Inventory.getSize() - (x + 1)));

                if (goneThroughItems == false && y == 0 && Inventory.getSize() > 0) {
                    int position = x / 87;
                    System.out.println(position);
                    if (Inventory.getItem(position) == "Axe") {
                        graphicsHandler.drawImage(Axe, x, y, 90, 80);
                    }

                    else if (Inventory.getItem(position) == "Spear") {
                        graphicsHandler.drawImage(Spear, x, y, 90, 80);

                    }

                    else if (Inventory.getItem(position) == "Machete") {
                        graphicsHandler.drawImage(Machete, x, y, 90, 80);
                    }

                    else if (Inventory.getItem(position) == "Katana") {
                        // System.out.println(Inventory.getSize());
                        graphicsHandler.drawImage(Katana, x, y, 90, 80);
                    }

                    if (position == Inventory.getSize() - 1) {
                        goneThroughItems = true;
                    }

                    /*
                     * if (x == Inventory.getSize()) {
                     * goneThroughItems = true;
                     * }
                     */

                }

            }
        }
    }
}
