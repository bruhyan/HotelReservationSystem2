/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsreservationclient;

import ejb.session.stateless.BookingControllerRemote;
import ejb.session.stateless.CustomerControllerRemote;
import ejb.session.stateless.ReservationControllerRemote;
import ejb.session.stateless.RoomControllerRemote;
import ejb.session.stateless.RoomTypeControllerRemote;
import javax.ejb.EJB;

/**
 *
 * @author mdk12
 */
public class Main {

    @EJB
    private static CustomerControllerRemote customerControllerRemote;
    @EJB
    private static RoomTypeControllerRemote roomTypeControllerRemote;
    @EJB
    private static RoomControllerRemote roomControllerRemote;
    @EJB
    private static ReservationControllerRemote reservationControllerRemote;
    @EJB
    private static BookingControllerRemote bookingControllerRemote;
    
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(customerControllerRemote, roomTypeControllerRemote, roomControllerRemote, reservationControllerRemote, bookingControllerRemote);
        mainApp.runApp();
    }
    
}
