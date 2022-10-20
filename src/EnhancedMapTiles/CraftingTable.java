package EnhancedMapTiles;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.GameObject;
import GameObject.SpriteSheet;
import Level.EnhancedMapTile;
import Level.Player;
import Level.PlayerState;
import Level.TileType;
import Utils.Direction;
import Utils.Point;

// This class is for the end level gold box tile
// when the player touches it, it will tell the player that the level has been completed
public class CraftingTable extends EnhancedMapTile {
    public CraftingTable(Point location) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("craft.png"), 16, 16), TileType.NOT_PASSABLE);
    }

    @Override
    public void update(Player player) {
        super.update(player);
        if (player.overlaps(this) && player.getPlayerState() == PlayerState.WALKING)
        {
            this.setLocation(1000, 1000);
        }
    }

    @Override
    protected GameObject loadBottomLayer(SpriteSheet spriteSheet) {
        Frame frame = new FrameBuilder(spriteSheet.getSubImage(0, 0))
                .withScale(3)
                .build();
        return new GameObject(x, y, frame);
    }
}
