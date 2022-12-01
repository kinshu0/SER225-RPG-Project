package NPCs;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Engine.PlayMusic;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.Camera;
import Level.CurrentWeapon;
import Level.Deaths;
import Level.Inventory;
import Level.NPC;
import Level.*;
import Level.base;
import Utils.Direction;
import Utils.Point;
import Utils.Stopwatch;
import java.util.HashMap;
import Maps.TestMap;
import Screens.deathScreen;

// This class is for the dinosaur NPC
public class Ghost extends NPC {

    int lives = 20;

    PlayMusic music = new PlayMusic();
    protected Stopwatch hitTimer = new Stopwatch();
    protected Stopwatch hitTimer2 = new Stopwatch();

    float dx;
    float dy;
    boolean died = false;

    public Ghost(int id, Point location) {
        super(id, location.x, location.y, new SpriteSheet(ImageLoader.load("Ghost.png"), 14, 17), "STAND_LEFT");
        hitTimer.setWaitTime(500);
        hitTimer2.setWaitTime(300);
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {
            {
                put("STAND_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(0, 0))
                                .withScale(3)
                                .withBounds(4, 5, 5, 10)
                                .build()
                });
                put("STAND_RIGHT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(0, 0))
                                .withScale(3)
                                .withBounds(4, 5, 5, 10)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build()
                });

                put("WALK_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(1, 0), 200)
                                .withScale(3)
                                .withBounds(4, 5, 5, 10)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(1, 1), 200)
                                .withScale(3)
                                .withBounds(4, 5, 5, 10)
                                .build()
                });

                put("WALK_RIGHT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(1, 0), 200)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .withBounds(4, 5, 5, 10)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(1, 1), 200)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .withBounds(4, 5, 5, 10)
                                .build()
                });
            }
        };
    }

    @Override
    public void update(Player player) {
        super.update(player);

        if (!player.overlaps(this)) {
            followGameObject(player);
            // player.setPlayerLives(player.getPlayerLivesI() - 1);
        }

        if (CurrentWeapon.getWeapon() == "Axe") {
            if (player.axeRange(this, getX(), getY()) && hitTimer.isTimeUp()) {
                music.playDG();
                lives = lives - (5 + Inventory.returnAxe());
                System.out.println(lives);
                // player.setPlayerLives(player.getPlayerLivesI() - 1);
                hitTimer.reset();
            }
        }

        if (CurrentWeapon.getWeapon() == "Spear") {
            if (player.spearRange(this, getX(), getY()) && hitTimer.isTimeUp()) {
                music.playDG();
                lives = lives - (3 + Inventory.returnSpear());
                System.out.println(lives);

                // player.setPlayerLives(player.getPlayerLivesI() - 1);
                hitTimer.reset();
            }

        }

        if (CurrentWeapon.getWeapon() == "Machete") {
            if (player.spearRange(this, getX(), getY()) && hitTimer.isTimeUp()) {
                music.playDG();
                lives = lives - (6 + Inventory.returnMachete());
                System.out.println(lives);
                // player.setPlayerLives(player.getPlayerLivesI() - 1);
                hitTimer.reset();
            }
        }

        if (CurrentWeapon.getWeapon() == "Katana") {
            if (player.spearRange(this, getX(), getY()) && hitTimer.isTimeUp()) {
                music.playDG();
                lives = lives - (4 + Inventory.returnKatana());
                System.out.println(lives);
                // player.setPlayerLives(player.getPlayerLivesI() - 1);
                hitTimer.reset();
            }
        }

        if (player.overlaps(this) && hitTimer2.isTimeUp()) {
            music.playDG();
            // System.out.println(lives);
            // player.setPlayerLives(player.getPlayerLivesI() - 1);
            Deaths.hitPlayer(1);
            hitTimer2.reset();
        }

        if (lives < 1) {
            if (died == false) {
                Deaths.addGhostDeath();
                Deaths.lastGhostX(getX());
                Deaths.lastGhostY(getY());
                died = true;
            }
            this.setLocation(1000, 1000);
        }
    }

    public boolean isGhostDead() {
        if (lives < 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {

        if (lives > 1) {
            super.draw(graphicsHandler);
        }
    }

}
