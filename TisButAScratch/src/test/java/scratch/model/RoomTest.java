package scratch.model;

import com.google.inject.*;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import scratch.model.mockModules.MockModule;

import java.awt.geom.Rectangle2D;

/**
 * Created by Anna on 2014-05-02.
 */
public class RoomTest extends TestCase {
	private IMap map;
	private Room room;

	@Before
	public void setUp() {
		Injector injector = Guice.createInjector(new MockModule());
		map = injector.getInstance(IMap.class);
		room= new Room(map);
	}

	public void tearDown() throws Exception {

	}

	//made to create mock-players used in tests
	private Player createPlayerForTest(int id) {
		Injector injector = Guice.createInjector(new MockModule());
		IPlayerInput playerInput = injector.getInstance(IPlayerInput.class);
		return new Player(playerInput, new Rectangle2D.Double(0,0,32,32),id);
	}

	public void testUpdate() throws Exception {

	}

	public void testUpdateCharater(ICharacter character) throws Exception {

	}
	//the function MapCollision is used in AllowedPosition so I'm just testing one of them.
	public void testAllowedPosition(Rectangle2D.Double unitTile, Vector2D toPosition){
		Player player1 = createPlayerForTest(0);
		room.enterRoom(player1);
		//not really sure of how to test this. Might need help with this.
	}

	public void testMapCollision(Rectangle2D.Double objectToPlace, Vector2D placeToPut){

	}

	public void testDealDamage(){

	}

	public void testEnterRoom() throws Exception {
		assertTrue(room.getPlayers().isEmpty());
		Player player1 = createPlayerForTest(0);
		Player player2 = createPlayerForTest(0);
		room.enterRoom(player1);
		room.enterRoom(player2);
		assertTrue(room.getPlayers().contains(player1) && room.getPlayers().contains(player2));
	}

	public void testExitRoom() throws Exception {
		Player player1 = createPlayerForTest(0);
		Player player2 = createPlayerForTest(1);
		room.enterRoom(player1);
		room.enterRoom(player2);
		assertTrue(!room.getPlayers().isEmpty());
		room.exitRoom(player1);
		assertTrue(!room.getPlayers().contains(player1) && room.getPlayers().get(0).getID()==1);
		assertEquals(1,room.getPlayers().get(0).getID());
		room.exitRoom(player2);
		assertTrue(room.getPlayers().isEmpty());
	}

	@Test(expected=NullPointerException.class)
	public void testExitRoomException() throws Exception {
		Player player = createPlayerForTest(0);
		room.exitRoom(player);
		assertTrue(room.getPlayers().isEmpty());
	}
	//I dont think I need to test this since I use it i so many other tests.
	public void testGetPlayers() throws Exception {
	}

	public void testGetNpcs() throws Exception {
		assertEquals(1,room.getNpcs().size()); //change 1 to expected number of npcs expected on the specific map.
	}

}