package scratch.model;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import scratch.model.weapons.Weapon;
import scratch.utils.Cooldown;

import javax.annotation.concurrent.Immutable;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * The interface for all Characters. Every character has a given health,
 * position, damage, movement speed and weapon.
 *
 * @author Alma Ottedag revised 2014-03-27 by Ivar Josefsson
 */
@Immutable
@Root
public class GameCharacter implements KryoSerializable, IMovableEntity {

    @Element(type = Rectangle2D.Double.class, required = false)
    private Rectangle2D.Double unitTile = new Rectangle2D.Double(0, 0, 32, 32);
    @Element(name = "weapon")//(type = WeaponPath.class)
    private Weapon weapon;
    @Element
    private int health;
    @Element
    private int movementSpeed;
    @Attribute
    private int id;
    @Element(type = Direction.class, required = false)
    private Direction moveDirection = Direction.NONE;
    @Element(type = Direction.class, required = false)
    private Direction lookingDirection = Direction.SOUTH;
    @Element
    private String imagePath;
    @Element(required = false)
    private boolean interactIsCooledDown = true;
    private Vector2D nextMoveDirection;
    private boolean interacting;
    private boolean attacking;


    private final Runnable cooldownReset = new Runnable() {
        @Override
        public void run() {
            interactIsCooledDown = true;
        }
    };

    final private List<CharacterChangeListener> listeners = new ArrayList<>();

    public GameCharacter(Rectangle2D.Double unitTile, Weapon weapon, int health, int movementSpeed, int id, String imagePath) {
        this.unitTile = new Rectangle2D.Double(unitTile.getX(), unitTile.getY(), unitTile.getWidth(), unitTile.getHeight());
        this.weapon = weapon;
        this.health = health;
        this.movementSpeed = movementSpeed;
        this.id = id;
        this.imagePath = imagePath;
        nextMoveDirection = new Vector2D();
    }

    public void registerListener(CharacterChangeListener listener) {
        listeners.add(listener);
    }

    GameCharacter() {
        super();
        nextMoveDirection = new Vector2D();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void performInteractCooldown() {
        interactIsCooledDown = false;
        Cooldown.cooldown(500, cooldownReset);
    }

    public void removeListener(CharacterChangeListener listener) {
        listeners.remove(listener);
    }

    public void takeDamage(int dmg) {
        health = health - Math.abs(dmg);
        if (health < 0) {
            health = 0;
        }
    }

    private void calculateMoveDirection(Vector2D newPosition) {
        if (getPosition().equals(newPosition)) {
            moveDirection = Direction.NONE;
            return;
        }

        final Rectangle2D.Double unitTile = getTile();
        final double diffX = newPosition.getX() - unitTile.x;
        final double diffY = newPosition.getY() - unitTile.y;

        // 517.5 =
        // 180 to avid negative angles
        //+ 337.5 (360 - 22.5)
        final double theta = (Math.toDegrees(Math.atan2(diffX, diffY)) + 517.5) % 360;

        final Direction[] directions = {
            Direction.NORTHWEST,
            Direction.WEST,
            Direction.SOUTHWEST,
            Direction.SOUTH,
            Direction.SOUTHEAST,
            Direction.EAST,
            Direction.NORTHEAST,
            Direction.NORTH
        };
        lookingDirection = directions[(int) theta / 45];
        moveDirection = directions[(int) theta / 45];
    }

    public void update() {
        final Vector2D newPosition = calculateNewPosition();
        calculateMoveDirection(newPosition);

        for (final CharacterChangeListener listener : getListeners()) {
            listener.handleCharacterMovement(this, newPosition);

            if (isInteracting()) {
                interact();
            }

            if (isAttacking()) {
                performAttack();
            }
        }
    }

    protected Vector2D calculateNewPosition() {
        return new Vector2D(
                getPosition().getX() + nextMoveDirection.getX() * movementSpeed,
                getPosition().getY() + nextMoveDirection.getY() * movementSpeed
        );
    }

    @Override
    public void setPosition(Vector2D position) {
        unitTile.setRect(position.getX(), position.getY(), unitTile.getWidth(), unitTile.getHeight());
    }

    public void setInteracting(boolean interacting) {
        this.interacting = interacting;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public void setNextMoveDirection(Vector2D moveDirection) {
        this.nextMoveDirection = moveDirection.getNormalisedVector();
    }

    public Vector2D getNextMoveDirection() {
        return nextMoveDirection;
    }

    public boolean isInteracting() {
        return interacting && interactIsCooledDown;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Rectangle2D.Double getTile() {
        return unitTile;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public int getHealth() {
        return health;
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }

    public int getId() {
        return id;
    }

    public Direction getMoveDirection() {
        return moveDirection;
    }

    public Direction getLookingDirection() {
        return lookingDirection;
    }

    @Override
    public void setMoveDirection(Direction direction) {
        this.moveDirection = direction;
    }

    public int getDamage() {
        return weapon.getDamage();
    }

    @Override
    public Vector2D getPosition() {
        return new Vector2D(unitTile.getX(), unitTile.getY());
    }

    public String getImagePath() {
        return imagePath;
    }

    public Attack getAttack() {
        final Rectangle2D.Double origin = new Rectangle2D.Double(
                unitTile.x + 32 * lookingDirection.getX(),
                unitTile.y + 32 * lookingDirection.getY(),
                weapon.getAttackArea().getWidth(),
                weapon.getAttackArea().getHeight());
        return new Attack(origin, new Vector2D(lookingDirection.getX()*movementSpeed, lookingDirection.getY()*2), weapon, this.getClass());
    }

    public void performAttack() {
        if (weapon.hasCooledDown()) {
            for (final CharacterChangeListener listener : listeners) {
                listener.handleCharacterAttack(this);
            }
            weapon.startCooldown();
        }
    }

    public void interact() {
        for (final CharacterChangeListener listener : listeners) {
            listener.handleCharacterInteraction(this);
        }
        performInteractCooldown();
    }

    public boolean isAlive() {
        return getHealth() > 0;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GameCharacter)) {
            return false;
        }

        final GameCharacter character = (GameCharacter) o;

        return id == character.id;
    }

    @Override
    public final int hashCode() {
        return 31 * id;
    }

    public List<CharacterChangeListener> getListeners() {
        return listeners;
    }

    public void setMovementSpeed(int movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public void setCharacter(GameCharacter character) {
        unitTile = character.getTile();
        weapon = character.getWeapon();
        health = character.getHealth();
        movementSpeed = character.getMovementSpeed();
        moveDirection = character.getMoveDirection();
        lookingDirection = character.getLookingDirection();
        imagePath = character.getImagePath();
        attacking = character.isAttacking();
        interacting = character.isInteracting();
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, unitTile);
        kryo.writeObject(output, weapon);
        kryo.writeObject(output, health);
        kryo.writeObject(output, movementSpeed);
        kryo.writeObject(output, id);
        kryo.writeObject(output, moveDirection);
        kryo.writeObject(output, lookingDirection);
        kryo.writeObject(output, imagePath);
        kryo.writeObject(output, attacking);
        kryo.writeObject(output, interacting);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        unitTile = kryo.readObject(input, Rectangle2D.Double.class);
        weapon = kryo.readObject(input, Weapon.class);
        health = kryo.readObject(input, Integer.class);
        movementSpeed = kryo.readObject(input, Integer.class);
        id = kryo.readObject(input, Integer.class);
        moveDirection = kryo.readObject(input, Direction.class);
        lookingDirection = kryo.readObject(input, Direction.class);
        imagePath = kryo.readObject(input, String.class);
        attacking = kryo.readObject(input, Boolean.class);
        interacting = kryo.readObject(input, Boolean.class);
    }

}
