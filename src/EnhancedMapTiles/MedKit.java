package EnhancedMapTiles;

import GameObject.Frame;
import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.GameObject;
import GameObject.SpriteSheet;
import Level.*;
import Utils.Point;

public class MedKit extends EnhancedMapTile {

    boolean start = false;

    public MedKit(Point location) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("MedKit.png"), 16, 16), TileType.NOT_PASSABLE);
    }

    @Override
    public void update(Player player) {
        super.update(player);

        if (start == false) {
            this.setLocation(1000, 1000);
            start = true;
        }
        if (player.overlaps(this) && player.getPlayerState() == PlayerState.WALKING) {
            Deaths.hitPlayer(-30);
            this.setLocation(1000, 1000);
        }

        if (Deaths.didGhostDie() == true) {
            this.setLocation((int) Deaths.getXGhost(), (int) Deaths.getYGhost());
        }

    }

    private boolean canMoveLeft(Player player) {
        return player.getBoundsX1() <= getBoundsX2() && player.getBoundsX2() > getBoundsX2() && canMoveX(player);
    }

    private boolean canMoveRight(Player player) {
        return player.getBoundsX2() >= getBoundsX1() && player.getBoundsX1() < getBoundsX1() && canMoveX(player);
    }

    private boolean canMoveX(Player player) {
        return (player.getBoundsY1() < getBoundsY2() && player.getBoundsY2() >= getBoundsY2()) ||
                (player.getBoundsY2() > getBoundsY1() && player.getBoundsY1() <= getBoundsY1()) ||
                (player.getBoundsY2() < getBoundsY2() && player.getBoundsY1() > getBoundsY1());
    }

    private boolean canMoveUp(Player player) {
        return player.getBoundsY1() <= getBoundsY2() && player.getBoundsY2() > getBoundsY2() && canMoveY(player);
    }

    private boolean canMoveDown(Player player) {
        return player.getBoundsY2() >= getBoundsY1() && player.getBoundsY1() < getBoundsY1() && canMoveY(player);
    }

    private boolean canMoveY(Player player) {
        return (player.getBoundsX1() < getBoundsX2() && player.getBoundsX2() >= getBoundsX2()) ||
                (player.getBoundsX2() > getBoundsX1() && player.getBoundsX1() <= getBoundsX1()) ||
                (player.getBoundsX2() < getBoundsX2() && player.getBoundsX1() > getBoundsX1());
    }

    @Override
    protected GameObject loadBottomLayer(SpriteSheet spriteSheet) {
        Frame frame = new FrameBuilder(spriteSheet.getSubImage(0, 0))
                .withScale(3)
                .build();
        return new GameObject(x, y, frame);
    }
}
