/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package scratch.construction;

import org.newdawn.slick.tiled.TiledMap;
import scratch.model.IMap;
import scratch.model.Vector2D;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 *
 * @author Ivar
 */
public class SlickMap implements IMap{
    private final TiledMap map;
    private final int collisionIndex;
    private final int interactiveObjectIndex;

    public SlickMap(TiledMap map){
        this.map = map;
        collisionIndex = map.getLayerIndex("collision");
        interactiveObjectIndex = map.getLayerIndex("interactiveObject");
    }

    public boolean isColliding(Vector2D coordinate) {
        try {
            return (map.getTileId((int) coordinate.getX() / map.getTileWidth(),(int) coordinate.getY() / map.getTileHeight(), collisionIndex) != 0);
        }catch(IndexOutOfBoundsException e){
            return false;
        }
    }

    @Override
    public boolean hasInteractiveObject() {
        boolean found = false;
        for(int i = 0; i < map.getWidth(); i++){
            for(int j = 0; j < map.getHeight(); j++){
                found = map.getTileId(i, j, interactiveObjectIndex) != -1;
            }
        }
        return found;
    }

    public int getHeight() {

        return map.getHeight()*map.getTileHeight();
    }

    public int getWidth() {
        return map.getWidth()*map.getTileWidth();
    }

    @Override
    public java.util.List<Rectangle2D.Double> getNPCRectangles() {
        //TODO implement this correcly. should load from tiled map. with layer npc
        java.util.List<Rectangle2D.Double> npcRectangles = new ArrayList<>();

        npcRectangles.add(new Rectangle2D.Double(32, 32, 32, 32));

        return npcRectangles;
    }

}
