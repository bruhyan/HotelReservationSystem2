/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author mdk12
 */
public class NoAvailableOnlineRoomRateException extends Exception{
    public NoAvailableOnlineRoomRateException(){
    
    }
    
    public NoAvailableOnlineRoomRateException(String msg){
        super(msg);
    }
}
