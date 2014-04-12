package scratch.model;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import scratch.model.weapons.IWeapon;

/**
 * A class that represents a character not controlled by the player
 * @author Ivar
 *
 */
public class NpcType implements INpc {

    private Rectangle2D.Double unitTile;
    private IWeapon weapon;
    private int health;
    private int movementSpeed;
    private final String imagePath;
    private final int id;
    private MoveDirection moveDirection;
    private boolean hostile;
    private boolean alive;
    private INPCMove movementPattern;

    public NpcType(Rectangle unitTile, IWeapon weapon, int health, int moveSpeed, String imagePath, int id){
        this.unitTile = new Rectangle2D.Double(unitTile.getX(), unitTile.getY(), unitTile.getWidth(), unitTile.getHeight());
        this.weapon = weapon;
        this.health = health;
        this.movementSpeed = moveSpeed;
        this.imagePath = imagePath;
        this.id = id;
        moveDirection = MoveDirection.NONE;
        hostile = true;
        alive = true;
    }

    @Override
    public boolean alive() {
        return alive;
    }

    /**
     * Call to determine if NPC is hostile
     * @return
     */
    public boolean isHostile(){
        return hostile;
    }

    /**
     * Call to get a Point with the position of the NPC
     * @return a copy of the pointer holding the position
     */
    public Vector2D getPosition() {
        return new Vector2D(unitTile.x, unitTile.y);
    }

    public IWeapon getWeapon() {
        return weapon;
    }

    public int getDamage() {
        return weapon.getDamage();
    }

    public int getRange() {
        return weapon.getRange();
    }

    public int getHealth() {
        return health;
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }

    /**
     * The NPC will take damage if enough time has passed since last time he took damage
     * @param dmg is the amount of damage the npc should take
     * @return true if the NPC is still alive.
     */
    @Override
    public void takeDamage(int dmg) {
        //TODO
    }

    public void setPosition(Vector2D position) {
        calculateMoveDirection(position);
        unitTile.setRect(position.getX(), position.getY(), unitTile.getWidth(), unitTile.getHeight());
    }

    @Override
    public Rectangle2D.Double getUnitTile() {
        return unitTile;
    }

    @Override
    public Vector2D calculateMovementPosition(IRoomData roomData) {
        return movementPattern.calculateNewPosition(roomData, this);
    }

    private void calculateMoveDirection(Vector2D newPosition){
        double diffX = newPosition.getX() - unitTile.x;
        double diffY = newPosition.getY() - unitTile.y;
        double theta = Math.atan(diffY / diffX)*180/Math.PI;

        if(diffX == 0 && diffY == 0){
            setMoveDirection(MoveDirection.NONE);
        }else if(-22.5 <= theta && theta <= 22.5 && 0 <= diffX){
            setMoveDirection(MoveDirection.EAST);
        }else if(-22.5 <= theta && theta <= 22.5 && diffX < 0){
            setMoveDirection(MoveDirection.WEST);
        }else if(22.5 <= theta && theta <= 67.5 && 0 <= diffX){
            setMoveDirection(MoveDirection.SOUTHEAST);
        }else if(-67.5 <= theta && theta <= -22.5 && diffX < 0){
            setMoveDirection(MoveDirection.SOUTHWEST);
        }else if(-67.5 <= theta && theta <= -22.5 && 0 <= diffX){
            setMoveDirection(MoveDirection.NORTHEAST);
        }else if(22.5 <= theta && theta <= 67.5 && diffX < 0){
            setMoveDirection(MoveDirection.NORTHWEST);
        }else if(theta < -67.5){
            setMoveDirection(MoveDirection.NORTH);
        }else if(67.5 < theta){
            setMoveDirection(MoveDirection.SOUTH);
        }
    }

    private void setMoveDirection(MoveDirection direction){
        moveDirection = direction;
    }

    public MoveDirection getMoveDirection(){
        return moveDirection;
    }

    public void setHealth(int health){
        this.health = health;
    }

    public int getID(){
        return id;
    }

    public String getImagePath(){
        return imagePath;
    }

    @Override
    public void setMovementPattern(INPCMove movementPattern) {
        this.movementPattern = movementPattern;
    }
}
