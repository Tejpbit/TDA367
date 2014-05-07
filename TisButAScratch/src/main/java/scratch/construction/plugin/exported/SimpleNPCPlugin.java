package scratch.construction.plugin.exported;


import com.google.inject.Inject;
import scratch.construction.plugin.AIPlugin;
import scratch.construction.plugin.Pluggable;
import scratch.model.*;

import java.awt.geom.Point2D;

/**
 * Moves NPC toward the Player.
 */
@AIPlugin(id = 1)
public final class SimpleNPCPlugin implements Pluggable<SimpleNPCPlugin>, INPCMove{
	@Inject
    @Override

    public SimpleNPCPlugin get() {
        return this;
    }

    @Override
    public Vector2D calculateNewPosition(IRoomData roomData, INpc npc) {
        Vector2D playerPos = roomData.getPlayers().get(0).getPosition();
        Vector2D npcPos = npc.getPosition();
        Vector2D directionVector = new Vector2D(new Point2D.Double(npcPos.getX(), npcPos.getY()), new Point2D.Double(playerPos.getX(), playerPos.getY())).getNormalisedVector();
        int moveSpeed = npc.getMovementSpeed();
        return new Vector2D((npcPos.getX() + directionVector.getX()) * moveSpeed, (npcPos.getY() + directionVector.getY()) * moveSpeed);

    }
	public boolean isAttacking(IRoomData roomData, INpc npc){
		for(Player player: roomData.getPlayers()){
			if(closeEnough(player.getPosition(), npc.getPosition())){
				return true;
			}
		}
		return false;
	}

	private boolean closeEnough(Vector2D playerPos, Vector2D npcPos){
		//+16 to get the center of the character, playerpos returns the point of the right high corner
		Vector2D vector2D= new  Vector2D(new Point2D.Double(playerPos.getX()+16, playerPos.getY()+16), new Point2D.Double(npcPos.getX()+16, npcPos.getY()+16));
		if(vector2D.getMagnitude()<= 48){
			return true;
		}
		return false;
	}
}