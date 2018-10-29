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
public class BookingNotFoundException extends Exception{
    public BookingNotFoundException(){
    
    }
    
    public BookingNotFoundException(String msg){
        super(msg);
    }
}
