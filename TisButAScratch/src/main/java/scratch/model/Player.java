package scratch.model;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Date;
import scratch.model.weapons.IWeapon;
/**
 * Logical representation of the Player in the game.
 * @author Anna Nylander
 *
 */
public class Player implements ICharacter {
	private int health;
	private final int id;
	private int movementSpeed;
	private Rectangle unitTile;
	private IPlayerInput playerInput;
        private Date tookDamageAtTime;
        private boolean alive;

	public Player(IPlayerInput playerInput, Rectangle unitTile, int id){
		this.playerInput=playerInput;
		movementSpeed = 2;
		this.id = id;
                this.health = 10;
                tookDamageAtTime = new Date();
                alive = true;
		//TODO: Can we rely on clone here? Not certain that the copy will be deep enough
		this.unitTile = new Rectangle((int)unitTile.getX(), (int)unitTile.getY(), (int)unitTile.getWidth(), (int)unitTile.getHeight());
	}

	@Override
	public Point calculateMovementPosition(){
		return calculateNewPosition(playerInput.getMoveInput());
	}
        
        @Override
        public boolean alive() {
            return alive;
        }


	public Point calculateNewPosition(MoveDirection direction){
		int deltaX;
		int deltaY;

		switch(direction){
			case NORTH:
				deltaX=0;
				deltaY=-movementSpeed;
				break;
			case SOUTH:
				deltaX=0;
				deltaY=+movementSpeed;
				break;
			case WEST:
				deltaX=-movementSpeed;
				deltaY=0;
				break;
			case EAST:
				deltaX=+movementSpeed;
				deltaY=0;
				break;
			case NORTHWEST:
				deltaX=-movementSpeed;
				deltaY=-movementSpeed;
				break;
			case NORTHEAST:
				deltaX=+movementSpeed;
				deltaY=-movementSpeed;
				break;
			case SOUTHWEST:
				deltaX=-movementSpeed;
				deltaY=+movementSpeed;
				break;
			case SOUTHEAST:
				deltaX=+movementSpeed;
				deltaY=+movementSpeed;
				break;
			default:
				deltaX = 0;
				deltaY = 0;
		}
		return new Point((int)getPosition().getX()+deltaX, (int)getPosition().getY()+deltaY);
	}

        /**
         * The player will take damage if enough time has passed since last time he took damage
         * @param dmg is the amount of damage the npc should take
         * @return true if the NPC is still alive.
         */
	@Override
	public void takeDamage(int dmg){
		Date moment = new Date();
                if (Math.abs(tookDamageAtTime.getTime() - moment.getTime()) > Constants.TIME_BETWEEN_DAMAGE_INSTANCE){
                    tookDamageAtTime = moment;
                    health=health-dmg;
                }
                if (health< 0){
                    alive = false;
                }
	}

	@Override
	public int getHealth() {
		return health;
	}

	@Override
	public Point getPosition() {
		//Copy to protect from unintentional modification
		return new Point(unitTile.getLocation());
	}

	@Override
	public int getDamage() {
		return 0;
	}

	@Override
	public int getMovementSpeed() {
		return movementSpeed;
	}

	@Override
	public IWeapon getWeapon() {
		return null;
	}

	public IPlayerInput getPlayerInput(){
		return playerInput;
	}

	public int getID(){
		return id;
	}

	@Override
	public Rectangle getUnitTile(){
		return unitTile;
	}

	@Override
	public void setPosition(Point newPosition){
		unitTile.setLocation(new Point(newPosition));
	}

}
