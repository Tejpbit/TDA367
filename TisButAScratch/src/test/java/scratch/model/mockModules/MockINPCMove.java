package scratch.model.mockModules;

import scratch.model.INPCMove;
import scratch.model.IRoomData;
import scratch.model.NpcType;
import scratch.model.Vector2D;

/**
 * Created by Anna on 2014-05-05.
 */
//NOT yet done, will collect code from existing class to fill this out.
public class MockINPCMove implements INPCMove {

    @Override
    public Vector2D calculateNewPosition(NpcType npc) {
        return null;
    }

    @Override
    public boolean isAttacking(NpcType npc) {
        return false;
    }

    @Override
    public void setRoomData(IRoomData roomData) {

    }
}
