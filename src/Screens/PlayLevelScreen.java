package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.*;
import Maps.TestMap;
import NPCs.Ghost;
import NPCs.Knight;
import NPCs.Zombie;
import NPCs.ZombieBoss;
import Players.Cat;
import Players.CatWep;
import Players.*;
import SpriteFont.SpriteFont;
import Utils.Direction;
import Utils.Point;
import Utils.Stopwatch;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.lang.model.util.ElementScanner14;

//import javax.lang.model.util.ElementScanner14;

// This class is for when the platformer game is actually being played
public class PlayLevelScreen extends Screen {

    Random rand = new Random();

    protected int xInvSelect = 0;
    protected int yInvSelect = 0;
    protected int boxSel = 1;

    protected ScreenCoordinator screenCoordinator;
    protected Map map;
    protected Player player;
    protected Player player1;
    protected Player playerKatana;
    protected Player playerMachete;
    protected Player playerSpear;
    protected SpriteFont livesLabels;
    protected SpriteFont NightLabels;
    protected SpriteFont timeLabels;
    protected SpriteFont craftingLabel;
    protected SpriteFont inventoryLabel;
    protected SpriteFont baseLabel;

    protected Stopwatch keyTimer = new Stopwatch();

    BufferedImage rect = ImageLoader.load("rect.png");
    BufferedImage Axe = ImageLoader.load("Axe.png");
    BufferedImage AxePlus1 = ImageLoader.load("Axe+1.png");
    BufferedImage Katana = ImageLoader.load("Katana.png");
    BufferedImage Machete = ImageLoader.load("machete.png");
    BufferedImage Spear = ImageLoader.load("Spear.png");
    BufferedImage Steel = ImageLoader.load("Steel.png");
    BufferedImage MedKit = ImageLoader.load("MedKit.png");
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

    protected SpriteFont NightTitleScreen;

    protected boolean titleScreenOn = false;

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
        this.player1 = new CatWep(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
        this.playerKatana = new CatKatana(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
        this.playerMachete = new CatMachete(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
        this.playerSpear = new CatSpear(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
        this.player.setMap(map);
        this.player1.setMap(map);
        this.playerKatana.setMap(map);
        this.playerMachete.setMap(map);
        this.playerSpear.setMap(map);
        Point playerStartPosition = map.getPlayerStartPosition();
        this.player.setLocation(playerStartPosition.x, playerStartPosition.y);
        this.playLevelScreenState = PlayLevelScreenState.RUNNING;
        this.player.setFacingDirection(Direction.LEFT);
        this.player1.setLocation(player.getX(), player.getY());
        this.player1.setFacingDirection(Direction.LEFT);
        this.playerKatana.setLocation(player.getX(), player.getY());
        this.playerKatana.setFacingDirection(Direction.LEFT);
        this.playerMachete.setLocation(player.getX(), player.getY());
        this.playerMachete.setFacingDirection(Direction.LEFT);
        this.playerSpear.setLocation(player.getX(), player.getY());
        this.playerSpear.setFacingDirection(Direction.LEFT);

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

        // lives
        timeLabels = new SpriteFont("", 10, 30, "Comic Sans", 30, Color.black);
        timeLabels.setOutlineColor(Color.white);
        timeLabels.setOutlineThickness(3);

        // crafting logic
        baseLabel = new SpriteFont(base.getBaseHealthS(), 10, 90, "Comic Sans", 30, Color.black);
        baseLabel.setOutlineColor(Color.white);
        baseLabel.setOutlineThickness(3);

        // crafting logic
        craftingLabel = new SpriteFont("Crafting", 400, 500, "Comic Sans", 24, Color.white);
        craftingLabel.setOutlineColor(Color.black);
        craftingLabel.setOutlineThickness(2.0f);

        // inventory logic
        inventoryLabel = new SpriteFont("Inventory", 365, 330, "Comic Sans", 24, Color.white);
        inventoryLabel.setOutlineColor(Color.black);
        inventoryLabel.setOutlineThickness(2.0f);

        NightLabels = new SpriteFont(TheTimekeeperNecromaniac.getNight(), 10, 120, "Comic Sans", 24, Color.white);
        NightLabels.setOutlineColor(Color.white);
        NightLabels.setOutlineThickness(3);

        NightTitleScreen = new SpriteFont("", 80, 330, "Times New Roman", 170, Color.WHITE);
        NightTitleScreen.setOutlineColor(Color.BLACK);
        NightTitleScreen.setOutlineThickness(5);

        PauseScreen.initPause();

        Map.addNPC(new Zombie(1, new Point(30, 30)), map);
        Map.addNPC(new Zombie(2, new Point(30, 30)), map);
        Map.addNPC(new Zombie(3, new Point(30, 30)), map);
        Map.addNPC(new Ghost(0, new Point(15, 15)), map);

        winScreen = new WinScreen(this);
        keyTimer.setWaitTime(200);
        base.setBaseHealth(3000);
    }

    public void update() {
        // based on screen state, perform specific actions
        if (!OptionsClass.getZomAct()) {
            map.claerNPC();
        }

        switch (playLevelScreenState) {
            // if level is "running" update player and map to keep game logic for the
            // platformer level going
            case RUNNING:

                player.update();
                map.update(player);

                /*
                 * if (CurrentWeapon.getWeapon() == "Spear") {
                 * playerSpear.update();
                 * map.update(playerSpear);
                 * 
                 * } else if (CurrentWeapon.getWeapon() == "Axe") {
                 * playerSpear.update();
                 * map.update(playerSpear);
                 * 
                 * } else if (CurrentWeapon.getWeapon() == "Machete") {
                 * playerSpear.update();
                 * map.update(playerSpear);
                 * 
                 * } else if (CurrentWeapon.getWeapon() == "Katana") {
                 * playerSpear.update();
                 * map.update(playerSpear);
                 * 
                 * } else {
                 * player.update();
                 * map.update(player);
                 * }
                 * 
                 */
                // System.out.println(CurrentWeapon.getWeapon());

                baseLabel.setText(base.getBaseHealthS());
                livesLabels.setText(Deaths.getPlayerLivesString());
                timeLabels.setText(TheTimekeeperNecromaniac.getTime());
                NightLabels.setText(TheTimekeeperNecromaniac.getNight());
                NightTitleScreen.setText(TheTimekeeperNecromaniac.getNightTitle());
                TheTimekeeperNecromaniac.increment();
                if (TheTimekeeperNecromaniac.nightCheck()) {
                    int numb = TheTimekeeperNecromaniac.getNightI();
                    for (int i = 0; i <= numb * 3; i++) {
                        int xZ = rand.nextInt(500);
                        int xY = rand.nextInt(500);
                        Map.addNPC(new Zombie(i, new Point(xZ, xY)), map);
                    }
                    for (int j = 0; j <= numb + 2; j++) {
                        int xZ = rand.nextInt(500);
                        int xY = rand.nextInt(500);
                        Map.addNPC(new Ghost(j, new Point(xZ, xY)), map);
                    }
                    if (numb >= 3) {

                        int l = numb - 2;
                        for (int i = 0; i < l; i++) {
                            int xZ = rand.nextInt(500);
                            int xY = rand.nextInt(500);
                            Map.addNPC(new Knight(l, new Point(xZ, xY)), map);
                        }
                    }
                    if (numb >= 2) {

                        int g = (numb) - 1;
                        for (int i = 0; i < g; i++) {
                            int xZ = rand.nextInt(500);
                            int xY = rand.nextInt(500);
                            Map.addNPC(new ZombieBoss(g, new Point(xZ, xY)), map);
                        }

                    }
                }
                break;
            // if level has been completed, bring up level cleared screen
            case LEVEL_COMPLETED:
                winScreen.update();
                break;
        }

        // death screen
        if (base.getBaseHealth() <= 0 || Deaths.getPlayerLives() <= 0) {
            screenCoordinator.setGameState(GameState.DEATH);
        }

        // if flag is set at any point during gameplay, game is "won"
        if (map.getFlagManager().isFlagSet("hasFoundBall")) {
            playLevelScreenState = PlayLevelScreenState.LEVEL_COMPLETED;
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        // **start pause screen**
        // this is what tells if the key is down
        if (Keyboard.isKeyDown(pauseKey) && !keyLocker.isKeyLocked(pauseKey)) {
            GamePanel.setIsGamePaused(!GamePanel.isGamePaused());
            keyLocker.lockKey(pauseKey);
        }
        // this is what actually draws it
        if (GamePanel.isGamePaused()) {
            if (PauseScreen.drawPause(graphicsHandler, keyTimer, screenCoordinator, keyLocker)) {
                GamePanel.setIsGamePaused(false);
            }
        }
        // this unlocks the screen
        if (Keyboard.isKeyUp(pauseKey)) {
            keyLocker.unlockKey(pauseKey);
        }

        // **end pause screen**

        if (Inventory.getSize() > 0) {
            if (CurrentWeapon.getWeapon() == "Axe") {
                float xLoc = player.getX();
                float yLoc = player.getY();
                this.player1.setLocation(xLoc, yLoc);
                player = player1;
                // System.out.println(map.getPlayerStartPosition().x);
            } else if (CurrentWeapon.getWeapon() == "Katana") {
                float xLoc = player.getX();
                float yLoc = player.getY();
                this.playerKatana.setLocation(xLoc, yLoc);
                player = playerKatana;
                // System.out.println(map.getPlayerStartPosition().x);
            } else if (CurrentWeapon.getWeapon() == "Machete") {
                float xLoc = player.getX();
                float yLoc = player.getY();
                this.playerMachete.setLocation(xLoc, yLoc);
                player = playerMachete;
                // System.out.println(map.getPlayerStartPosition().x);
            } else if (CurrentWeapon.getWeapon() == "Spear") {
                float xLoc = player.getX();
                float yLoc = player.getY();
                this.playerSpear.setLocation(xLoc, yLoc);
                player = playerSpear;
                // System.out.println(map.getPlayerStartPosition().x);
            }
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

        if (Keyboard.isKeyDown(Key.H) && keyTimer.isTimeUp()) {

            System.out.println("axe");
            if (Inventory.contains("Axe")) {
                CurrentWeapon.equipWeapon("Axe");

            }
            keyTimer.reset();
        } else if (Keyboard.isKeyDown(Key.J) && keyTimer.isTimeUp()) {

            System.out.println("spear");
            if (Inventory.contains("Spear")) {
                CurrentWeapon.equipWeapon("Spear");
            }
            keyTimer.reset();
        } else if (Keyboard.isKeyDown(Key.K) && keyTimer.isTimeUp()) {

            System.out.println("machete");
            if (Inventory.contains("Machete")) {
                CurrentWeapon.equipWeapon("Machete");
            }
            keyTimer.reset();
        } else if (Keyboard.isKeyDown(Key.L) && keyTimer.isTimeUp()) {

            System.out.println("katana");
            if (Inventory.contains("Katana")) {
                CurrentWeapon.equipWeapon("Katana");
            }
            keyTimer.reset();
        }

        if (isInventoryScreen) {
            graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(),
                    new Color(245, 245, 220));
            inventoryLabel.draw(graphicsHandler);

            // writes the rectangle
            boolean goneThroughItems = false;
            for (int y = 30; y < 300; y = y + 70) {
                for (int x = 15; x + 70 < 800; x = x + 70) {
                    graphicsHandler.drawFilledRectangle(x, y, 60, 60, Color.BLACK);
                    graphicsHandler.drawFilledRectangle(x + 2, y + 2, 56, 56, Color.gray);

                    if (goneThroughItems == false && Inventory.getSize() > 0) {
                        int position = x / 70;

                        if (y == 30) {
                            if (Inventory.getItem(position) == "Axe") {
                                graphicsHandler.drawImage(Axe, x, y, 90, 80);
                            }

                            else if (Inventory.getItem(position) == "Axe+1") {
                                graphicsHandler.drawImage(AxePlus1, x, y, 90, 80);

                            } else if (Inventory.getItem(position) == "Spear") {
                                graphicsHandler.drawImage(Spear, x, y, 90, 80);

                            }

                            else if (Inventory.getItem(position) == "Machete") {
                                graphicsHandler.drawImage(Machete, x, y, 90, 80);
                            }

                            else if (Inventory.getItem(position) == "Katana") {
                                // System.out.println(Inventory.getSize());
                                graphicsHandler.drawImage(Katana, x, y, 90, 80);
                            }

                            if (Inventory.getItem(position) == "Steel") {
                                graphicsHandler.drawImage(Steel, x, y, 90, 80);
                            }

                            if (position == Inventory.getSize() - 1) {
                                goneThroughItems = true;
                            }
                        }

                        /*
                         * if (x == Inventory.getSize()) {
                         * goneThroughItems = true;
                         * }
                         */

                    }

                }
            }

            // crafting portion
            graphicsHandler.drawFilledRectangle(150, 400, 60, 60, Color.BLACK);
            graphicsHandler.drawFilledRectangle(150 + 2, 400 + 2, 56, 56, Color.gray);

            graphicsHandler.drawFilledRectangle(300, 400, 60, 60, Color.BLACK);
            graphicsHandler.drawFilledRectangle(300 + 2, 400 + 2, 56, 56, Color.gray);

            graphicsHandler.drawFilledRectangle(450, 400, 60, 60, Color.BLACK);
            graphicsHandler.drawFilledRectangle(450 + 2, 400 + 2, 56, 56, Color.gray);

            if (CraftingInventory.getSize() >= 1) {

                if (CraftingInventory.contains("Axe") == true) {
                    graphicsHandler.drawImage(Axe, 150, 400, 90, 80);
                } else if (CraftingInventory.contains("Axe+1") == true) {
                    graphicsHandler.drawImage(AxePlus1, 150, 400, 90, 80);
                } else if (CraftingInventory.contains("Spear") == true) {
                    graphicsHandler.drawImage(Spear, 150, 400, 90, 80);
                } else if (CraftingInventory.contains("Machete") == true) {
                    graphicsHandler.drawImage(Machete, 150, 400, 90, 80);
                } else if (CraftingInventory.contains("Katana") == true) {
                    graphicsHandler.drawImage(Katana, 150, 400, 90, 80);
                }
                if (CraftingInventory.contains("Steel")) {
                    graphicsHandler.drawImage(Steel, 260, 400, 90, 80);
                }
            }

            if (CraftingInventory.contains("Axe") && CraftingInventory.contains("Steel")) {
                // incerase damage of axe
                Inventory.upgradeAxe();
                CraftingInventory.clearInventory();
            } else if (CraftingInventory.contains("Katana") && CraftingInventory.contains("Steel")) {
                // incerase damage of axe
                Inventory.upgradeKatana();
                CraftingInventory.clearInventory();
            } else if (CraftingInventory.contains("Machete") && CraftingInventory.contains("Steel")) {
                // incerase damage of axe
                Inventory.upgradeMachete();
                CraftingInventory.clearInventory();
            } else if (CraftingInventory.contains("Spear") && CraftingInventory.contains("Steel")) {
                // incerase damage of axe
                Inventory.upgradeSpear();
                CraftingInventory.clearInventory();
            }

            if (Keyboard.isKeyDown(Key.DOWN) && keyTimer.isTimeUp()) {
                yInvSelect = Math.max(0, Math.min(4, yInvSelect + 1));
                keyTimer.reset();
            } else if (Keyboard.isKeyDown(Key.UP) && keyTimer.isTimeUp()) {
                yInvSelect = Math.max(0, Math.min(4, yInvSelect - 1));
                keyTimer.reset();
            } else if (Keyboard.isKeyDown(Key.LEFT) && keyTimer.isTimeUp()) {
                xInvSelect = Math.max(0, Math.min(10, xInvSelect - 1));
                boxSel = Math.max(1, Math.min(3, boxSel - 1));
                keyTimer.reset();
            } else if (Keyboard.isKeyDown(Key.RIGHT) && keyTimer.isTimeUp()) {
                xInvSelect = Math.max(0, Math.min(10, xInvSelect + 1));
                boxSel = Math.max(1, Math.min(3, boxSel + 1));
                keyTimer.reset();
            } else if (Keyboard.isKeyDown(Key.ENTER) && keyTimer.isTimeUp()) {
                if (Inventory.getSize() >= 1) {
                    System.out.println(xInvSelect);
                    String item = Inventory.getItem(xInvSelect);
                    if (item == "Axe") {
                        graphicsHandler.drawImage(Axe, 400, 400, 90, 80);
                        CraftingInventory.addItem("Axe");
                    } else if (item == "Axe+1") {
                        graphicsHandler.drawImage(AxePlus1, 400, 400, 90, 80);
                        CraftingInventory.addItem("Axe+1");
                    } else if (item == "Spear") {
                        graphicsHandler.drawImage(Spear, 400, 400, 90, 80);
                        CraftingInventory.addItem("Spear");
                    } else if (item == "Machete") {
                        graphicsHandler.drawImage(Machete, 400, 400, 90, 80);
                        CraftingInventory.addItem("Machete");
                    } else if (item == "Katana") {
                        // System.out.println(Inventory.getSize());
                        graphicsHandler.drawImage(Katana, 400, 400, 90, 80);
                        CraftingInventory.addItem("Katana");
                    } else if (item == "Steel") {
                        graphicsHandler.drawImage(Steel, 400, 400, 90, 80);
                        CraftingInventory.addItem("Steel");
                    }
                    keyTimer.reset();
                }
            }

            // highlights which box
            if (yInvSelect == 4) {
                graphicsHandler.drawFilledRectangle(boxSel * 150, 400, 60, 60, Color.yellow);
                graphicsHandler.drawFilledRectangle(boxSel * 150 + 2, 400 + 2, 56, 56, Color.gray);
            } else {
                graphicsHandler.drawFilledRectangle(xInvSelect * 70 + 15, yInvSelect * 70 + 30, 60, 60, Color.yellow);
                graphicsHandler.drawFilledRectangle(xInvSelect * 70 + 15 + 2, yInvSelect * 70 + 30 + 2, 56, 56,
                        Color.gray);

            }
        }

        if (Keyboard.isKeyUp(inventoryScreen)) {
            keyLocker.unlockKey(inventoryScreen);
        }

        if (!GamePanel.isGamePaused() && !isCraftingScreen && !isInventoryScreen) {
            // based on screen state, draw appropriate graphics
            switch (playLevelScreenState) {
                case RUNNING:
                    map.draw(player, graphicsHandler);

                    graphicsHandler.drawImageAlpha(backgroundFilter, 0, 0, 786, 568,
                            (float) (0.95 * Math.sin(
                                    (double) (TheTimekeeperNecromaniac.getHours() * 60
                                            + TheTimekeeperNecromaniac.getMinutes() + 6 * 60) / (60 * 24) * Math.PI
                                            * 2)));

                    // graphicsHandler.drawImageAlpha(backgroundFilter, 0, 0, 786, 568,
                    // (float) Math.sin(((double) ((TheTimekeeperNecromaniac.getHours() * 60 +
                    // TheTimekeeperNecromaniac.getMinutes())
                    // % (12 * 60)) / (12 * 60)) * Math.PI * 2));

                    livesLabels.draw(graphicsHandler);
                    timeLabels.draw(graphicsHandler);
                    baseLabel.draw(graphicsHandler);
                    NightLabels.draw(graphicsHandler);
                    NightTitleScreen.draw(graphicsHandler);

                    for (int i = 250; i < 550; i += 50) {
                        graphicsHandler.drawImage(rect, i, 500, 50, 50);
                    }

                    // just need to add variables for spaces
                    if (Inventory.contains("Axe") == true) {
                        graphicsHandler.drawImage(Axe, 255, 510, 30, 30);
                    }
                    if (Inventory.contains("Axe+1") == true) {
                        graphicsHandler.drawImage(AxePlus1, 255, 510, 30, 30);
                    }
                    if (Inventory.contains("Spear") == true) {
                        graphicsHandler.drawImage(Spear, 310, 510, 30, 30);

                    }
                    if (Inventory.contains("Machete") == true) {
                        graphicsHandler.drawImage(Machete, 360, 510, 30, 30);
                    }
                    if (Inventory.contains("Katana") == true) {
                        // System.out.println(Inventory.getSize());
                        graphicsHandler.drawImage(Katana, 410, 510, 30, 30);
                    }

                    // graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(),
                    // ScreenManager.getScreenHeight(), new Color(0, 0, 0,100));
                    // graphicsHandler.drawImage(backgroundFilter, 0, 0);
                    // System.out.println(String.format("Width: %d\tHeight: %d",
                    // ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight()));
                    break;
                case LEVEL_COMPLETED:
                    winScreen.draw(graphicsHandler);
                    break;
            }

        }
        // this is what actually draws it
        if (GamePanel.isGamePaused()) {
            if (PauseScreen.drawPause(graphicsHandler, keyTimer, screenCoordinator, keyLocker)) {
                GamePanel.setIsGamePaused(false);
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
