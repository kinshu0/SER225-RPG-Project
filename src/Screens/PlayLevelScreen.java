package Screens;

import Engine.*;
import Engine.DayNight.RunState;
import Engine.DayNight.State;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.*;
import Maps.TestMap;
import Players.Cat;
import SpriteFont.SpriteFont;
import Utils.Direction;
import Utils.Point;
import Utils.Stopwatch;

import java.awt.*;
import java.awt.image.BufferedImage;

// This class is for when the platformer game is actually being played
public class PlayLevelScreen extends Screen {
    protected int currentMenuItemHovered = 0;
    protected int xInvSelect = 0;
    protected int yInvSelect = 0;
    protected int menuItemSelected = -1;
    protected ScreenCoordinator screenCoordinator;
    protected Stopwatch keyTimer = new Stopwatch();
    protected Map map;
    protected Player player;
    protected SpriteFont livesLabels;
    protected SpriteFont timeLabels;
    private SpriteFont pauseLabel;
    protected SpriteFont craftingLabel;
    protected SpriteFont inventoryLabel;
    protected SpriteFont optionsLabel;
    protected SpriteFont controlsLabel;
    protected SpriteFont extrasLabel;
    protected SpriteFont saveLabel;
    protected SpriteFont pauselivesLabels;
    protected SpriteFont TimelivesLabels;

    BufferedImage rect = ImageLoader.load("rect.png");
    BufferedImage Axe = ImageLoader.load("Axe.png");
    BufferedImage backgroundFilter = ImageLoader.load("background-filter.png");

    protected PlayLevelScreenState playLevelScreenState;
    protected WinScreen winScreen;
    protected FlagManager flagManager;

    protected Key LIVES_UP_KEY = Key.U;
    protected Key LIVES_DOWN_KEY = Key.D;

    protected Key inventoryScreen = Key.I;
    protected Key CraftingScreen = Key.C;
    private static boolean isCraftingScreen = false;
    private static boolean isInventoryScreen = false;
    private KeyLocker keyLocker = new KeyLocker();
    private final Key pauseKey = Key.P;

    private int count_updates = 0;
    private RunState runState = new RunState();

    public PlayLevelScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    public void initialize() {
        // setup state
        flagManager = new FlagManager();
        flagManager.addFlag("hasLostBall", false);
        flagManager.addFlag("hasTalkedToWalrus", false);
        flagManager.addFlag("hasTalkedToDinosaur", false);
        flagManager.addFlag("hasFoundBall", false);

        // define/setup map
        this.map = new TestMap();
        map.reset();
        map.setFlagManager(flagManager);

        // setup player
        this.player = new Cat(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
        this.player.setMap(map);
        Point playerStartPosition = map.getPlayerStartPosition();
        this.player.setLocation(playerStartPosition.x, playerStartPosition.y);
        this.playLevelScreenState = PlayLevelScreenState.RUNNING;
        this.player.setFacingDirection(Direction.LEFT);

        // let pieces of map know which button to listen for as the "interact" button
        map.getTextbox().setInteractKey(player.getInteractKey());
        // setup map scripts to have references to the map and player
        for (MapTile mapTile : map.getMapTiles()) {
            if (mapTile.getInteractScript() != null) {
                mapTile.getInteractScript().setMap(map);
                mapTile.getInteractScript().setPlayer(player);
            }
        }
        for (NPC npc : map.getNPCs()) {
            if (npc.getInteractScript() != null) {
                npc.getInteractScript().setMap(map);
                npc.getInteractScript().setPlayer(player);
            }
        }
        for (EnhancedMapTile enhancedMapTile : map.getEnhancedMapTiles()) {
            if (enhancedMapTile.getInteractScript() != null) {
                enhancedMapTile.getInteractScript().setMap(map);
                enhancedMapTile.getInteractScript().setPlayer(player);
            }
        }
        for (Trigger trigger : map.getTriggers()) {
            if (trigger.getTriggerScript() != null) {
                trigger.getTriggerScript().setMap(map);
                trigger.getTriggerScript().setPlayer(player);
            }
        }
        // lives
        livesLabels = new SpriteFont(player.getPlayerLives(), 10, 60, "Comic Sans", 30, Color.black);
        livesLabels.setOutlineColor(Color.white);
        livesLabels.setOutlineThickness(3);

        // time
        timeLabels = new SpriteFont("Time: 3:00", 10, 25, "Comic Sans", 30, Color.black);
        timeLabels.setOutlineColor(Color.white);
        timeLabels.setOutlineThickness(3);

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

        pauselivesLabels = new SpriteFont(player.getPlayerLives(), 10, 550, "Comic Sans", 24, Color.black);
        pauselivesLabels.setOutlineColor(Color.black);
        pauselivesLabels.setOutlineThickness(3);

        TimelivesLabels = new SpriteFont("Time: 3:00", 650, 550, "Comic Sans", 24, Color.black);
        TimelivesLabels.setOutlineColor(Color.black);
        TimelivesLabels.setOutlineThickness(3);

        // crafting logic
        craftingLabel = new SpriteFont("Crafting", 400, 500, "Comic Sans", 24, Color.white);
        craftingLabel.setOutlineColor(Color.black);
        craftingLabel.setOutlineThickness(2.0f);

        // inventory logic
        inventoryLabel = new SpriteFont("Inventory", 365, 330, "Comic Sans", 24, Color.white);
        inventoryLabel.setOutlineColor(Color.black);
        inventoryLabel.setOutlineThickness(2.0f);

        winScreen = new WinScreen(this);
        keyTimer.setWaitTime(200);
    }

    public void update() {
        // TODO: changing lives is not working atm
        if (Keyboard.isKeyDown(LIVES_UP_KEY)) {
            player.setPlayerLives(Math.max(0, Math.min(5, player.getPlayerLivesI() + 1)));
        } else if (Keyboard.isKeyDown(LIVES_DOWN_KEY)) {
            player.setPlayerLives(Math.max(0, Math.min(5, player.getPlayerLivesI() - 1)));
        }

        // based on screen state, perform specific actions
        switch (playLevelScreenState) {
            // if level is "running" update player and map to keep game logic for the
            // platformer level going
            case RUNNING:
                player.update();
                map.update(player);
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
                livesLabels.setText(player.getPlayerLives());
                break;
            // if level has been completed, bring up level cleared screen
            case LEVEL_COMPLETED:
                winScreen.update();
                break;
        }

        // if flag is set at any point during gameplay, game is "won"
        if (map.getFlagManager().isFlagSet("hasFoundBall")) {
            playLevelScreenState = PlayLevelScreenState.LEVEL_COMPLETED;
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        // **pause screen**

        // this is what tells if the key is down
        if (Keyboard.isKeyDown(pauseKey) && !keyLocker.isKeyLocked(pauseKey)) {
            GamePanel.setIsGamePaused(!GamePanel.isGamePaused());
            keyLocker.lockKey(pauseKey);
        }

        // this is what actually draws it
        if (GamePanel.isGamePaused()) {
            // graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(),
            // ScreenManager.getScreenHeight(), new Color(234, 221, 202));
            pauseLabel.draw(graphicsHandler);
            optionsLabel.draw(graphicsHandler);
            controlsLabel.draw(graphicsHandler);
            extrasLabel.draw(graphicsHandler);
            saveLabel.draw(graphicsHandler);
            pauselivesLabels.draw(graphicsHandler);
            TimelivesLabels.draw(graphicsHandler);

            if (Keyboard.isKeyDown(Key.DOWN) && keyTimer.isTimeUp()) {
                keyTimer.reset();
                currentMenuItemHovered++;
                System.out.println(currentMenuItemHovered);
            } else if (Keyboard.isKeyDown(Key.UP) && keyTimer.isTimeUp()) {
                keyTimer.reset();
                currentMenuItemHovered--;
            }

            // if down is pressed on last menu item or up is pressed on first menu item,
            // "loop" the selection back around to the beginning/end
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

            if (Keyboard.isKeyUp(Key.SPACE)) {
                keyLocker.unlockKey(Key.SPACE);
            }
            if (!keyLocker.isKeyLocked(Key.SPACE) && Keyboard.isKeyDown(Key.SPACE)) {
                menuItemSelected = currentMenuItemHovered;
                if (menuItemSelected == 3) {
                    screenCoordinator.setGameState(GameState.MENU);
                } else if (menuItemSelected == 1) {
                    graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(),
                            ScreenManager.getScreenHeight(), Color.white);
                } else if (menuItemSelected == 2) {
                    graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(),
                            ScreenManager.getScreenHeight(), Color.blue);
                }
            }
        }

        // this unlocks the screen
        if (Keyboard.isKeyUp(pauseKey)) {
            keyLocker.unlockKey(pauseKey);
        }

        // ** crafting screen **
        if (Keyboard.isKeyDown(CraftingScreen) && !keyLocker.isKeyLocked(CraftingScreen)) {
            isCraftingScreen = !isCraftingScreen;
            keyLocker.lockKey(CraftingScreen);
        }

        if (isCraftingScreen) {
            graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(),
                    new Color(234, 221, 202));
            craftingLabel.draw(graphicsHandler);
        }

        if (Keyboard.isKeyUp(CraftingScreen)) {
            keyLocker.unlockKey(CraftingScreen);
        }

        // ** inventory screen **
        if (Keyboard.isKeyDown(inventoryScreen) && !keyLocker.isKeyLocked(inventoryScreen)) {
            isInventoryScreen = !isInventoryScreen;
            keyLocker.lockKey(inventoryScreen);
        }

        if (isInventoryScreen) {
            graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(),
                    new Color(245, 245, 220));
            inventoryLabel.draw(graphicsHandler);

            // writes the rectangle
            for (int y = 30; y < 300; y = y + 70) {
                for (int x = 15; x + 70 < 800; x = x + 70) {
                    graphicsHandler.drawFilledRectangle(x, y, 60, 60, Color.BLACK);
                    graphicsHandler.drawFilledRectangle(x + 2, y + 2, 56, 56, Color.gray);
                }
            }

            // crafting portion
            graphicsHandler.drawFilledRectangle(150, 400, 60, 60, Color.BLACK);
            graphicsHandler.drawFilledRectangle(150 + 2, 400 + 2, 56, 56, Color.gray);

            graphicsHandler.drawFilledRectangle(300, 400, 60, 60, Color.BLACK);
            graphicsHandler.drawFilledRectangle(300 + 2, 400 + 2, 56, 56, Color.gray);

            graphicsHandler.drawFilledRectangle(500, 400, 60, 60, Color.BLACK);
            graphicsHandler.drawFilledRectangle(500 + 2, 400 + 2, 56, 56, Color.gray);

            if (Keyboard.isKeyDown(Key.DOWN)) {
                yInvSelect = Math.max(0, Math.min(3, yInvSelect + 1));
            } else if (Keyboard.isKeyDown(Key.UP)) {
                yInvSelect = Math.max(0, Math.min(3, yInvSelect - 1));
            } else if (Keyboard.isKeyDown(Key.LEFT)) {
                xInvSelect = Math.max(0, Math.min(10, xInvSelect - 1));
            } else if (Keyboard.isKeyDown(Key.RIGHT)) {
                xInvSelect = Math.max(0, Math.min(10, xInvSelect + 1));
            }

            // highlights which box
            graphicsHandler.drawFilledRectangle(xInvSelect * 70 + 15, yInvSelect * 70 + 30, 60, 60, Color.yellow);
            graphicsHandler.drawFilledRectangle(xInvSelect * 70 + 15 + 2, yInvSelect * 70 + 30 + 2, 56, 56, Color.gray);

        }

        if (Keyboard.isKeyUp(inventoryScreen)) {
            keyLocker.unlockKey(inventoryScreen);
        }

        if (!GamePanel.isGamePaused() && !isCraftingScreen && !isInventoryScreen) {
            // based on screen state, draw appropriate graphics
            switch (playLevelScreenState) {
                case RUNNING:
                    map.draw(player, graphicsHandler);
                    livesLabels.draw(graphicsHandler);
                    timeLabels.draw(graphicsHandler);
                    for (int i = 250; i < 550; i += 50) {
                        graphicsHandler.drawImage(rect, i, 500, 50, 50);
                    }
                    // just need to add variables for spaces
                    graphicsHandler.drawImage(Axe, 255, 510, 30, 30);
                    // graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(),
                    // ScreenManager.getScreenHeight(), new Color(0, 0, 0,100));
                    // graphicsHandler.drawImage(backgroundFilter, 0, 0);
                    graphicsHandler.drawImageAlpha(backgroundFilter, 0, 0, 786, 568,
                            (float) Math.sin(((double) (count_updates % 3600) / 3600) * Math.PI * 2));
                    // System.out.println(String.format("Width: %d\tHeight: %d",
                    // ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight()));
                    break;
                case LEVEL_COMPLETED:
                    winScreen.draw(graphicsHandler);
                    break;
            }
        }
    }

    public PlayLevelScreenState getPlayLevelScreenState() {
        return playLevelScreenState;
    }

    public void resetLevel() {
        initialize();
    }

    public void goBackToMenu() {
        screenCoordinator.setGameState(GameState.MENU);
    }

    // This enum represents the different states this screen can be in
    private enum PlayLevelScreenState {
        RUNNING, LEVEL_COMPLETED
    }
}
