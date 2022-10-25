package Maps;

import EnhancedMapTiles.*;
import Level.EnhancedMapTile;
import Level.Map;
import Level.NPC;
import Level.Script;
import Level.Trigger;
import NPCs.Dinosaur;
import NPCs.Ghost;
import NPCs.Walrus;
import NPCs.Zombie;
import Scripts.SimpleTextScript;
import Scripts.TestMap.DinoScript;
import Scripts.TestMap.GhostScript;
import Scripts.TestMap.LostBallScript;
import Scripts.TestMap.TreeScript;
import Scripts.TestMap.WalrusScript;
import Scripts.TestMap.ZombieScript;
import Tilesets.CommonTileset;
import Utils.Direction;

import java.util.ArrayList;

// Represents a test map to be used in a level
public class TestMap extends Map {

    private Script zombieScript;
    private Script ghostScript;

    public TestMap() {
        super("test_map.txt", new CommonTileset());
        this.playerStartPosition = getMapTile(17, 20).getLocation();
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();
        enhancedMapTiles.add(new Rock(getMapTile(2, 7).getLocation()));
        enhancedMapTiles.add(new Axe(getMapTile(4, 6).getLocation()));
        enhancedMapTiles.add(new Katana(getMapTile(3, 5).getLocation()));
        enhancedMapTiles.add(new Machete(getMapTile(3, 7).getLocation()));
        enhancedMapTiles.add(new Spear(getMapTile(3, 9).getLocation()));
        enhancedMapTiles.add(new Steel(getMapTile(5, 9).getLocation()));
        enhancedMapTiles.add(new CraftingTable(getMapTile(21, 21).getLocation()));
        return enhancedMapTiles;
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        ghostScript = new GhostScript();
        Ghost ghost = new Ghost(1, getMapTile(4, 28).getLocation().subtractY(40));
        // ghost.setInteractScript(ghostScript);
        npcs.add(ghost);
        // Walrus walrus = new Walrus(1, getMapTile(4, 28).getLocation().subtractY(40));
        // walrus.setInteractScript(new WalrusScript());
        // npcs.add(walrus);

        zombieScript = new ZombieScript();
        Zombie zombie = new Zombie(2, getMapTile(13, 4).getLocation());
        // zombie.setInteractScript(zombieScript);
        npcs.add(zombie);
        // Dinosaur dinosaur = new Dinosaur(2, getMapTile(13, 4).getLocation());
        // dinosaur.setExistenceFlag("hasTalkedToDinosaur");
        // dinosaur.setInteractScript(new DinoScript());
        // npcs.add(dinosaur);

        return npcs;
    }

    @Override
    public ArrayList<Trigger> loadTriggers() {
        ArrayList<Trigger> triggers = new ArrayList<>();
        // triggers.add(new Trigger(790, 1040, 110, 10, ghostScript));
        // triggers.add(new Trigger(780, 960, 10, 80, ghostScript));
        // triggers.add(new Trigger(900, 960, 10, 80, ghostScript));
        // triggers.add(new Trigger(800, 1030, 90, 10, zombieScript));
        // triggers.add(new Trigger(790, 960, 10, 80, zombieScript));
        // triggers.add(new Trigger(890, 960, 10, 80, zombieScript));
        // triggers.add(new Trigger(790, 1030, 100, 10, new LostBallScript(),
        // "hasLostBall"));
        // triggers.add(new Trigger(790, 960, 10, 80, new LostBallScript(),
        // "hasLostBall"));
        // triggers.add(new Trigger(890, 960, 10, 80, new LostBallScript(),
        // "hasLostBall"));
        return triggers;
    }

    @Override
    public void loadScripts() {
        getMapTile(21, 19).setInteractScript(new SimpleTextScript("Cat's house"));

        getMapTile(7, 26).setInteractScript(new SimpleTextScript("Walrus's house"));

        getMapTile(20, 4).setInteractScript(new SimpleTextScript("Dino's house"));

        getMapTile(2, 6).setInteractScript(new TreeScript());
    }
}
