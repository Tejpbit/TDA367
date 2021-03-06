package scratch.model;

import com.google.inject.Inject;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a single room and the contents in it.
 *
 * @author Ivar Josefsson
 *
 */
public final class Room implements IRoomData, CharacterChangeListener, DoorHelper {

    @Inject
    private List<IMovableEntity> movableEntities;
    private List<GameCharacter> characters;
    private final Map<GameCharacter, Vector2D> characterMovementMap = new HashMap<>();
    private final List<GameCharacter> charactersInteracting = new ArrayList<>();
    private List<Attack> attacks = new ArrayList<>();
    private IMap map;
    private List<InteractiveObject> interactiveObjects;
    private DoorHandler doorHandler;

    public Room(IMap collisionMap, DoorHandler doorHandler) {
        this.map = collisionMap;
        this.doorHandler = doorHandler;
        characters = new ArrayList();
        interactiveObjects = new ArrayList<>();
        movableEntities = new ArrayList<>();
    }

    public void update() {
        dealDamage();
        updateAttacks();
        updateCharacterMovements();
        updateCharacterInteractions();

    }

    public boolean isActive() {
        return !(getLivingPlayers().isEmpty());
    }

    private void updateAttacks() {
        List<Attack> remainingAttacks = new ArrayList<>();
        for (final Attack attack : attacks) {
            if (!(isColliding(attack, attack.getPosition())) && attack.isRemaining()) {
                remainingAttacks.add(attack);
                attack.update();
            }
        }
        attacks = remainingAttacks;

    }

    private void updateCharacterInteractions() {
        for (final GameCharacter character : charactersInteracting) {
            for (final InteractiveObject interactiveObject : interactiveObjects) {
                if (character.getTile().intersects(interactiveObject.getUnitTile())) {
                    final String objectType = interactiveObject.getType();
                    if ("door".equals(objectType)) {

                        doorHandler.interactHappened(this, character, interactiveObject);
                        break;
                    }
                }
            }
        }
        charactersInteracting.clear();
    }

    private void updateCharacterMovements() {
        for (final Map.Entry<GameCharacter, Vector2D> inputEntry : characterMovementMap.entrySet()) {
            final GameCharacter character = inputEntry.getKey();
            character.setPosition(allowedPosition(character, inputEntry.getValue()));
        }
        characterMovementMap.clear();
    }

    private void dealDamage() {
        for (final Attack attack : attacks) {
            for (final GameCharacter character : characters) {
                if (character.getTile().intersects(attack.getAttackTile()) && attack.dealDamage(character.getClass())) {
                    character.takeDamage(attack.getDamage());
                }
            }
        }
    }

    /**
     * Called to determine the best allowed position
     *
     * @param entityToPlace the character with being moved
     * @param toPosition the position we want to end at
     * @return the best allowed position
     */
    private Vector2D allowedPosition(IMovableEntity entityToPlace, Vector2D toPosition) {
        final Rectangle2D.Double unitTile = entityToPlace.getTile();
        final double newX = toPosition.getX();
        final double newY = toPosition.getY();
        final double oldX = unitTile.getX();
        final double oldY = unitTile.getY();
        double returnX = oldX;
        double returnY = oldY;

        //Check if new X position is allowed
        if (!isColliding(entityToPlace, new Vector2D(newX, oldY))) {
            returnX = newX;
        }
        //Check if new Y position is allowed
        if (!isColliding(entityToPlace, new Vector2D(oldX, newY))) {
            returnY = newY;
        }
        return new Vector2D(returnX, returnY);
    }

    /**
     * Checks if there's a collision at the given coordinates
     *
     *
     * @param entityToPlace A "hitbox" of the object to place at placeToPut
     * @param placeToPut the place to put objectToPlace at
     * @return true if there is a collision
     */
    private boolean isColliding(IMovableEntity entityToPlace, Vector2D placeToPut) {
        final int tileSize = 32;
        final Rectangle2D.Double placeToPutArea = new Rectangle2D.Double(placeToPut.getX(), placeToPut.getY(), tileSize, tileSize);

        if (placeToPut.getX() < 0 || getMapWidth() < (placeToPut.getX() + entityToPlace.getTile().getWidth())) {
            return true;
        }
        if (placeToPut.getY() < 0 || getMapHeight() < (placeToPut.getY() + entityToPlace.getTile().getHeight())) {
            return true;
        }
        for (final IMovableEntity entity : movableEntities) {

            if (isCollidingWithBox(entityToPlace, placeToPutArea, entity)) {
                return true;
            }

            if (isCollidingWithCharacter(entityToPlace, placeToPutArea, entity)) {
                return true;
            }
        }

        final Rectangle2D.Double unitTile = entityToPlace.getTile();
        final Vector2D northWest = new Vector2D(placeToPut.getX() + 1, placeToPut.getY() + 1);
        final Vector2D northEast = new Vector2D(placeToPut.getX() + unitTile.getWidth() - 1, placeToPut.getY() + 1);
        final Vector2D southWest = new Vector2D(placeToPut.getX() + 1, placeToPut.getY() + unitTile.getHeight() - 1);
        final Vector2D southEast = new Vector2D(placeToPut.getX() + unitTile.getWidth() - 1, placeToPut.getY() + unitTile.getHeight() - 1);

        return map.isColliding(northWest)
                || map.isColliding(northEast) || map.isColliding(southEast) || map.isColliding(southWest);
    }

    private boolean isCollidingWithBox(IMovableEntity entityToPlace, Rectangle2D.Double placeToPutArea, IMovableEntity entity) {
        if (entity.getTile().intersects(placeToPutArea)
                && !entity.getTile().equals(entityToPlace.getTile())
                && entity instanceof MovableObject) {
            if (entityToPlace.getClass() == GameCharacter.class) {
                updateBoxPosition((GameCharacter) entityToPlace, (MovableObject) entity);
            }
            return true;
        }
        return false;
    }

    private boolean isCollidingWithCharacter(IMovableEntity entityToPlace, Rectangle2D.Double placeToPutArea, IMovableEntity entity) {
        if (entity instanceof GameCharacter) {
            final GameCharacter gameCharacter = (GameCharacter) entity;
            if (gameCharacter.getTile().intersects(placeToPutArea)
                    && !(entityToPlace.getTile().equals(gameCharacter.getTile()))
                    && gameCharacter.isAlive()){
                return true;
            }
        }
        return false;
    }

    private void updateBoxPosition(GameCharacter character, IMovableEntity interactiveObject) {
        final Vector2D nextMoveDirection = character.getNextMoveDirection();
        final Rectangle2D.Double boxArea = interactiveObject.getTile();
        final Vector2D newPos = new Vector2D(boxArea.getX() + nextMoveDirection.getX() * character.getMovementSpeed(), boxArea.getY() + nextMoveDirection.getY() * character.getMovementSpeed());
        if (!(isColliding(interactiveObject, newPos))){
            interactiveObject.setPosition(newPos);
        }
        
    }

    @Override
    public Vector2D getClosestPlayerPosition(Vector2D npcPosition) {
        if (getLivingPlayers().isEmpty()) {
            return null;
        }

        GameCharacter closestPlayer = null;
        for (final GameCharacter player : getLivingPlayers()) {
            if (closestPlayer == null || npcPosition.distance(closestPlayer.getPosition()) > npcPosition.distance(player.getPosition())) {
                closestPlayer = player;
            }
        }
        return closestPlayer.getPosition();
    }

    public void addInteractiveObject(InteractiveObject interactiveObject) {
        if (interactiveObject instanceof MovableObject) {
            movableEntities.add((MovableObject) interactiveObject);
        }
        this.interactiveObjects.add(interactiveObject);
    }

    public boolean addCharacter(GameCharacter character) {
        character.registerListener(this);
        movableEntities.add(character);
        return characters.add(character);
    }

    public boolean removeCharacter(GameCharacter character) {
        character.removeListener(this);
        movableEntities.remove(character);
        return characters.remove(character);
    }

    @Override
    public void handleCharacterMovement(GameCharacter character, Vector2D movement) {
        characterMovementMap.put(character, movement);
    }

    @Override
    public void handleCharacterAttack(GameCharacter character) {
        attacks.add(character.getAttack());
    }

    /**
     *
     * @return: the total height of the map in pixels
     */
    private int getMapHeight() {
        return map.getHeight();
    }

    /**
     *
     * @return: the total width of the map in pixels
     */
    private int getMapWidth() {
        return map.getWidth();
    }

    public int getId() {
        return map.getId();
    }

    @Override
    public List<GameCharacter> getCharacters() {
        return characters;
    }

    public List<Attack> getAttacks() {
        return attacks;
    }

    public void setAttacks(List<Attack> attacks) {
        this.attacks = attacks;
    }

    public List<InteractiveObject> getInteractiveObjects() {
        return interactiveObjects;
    }

    @Override
    public IMap getMap() {
        return map;
    }

    public Map<GameCharacter, Vector2D> getCharacterMovementMap() {
        return characterMovementMap;
    }

    public List<GameCharacter> getCharacterInteractAreaMap() {
        return charactersInteracting;
    }

    public DoorHandler getDoorHandler() {
        return doorHandler;
    }

    @Override
    public void handleCharacterInteraction(GameCharacter character) {
        charactersInteracting.add(character);
    }

    private List<GameCharacter> getPlayers() {
        final List<GameCharacter> players = new ArrayList<>();
        for (final GameCharacter character : characters) {
            if (character.getClass() != NpcType.class) {
                players.add(character);
            }
        }
        return players;
    }

    private List<GameCharacter> getLivingPlayers() {
        final List<GameCharacter> players = new ArrayList<>();
        for (final GameCharacter player : getPlayers()) {
            if (player.isAlive()) {
                players.add(player);
            }
        }
        return players;
    }
}
