package model;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represents a single room and the contents in it.
 * @author Ivar Josefsson
 *
 */
public class Room {
	
	// These tiles have the dimension
	private Dimension tileDim;
	private List<Player> players;
	private int[][] collisionMap;
	
	public Room(int[][] mapRepresentation, Dimension tileDim){
		//TODO Player should be added separately with another method.
	
		this.collisionMap = collisionMap;
		this.tileDim = tileDim;
		players = new ArrayList();
	}
	
	/**
	 * Adds the specified player from the Room
	 * @param player
	 */
	public void enterRoom(Player player){
		players.add(player);
	}
	
	/**
	 * Removes the specified player from the Room
	 * @param player
	 */
	public void exitRoom(Player player){
		players.remove(player);
	}
	
	
	
	private void movePlayer(Player player){
		Point newPosition = player.calculateMovementPosition();
		player.setPosition(allowedPosition(player.getPosition(), newPosition));
	}
	
	
	/**
	 * Called to determine the best allowed position
	 * @param fromPosition the position we start from
	 * @param toPosition the position we want to end at
	 * @return the best allowed position
	 */
	private Point allowedPosition(Point fromPosition, Point toPosition){
		int oldX = (int)fromPosition.getX();
		int oldY = (int)fromPosition.getY();
		int newX = (int)toPosition.getX();
		int newY = (int)toPosition.getY();
		int returnX = oldX;
		int returnY = oldY;
				
		if (0 <= newX && newX <= getMapWidth() && !mapCollision(newX, oldY)){
			returnX = newX;
		}
		if (0 <= newY && newY <= getMapHeight() && !mapCollision(oldX, newY)){
			//TODO Causes ArrayIndexOutOfBoundsException in mapCollision trying to index a mapTile that does not exist
			if (!(!mapCollision(newX, oldY) && !mapCollision(oldX, newY) && mapCollision(newX,newY)));{
				returnY = newY;
			}
			
		}
		return new Point(returnX, returnY);
		
	}
	
	/**
	 * Checks if there's a collision at the given coordinates
	 * @param x is the coordinate on the X axis
	 * @param y is the coordinate on the Y axis
	 * @return true if there is a collision
	 */
	private boolean mapCollision(int x, int y){
		//TODO The catch part will trigger when player is on the edge of the map.
		try {
			return collisionMap[pixelToGridX(x)][pixelToGridY(y)] != 0;
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}
	
	/**
	 * 
	 * @return: the total height of the map in pixels
	 */
	private double getMapHeight() {
		return collisionMap[0].length * tileDim.getHeight();
	}
	
	/**
	 * 
	 * @return: the total width of the map in pixels
	 */
	private double getMapWidth() {
		return collisionMap.length * tileDim.getWidth();
	}
	
	
	/**
	 * Calculates what tileID a pixel belongs as an X coordinate
	 * @param pixel to calculate from
	 * @return the tileID as an integer
	 */
	private int pixelToGridX(int pixel) {
		return (int)(pixel/tileDim.getWidth());
	}
	
	/**
	 * Calculates what tileID a pixel belongs as an Y coordinate
	 * @param pixel to calculate from
	 * @return the tileID as an integer
	 */
	private int pixelToGridY(int pixel) {
		return (int)(pixel/tileDim.getHeight());
	}
	
	public void update(){
		for (Player player:players){
			movePlayer(player);
		}
	}	
}
