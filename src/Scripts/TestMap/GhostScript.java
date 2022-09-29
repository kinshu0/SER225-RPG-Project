package Scripts.TestMap;

import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import GameObject.Frame;
import Level.*;
import Utils.Direction;
import Utils.Point;

// script for talking to dino npc
// the script is segmented -- it has multiple setups, cleanups, and executions based on its current action
public class GhostScript extends Script<NPC> {

    private int sequence = 0;
    private int amountMoved = 0;
    private int speed = 2;

    @Override
    protected void setup() {
    }

    @Override
    protected void cleanup() {
    }

    @Override
    public ScriptState execute() {
        entity.walk(Direction.RIGHT, speed);
        amountMoved += speed;
        if (amountMoved == 36) {
            speed *= -1;
        } else if (amountMoved == -36) {
            speed *= -1;
        }
        // sequence++;
        // System.out.printf("Ghost: %d\n", sequence);
        return ScriptState.RUNNING;
    }
}

