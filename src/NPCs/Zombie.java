package NPCs;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Engine.PlayMusic;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.Camera;
import Level.NPC;
import Level.Player;
import Level.base;
import Utils.Direction;
import Utils.Point;
import Utils.Stopwatch;

import java.util.HashMap;

// This class is for the dinosaur NPC
public class Zombie extends NPC {

    PlayMusic music = new PlayMusic();
    protected Stopwatch hitTimer = new Stopwatch();

    static int lives = 5;

    float dx;
    float dy;

    public Zombie(int id, Point location) {
        super(id, location.x, location.y, new SpriteSheet(ImageLoader.load("Zombie.png"), 14, 17), "STAND_LEFT");
        hitTimer.setWaitTime(200);
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

        if(this.getLocation().x > 480){
            dx = 1 - this.getLocation().x;
        } else if (this.getLocation().x < 520) {
            dx = 1 + this.getLocation().x;
        }

        if(this.getLocation().y > 480 ){
            dy = 1 - this.getLocation().x;
        } else if (this.getLocation().y < 520) {
            dy = 1 + this.getLocation().y;
        }

        this.walk(dx > 0 ? Direction.RIGHT : Direction.LEFT, 1);
        this.walk(dy > 0 ? Direction.DOWN : Direction.UP, 1);

        if(this.getLocation().x == 480 && hitTimer.isTimeUp()){
            base.baseDam();
            hitTimer.reset();
        } else if (this.getLocation().x == 520 && hitTimer.isTimeUp()) {
            base.baseDam();
            hitTimer.reset();
        } else if(this.getLocation().y == 480 && hitTimer.isTimeUp()){
            base.baseDam();
            hitTimer.reset();
        } else if (this.getLocation().y == 520 && hitTimer.isTimeUp()) {
            base.baseDam();
            hitTimer.reset();
        }

         //System.out.printf("Zombie Position: %s\n", this.getLocation());
         //System.out.printf("Player Position: %s\n", player.getLocation());

        if (player.overlaps(this)) {
            music.playDG();
            lives = lives - 1;
            System.out.println(lives);
            player.setPlayerLives(player.getPlayerLivesI() - 1);
            hitTimer.reset();
        }

        if (lives < 1) {
            this.setLocation(1000, 1000);

        }

    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {

        if (lives > 0) {
            super.draw(graphicsHandler);
        }
    }
}
