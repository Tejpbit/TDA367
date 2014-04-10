/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package scratch.model;

import java.awt.Point;

/**
 *
 * @author Ivar
 */
public interface INpc extends ICharacter{
    public void update(Point playerPos);
    public int getID();
    public MoveDirection getMoveDirection();
    public String getImagePath();
}