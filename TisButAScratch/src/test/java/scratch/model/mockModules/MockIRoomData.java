package scratch.model.mockModules;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import scratch.construction.SlickMap;
import scratch.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pippin on 5/3/14.
 */
public class MockIRoomData implements IRoomData{
    private List<Player> players = new ArrayList<Player>();
    private List<INpc> npcs = new ArrayList<INpc>();
    private IMap map;


    public void setup(){
        try{
          map = new SlickMap(new TiledMap("res/untitled.tmx"));
        } catch (SlickException e){
            e.printStackTrace();
        }
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public List<INpc> getNpcs() {

        return npcs;
    }

	@Override
	public List<IInteractiveObject> getInteractiveObjects() {
		return null;
	}

	@Override
    public IMap getMap() {
        if(map == null){
            setup();
        }
        return map;
    }
}
