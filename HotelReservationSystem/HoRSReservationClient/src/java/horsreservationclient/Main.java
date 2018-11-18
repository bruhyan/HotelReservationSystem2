
package horsreservationclient;

import ejb.session.stateless.BookingControllerRemote;
import ejb.session.stateless.CustomerControllerRemote;
import ejb.session.stateless.ReservationControllerRemote;
import ejb.session.stateless.RoomControllerRemote;
import ejb.session.stateless.RoomTypeControllerRemote;
import ejb.session.stateless.TransactionControllerRemote;
import javax.ejb.EJB;

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
    @EJB
    private static TransactionControllerRemote transactionControllerRemote;
    
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(customerControllerRemote, roomTypeControllerRemote, roomControllerRemote, reservationControllerRemote, bookingControllerRemote, transactionControllerRemote);
        mainApp.runApp();
    }
    
}
