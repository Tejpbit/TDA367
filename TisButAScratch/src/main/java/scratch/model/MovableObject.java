package scratch.model;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.awt.geom.Rectangle2D;

import java.util.Map;

/**
 * Created by Anna on 2014-05-23.
 */
public final class MovableObject extends InteractiveObject implements IMovableEntity {

    private Direction moveDirection;

    public MovableObject(String name, String type, int x, int y, int width, int height, Map<String, String> properties) {
        super(name, type, x, y, width, height, properties);
        moveDirection = Direction.NONE;
    }

    public MovableObject() {
        super();
    }

    @Override
    public Direction getMoveDirection() {
        return moveDirection;
    }

    @Override
    public void setMoveDirection(Direction moveDirection) {
        this.moveDirection = moveDirection;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        super.write(kryo, output);
        kryo.writeObject(output, moveDirection);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        super.read(kryo, input);
        moveDirection = kryo.readObject(input, Direction.class);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object == null || object.getClass() != MovableObject.class) {
            return false;
        }
        final MovableObject recievedObject = (MovableObject) object;
        return super.equals(recievedObject) && this.getMoveDirection() == recievedObject.getMoveDirection();
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + (moveDirection != null ? moveDirection.hashCode() : 0);

    }
    
    
    //These methods are requested by findbugs
    @Override
    public void setPosition(Vector2D newPosition) {
        super.setPosition(newPosition);
    }

    @Override
    public Vector2D getPosition() {
        return super.getPosition();
    }

    @Override
    public Rectangle2D.Double getTile() {
        return super.getUnitTile();
    }
}
