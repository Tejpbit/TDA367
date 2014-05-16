package scratch.controller;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import scratch.construction.MyMatcher;
import scratch.construction.RoomFactory;
import scratch.construction.SlickMap;
import scratch.model.*;
import scratch.network.NetworkServer;
import scratch.view.CharacterView;
import scratch.view.RoomView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The main controller class to control updates, rendering, initiating and
 * delegate tasks to other controllers.
 *
 * @author Anna Nylander
 *
 */
public final class ServerController extends Listener implements org.newdawn.slick.Game {

    private final NetworkServer networkServer;
    private final Game game;
    private final List<PlayerController> playerControllerList;
    private final List<NpcController> npcControllerList;
    private final List<RoomController> roomControllerList;

    public ServerController(Game game) {
        super();
        this.game = game;
        networkServer = new NetworkServer();
        playerControllerList = new ArrayList<>();
        roomControllerList = new ArrayList<>();
        npcControllerList = new ArrayList<>();
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        //TODO: This will need to change when we read from XML.
        gameContainer.setTargetFrameRate(60);
        final RoomFactory roomFactory = new RoomFactory();
        game.setMap(roomFactory.getRooms());

	    //NOTE: The player created here wont have a room as listeners. this is added when the player
        //enters the first room. (game.addPlayer(newPlayer); enters a player to the first room)
        Player newPlayer = loadPlayer("StandardPlayer", new Vector2D(20, 20), 1, gameContainer);
        System.out.println(newPlayer.toString());
        game.addPlayer(newPlayer);

        for (final Room room : roomFactory.getRooms()) {
            final TiledMap map = ((SlickMap) room.getMap()).getMap();
            //TODO refactor here. SlickMap and TiledMapPlus will be merged probably. Tejp fix
            RoomController roomController = new RoomController(room, new RoomView(map));
            roomControllerList.add(roomController);

            for (final NpcType npc : room.getNpcs()) {
                NpcController npcController = new NpcController(npc, new CharacterView(npc));
                npcController.addListener(networkServer);
                npcControllerList.add(npcController);
            }
            for (final Player player : room.getPlayers()) {
                PlayerController playerController = new PlayerController(player, new CharacterView(player));
                playerController.addListener(networkServer);
                playerControllerList.add(playerController);
            }
        }

        networkServer.start(this);

    }

    private Player loadPlayer(String file, Vector2D position, int id, GameContainer gameContainer) {
        final Serializer serializer = new Persister(new MyMatcher());
        final StringBuilder fileBuild = new StringBuilder();
        fileBuild.append("res/");
        fileBuild.append(file);
        fileBuild.append(".xml");
        final File source = new File(fileBuild.toString());
        Player player;
        try {
            player = serializer.read(Player.class, source);

        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
        player.setPosition(position);
        player.setId(id);
        player.setPlayerInput(new PlayerInput(id, gameContainer.getInput()));
        return player;
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        for (final PlayerController playerController : playerControllerList) {
            playerController.updatePlayer();
        }

        for (final NpcController npcController : npcControllerList) {
            npcController.updateNpc();
        }

        for (final RoomController roomController : roomControllerList) {
            roomController.updateRoom();
        }
    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {

    }

    @Override
    public boolean closeRequested() {
        return true;
    }

    @Override
    public String getTitle() {
        return "'Tis but a Scratch";
    }

    @Override
    public void connected(Connection connection) {
        connection.sendTCP(game.getActiveRoom());
    }

    @Override
    public void received(Connection connection, Object object) {
        //TODO We recieved something
        if (object instanceof PlayerInput){
            game.getPlayers().get(0).setPlayerInput((PlayerInput)object);
        }
    }
}
