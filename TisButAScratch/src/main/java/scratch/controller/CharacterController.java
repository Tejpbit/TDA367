/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scratch.controller;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.newdawn.slick.GameContainer;
import scratch.model.GameCharacter;
import scratch.view.NpcView;

import javax.swing.text.View;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author Ivar
 */
public abstract class CharacterController extends Listener {

    private GameCharacter character;
    private final PropertyChangeSupport listeners;
   // private final NpcView view;

    public CharacterController(final GameCharacter character) {
        this.character = character;
        listeners = new PropertyChangeSupport(this);
       // view = new NpcView(character);
    }

    public void addListener(final PropertyChangeListener listener) {
        listeners.addPropertyChangeListener(listener);
    }

    public void update() {
        character.update();
        listeners.firePropertyChange(null, null, character);
    }

    public abstract void render(GameContainer gameContainer);
        //view.render(gameContainer);


    public GameCharacter getCharacter() {
        return character;
    }

    public int getId() {
        return character.getId();
    }
    
    protected void setCharacter(GameCharacter character) {
	    this.character = character;
    }


    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof GameCharacter) {
            GameCharacter recievedCharacter = (GameCharacter) object;
            if (recievedCharacter.getId() == getId()) {
                setCharacter(recievedCharacter);
            }
        }
    }
}
