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

// This class is for when the platformer game is actually being played
public class PlayLevelScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected Map map;
    protected Player player;
    protected SpriteFont livesLabels;
    protected SpriteFont timeLabels;
    private SpriteFont pauseLabel;

    protected PlayLevelScreenState playLevelScreenState;
    protected WinScreen winScreen;
    protected FlagManager flagManager;

    protected Key LIVES_UP_KEY = Key.U;
    protected Key LIVES_DOWN_KEY = Key.D;
    private KeyLocker keyLocker = new KeyLocker();
    private final Key pauseKey = Key.P;

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
        if (Keyboard.isKeyDown(pauseKey) && !keyLocker.isKeyLocked(pauseKey)) {
            GamePanel.setIsGamePaused(!GamePanel.isGamePaused());
            keyLocker.lockKey(pauseKey);
        }

        // if game is paused, draw pause gfx over Screen gfx
        if (GamePanel.isGamePaused()) {
            pauseLabel.draw(graphicsHandler);
            graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(), new Color(0, 0, 0, 100));
        }

        if (Keyboard.isKeyUp(pauseKey)) {
            keyLocker.unlockKey(pauseKey);
        }

        if (!GamePanel.isGamePaused()) {
            // based on screen state, draw appropriate graphics
            switch (playLevelScreenState) {
                case RUNNING:
                    map.draw(player, graphicsHandler);
                    livesLabels.draw(graphicsHandler);
                    timeLabels.draw(graphicsHandler);
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
