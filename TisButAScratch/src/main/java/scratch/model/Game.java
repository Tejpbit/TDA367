package scratch.model;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Alma Ottedag
 *
 */
public class Game implements KryoSerializable {

    private List<Player> playerList;
    private List<NpcType> npcList;
    private Room activeRoom;

    public Game() {
        npcList = new ArrayList<NpcType>();
        playerList = new ArrayList<Player>();
        activeRoom = null;

    }

    public boolean addPlayer(Player player) {
        if (activeRoom == null) {
            return false;
        }
        playerList.add(player);
        activeRoom.enterRoom(player);
        return true;
    }

    public void addNpcs() {
        for (Map.Entry<Integer, NpcType> npcTypeEntry : activeRoom.getNpcs().entrySet()) {
            this.npcList.add(npcTypeEntry.getValue());
        }
    }

    public List<Player> getPlayers() {
        return playerList;
    }
    
    public List<AbstractCharacter> getCharacters(){
        List <AbstractCharacter> characters = new ArrayList<>();
        //TODO: This needs to change with several rooms
        for (Player player:activeRoom.getPlayers()){
            characters.add((AbstractCharacter)player);
        }
        for (NpcType npc:activeRoom.getNpcs().values()){
            characters.add((AbstractCharacter)npc);
        }
        return characters;
    }

    public boolean removePlayer(Player player) {
        return playerList.remove(player)
                && activeRoom.exitRoom(player);
    }

    public void setMap(List<Room> gameMapRooms) {
		//TODO: will need to be further implemented later.
        //set the active room. Currently there should only be one.
        activeRoom = gameMapRooms.get(0);
    }

    public Room getActiveRoom() {
        return activeRoom;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        //TODO: Fix
        System.out.println("We are sending a game");
    }

    @Override
    public void read(Kryo kryo, Input input) {
        System.out.println("We are reading a game");
    }

}