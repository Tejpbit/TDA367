package scratch.model;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alma Ottedag
 *
 */
public class Game implements KryoSerializable {

    private List<Player> playerList;
    private Room activeRoom;

    public Game() {
        playerList = new ArrayList<>();
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

    public List<NpcType> getNpcs() {
        return activeRoom.getNpcs();
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
        for (NpcType npc:activeRoom.getNpcs()){
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
