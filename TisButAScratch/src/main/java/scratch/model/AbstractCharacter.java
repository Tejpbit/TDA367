package scratch.model;


import scratch.model.weapons.IWeapon;
import org.simpleframework.xml.*;

import javax.annotation.concurrent.Immutable;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * The interface for all Characters. Every character has a given 
 * health, position, damage, movement speed and weapon. 
 * @author Alma Ottedag
 * revised 2014-03-27 by Ivar Josefsson
 */
@Root
@Immutable
public abstract class AbstractCharacter {
	@Element (type=Rectangle2D.Double.class)
    private Rectangle2D.Double unitTile;
    private List<CharacterChangeListener> listenerList = new ArrayList<>();
    @Element(type=IWeapon.class)
    private IWeapon weapon;
	@Element
    private int health;
	@Element
    private int movementSpeed;
	@Attribute
    private int id;
	@Element(type=MoveDirection.class, required = false)
    private MoveDirection moveDirection= MoveDirection.SOUTH;
	@Element
    private boolean alive;

    public AbstractCharacter(Rectangle2D.Double unitTile, IWeapon weapon, int health, int movementSpeed, int id){
        this.unitTile = new Rectangle2D.Double(unitTile.getX(), unitTile.getY(), unitTile.getWidth(), unitTile.getHeight());
        this.weapon = weapon;
        this.health = health;
        this.movementSpeed = movementSpeed;
        this.id = id;
        moveDirection = MoveDirection.SOUTH;
        alive = true;
        listenerList = new ArrayList<>();
    }
    public void registerListener(CharacterChangeListener listener){
        listenerList.add(listener);
    }
	AbstractCharacter(){
		super();
	}

    public void setId(int id){
	    this.id=id;
    }
    public abstract boolean isInteracting();
	public abstract void doInteractCooldown();
	public abstract boolean isAttacking();

    public void removeListener(CharacterChangeListener listener){
        listenerList.remove(listener);
    }

    public void takeDamage(int dmg) {
        health = health - Math.abs(dmg);

        System.out.println("Taking damage " + this.getHealth());

        if(Math.abs(dmg) > health) {
            alive = false;
            health = 0;
        }
    }

    public abstract void update();


    public void setPosition(Vector2D position){
        unitTile.setRect(position.getX(),position.getY(), unitTile.getWidth(), unitTile.getHeight());
    }

    public void setMoveDirection(MoveDirection direction){
        moveDirection = direction;
    }

    public void setHealth(int health){
        this.health = health;
    }



    public Rectangle2D.Double getUnitTile(){
        return unitTile;
    }

    public IWeapon getWeapon(){
        return weapon;
    }

    public int getHealth(){
        return health;
    }

    public int getMovementSpeed(){
        return movementSpeed;
    }

    public int getId(){
        return id;
    }

    public MoveDirection getMoveDirection(){
        return moveDirection;
    }

    public int getDamage(){
        return weapon.getDamage();
    }

    public Vector2D getPosition(){
        return new Vector2D(unitTile.getX(), unitTile.getY());
    }

    public Rectangle2D.Double getAttackArea(){
        return new Rectangle2D.Double(
                unitTile.x+(32*weapon.getRange()*moveDirection.getX()),
                unitTile.y+(32*weapon.getRange()*moveDirection.getY()),
                weapon.getAttackArea().width, weapon.getAttackArea().height);
    }


    public void attack(){
        if(weapon.hasCooledDown()){
            for(CharacterChangeListener listener : listenerList){
                listener.handleCharacterAttack(this);

            }
            weapon.startCooldown();
        }
    }
    public void interact(){
        for(CharacterChangeListener listener : listenerList){
            listener.handleCharacterInteraction(this);
        }
        doInteractCooldown();
    }

    public boolean isAlive(){
        return alive;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractCharacter)) return false;

        AbstractCharacter character = (AbstractCharacter) o;

        if (id != character.id) return false;


        return true;
    }

    @Override
    public final int hashCode() {
        int result = 31 * id;
        return result;
    }

    public List<CharacterChangeListener> getListenerList() {
        return listenerList;
    }
}
