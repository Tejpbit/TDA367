package scratch.controller;

import scratch.model.IPlayerInput;
import scratch.model.Model;
import scratch.model.MoveDirection;
import scratch.model.Player;

import org.newdawn.slick.Input;

import scratch.view.PlayerView;
import scratch.view.View;

/**
 * Class to collect input for the player.
 * @author Anna Nylander
 *
 */
public class PlayerController implements IPlayerInput {
	private Model model;
	private View view;
	private Player player;
	private MoveDirection moveDirection = MoveDirection.NONE;
	private boolean attack, interact;
	
	public PlayerController(Model model, View view){
		this.model=model;
		this.view = view;
		addPlayer();
	}
	
	private void addPlayer(){
		player=model.addPlayer(this);
		view.addPlayerView(player);
	}
	
	public void removePlayer(){
		model.removePlayer(player);
	}
	
	
	public void registerInput(Input input){
            if(player.alive()){
		if(input.isKeyDown(Input.KEY_DOWN) && input.isKeyDown(Input.KEY_LEFT)){
			//Move South-west
			setMoveInput(MoveDirection.SOUTHWEST);
		} else if(input.isKeyDown(Input.KEY_DOWN) && input.isKeyDown(Input.KEY_RIGHT)){
			//Move South-east
			setMoveInput(MoveDirection.SOUTHEAST);
		} else if(input.isKeyDown(Input.KEY_UP) && input.isKeyDown(Input.KEY_LEFT)){
			//Move North-west
			setMoveInput(MoveDirection.NORTHWEST);
		} else if(input.isKeyDown(Input.KEY_UP) && input.isKeyDown(Input.KEY_RIGHT)){
			//Move North-east
			setMoveInput(MoveDirection.NORTHEAST);
		} else if(input.isKeyDown(Input.KEY_DOWN)){
			//Move South
			setMoveInput(MoveDirection.SOUTH);
		} else if(input.isKeyDown(Input.KEY_UP)){
			//Move North
			setMoveInput(MoveDirection.NORTH);
		} else if(input.isKeyDown(Input.KEY_LEFT)){
			//Move West
			setMoveInput(MoveDirection.WEST);
		} else if(input.isKeyDown(Input.KEY_RIGHT)){
			//Move East
			setMoveInput(MoveDirection.EAST);
		}else{
			setMoveInput(MoveDirection.NONE);
		}
		//Attack & Interact commands can be sent at the same time as movement command
		if (input.isKeyDown(Input.KEY_X)){
			setAttackInput(true);
		} else {
                        setAttackInput(false);
                }
		if (input.isKeyDown(Input.KEY_Z)){
			setInteractInput(true);
		} else {
                        setInteractInput(false);
                }
            } else {
                resetInput();
            }
		
	}
        
        
        @Override
	public void setAttackInput(boolean attack){
		this.attack=attack;
	}
        @Override
	public void setInteractInput(boolean interact){
		this.interact=interact;
	}
        @Override
	public void setMoveInput(MoveDirection direction){
		this.moveDirection=direction;
	}
	
        @Override
	public boolean getAttackInput(){
		return attack;
	}
        @Override
	public boolean getInteractInput(){
		return interact;
	}
        @Override
	public MoveDirection getMoveInput(){
		return moveDirection;
	}
        
        @Override
        public void resetInput(){
            attack = false;
            interact = false;
            moveDirection = MoveDirection.NONE;
        }
        

}
