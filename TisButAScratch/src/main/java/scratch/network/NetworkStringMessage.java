/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scratch.network;

/**
 *
 * @author Cannonbait
 */
public class NetworkStringMessage {
    private String text;
    
    public NetworkStringMessage(){
        
    }
    
    public NetworkStringMessage (String text){
        this.text = text;
    }
    
    public String getText(){
        return text;
    }
}
