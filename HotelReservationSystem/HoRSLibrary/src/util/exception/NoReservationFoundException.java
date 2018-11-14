/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Bryan
 */
public class NoReservationFoundException extends Exception {

    /**
     * Creates a new instance of <code>NoReservationFoundException</code>
     * without detail message.
     */
    public NoReservationFoundException() {
    }

    /**
     * Constructs an instance of <code>NoReservationFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public NoReservationFoundException(String msg) {
        super(msg);
    }
}
