package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.*;
import Maps.TestMap;
import Players.Cat;
import SpriteFont.SpriteFont;
import Utils.Direction;
import Utils.Point;

import java.awt.*;
import java.awt.image.BufferedImage;

// This class is for when the platformer game is actually being played
public class PlayLevelScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected Map map;
    protected Player player;
    protected SpriteFont livesLabels;
    protected SpriteFont timeLabels;
    private SpriteFont pauseLabel;
    protected SpriteFont craftingLabel;
    protected SpriteFont inventoryLabel;

    BufferedImage rect = ImageLoader.load("rect.png");
    BufferedImage Axe = ImageLoader.load("Axe.png");

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
        livesLabels = new SpriteFont(player.getPlayerLives(), 10, 60, "Comic Sans", 30,  Color.black);
        livesLabels.setOutlineColor(Color.white);
        livesLabels.setOutlineThickness(3);

        // time
        timeLabels = new SpriteFont("Time: 3:00", 10, 25, "Comic Sans", 30,  Color.black);
        timeLabels.setOutlineColor(Color.white);
        timeLabels.setOutlineThickness(3);

        // pause logic
        pauseLabel = new SpriteFont("PAUSE", 365, 280, "Comic Sans", 24, Color.white);
        pauseLabel.setOutlineColor(Color.black);
        pauseLabel.setOutlineThickness(2.0f);

        // crafting logic
        craftingLabel = new SpriteFont("Crafting", 365, 280, "Comic Sans", 24, Color.white);
        craftingLabel.setOutlineColor(Color.black);
        craftingLabel.setOutlineThickness(2.0f);

        // inventory logic
        inventoryLabel = new SpriteFont("Crafting", 365, 280, "Comic Sans", 24, Color.white);
        inventoryLabel.setOutlineColor(Color.black);
        inventoryLabel.setOutlineThickness(2.0f);

        winScreen = new WinScreen(this);
    }

    public void update() {
        // TODO: changing lives is not working atm
        if (Keyboard.isKeyDown(LIVES_UP_KEY)){
            player.setPlayerLives(Math.max(0,Math.min(5,player.getPlayerLivesI()+1)));
        } else if (Keyboard.isKeyDown(LIVES_DOWN_KEY)) {
            player.setPlayerLives(Math.max(0,Math.min(5,player.getPlayerLivesI()-1)));
        }

        // based on screen state, perform specific actions
        switch (playLevelScreenState) {
            // if level is "running" update player and map to keep game logic for the platformer level going
            case RUNNING:
                player.update();
                map.update(player);
                count_updates = (count_updates + 1) % (60*24*60);
                timeLabels.setText(String.format("Time: %02d:%02d %s", count_updates / 3600, (count_updates % 3600) / 60 , (count_updates % 3600) / 60 > 30 ? "Day" : "Night"));
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
            pauseLabel.draw(graphicsHandler);
            graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(), new Color(0, 0, 0, 100));
        }

        // this unlocks the screen
        if (Keyboard.isKeyUp(pauseKey)) {
            keyLocker.unlockKey(pauseKey);
        }

        // crafting screen
        if (Keyboard.isKeyDown(CraftingScreen) && !keyLocker.isKeyLocked(CraftingScreen)) {
            isCraftingScreen = !isCraftingScreen;
            keyLocker.lockKey(CraftingScreen);
        }

        if (isCraftingScreen) {
            graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(), new Color(234, 221, 202));
            craftingLabel.draw(graphicsHandler);
        }

        if (Keyboard.isKeyUp(CraftingScreen)) {
            keyLocker.unlockKey(CraftingScreen);
        }

        // inventory screen
        if (Keyboard.isKeyDown(inventoryScreen) && !keyLocker.isKeyLocked(inventoryScreen)) {
            isInventoryScreen = !isInventoryScreen;
            keyLocker.lockKey(inventoryScreen);
        }

        if (isInventoryScreen) {
            graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(), new Color(245, 245, 220));
            inventoryLabel.draw(graphicsHandler);
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
                    for(int i = 250; i < 550; i+=50){
                        graphicsHandler.drawImage(rect, i, 500, 50, 50);
                    }
                    // just need to add variables for spaces
                    graphicsHandler.drawImage(Axe, 255, 510, 30, 30);
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
