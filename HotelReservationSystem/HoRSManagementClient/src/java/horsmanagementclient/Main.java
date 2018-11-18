
package horsmanagementclient;

import ejb.session.stateless.BookingControllerRemote;
import ejb.session.stateless.CustomerControllerRemote;
import javax.ejb.EJB;
import ejb.session.stateless.EmployeeControllerRemote;
import ejb.session.stateless.PartnerControllerRemote;
import ejb.session.stateless.ReservationControllerRemote;
import ejb.session.stateless.RoomAllocationExceptionControllerRemote;
import ejb.session.stateless.RoomControllerRemote;
import ejb.session.stateless.RoomRateControllerRemote;
import ejb.session.stateless.RoomTypeControllerRemote;
import ejb.session.stateless.SystemTimerSessionBeanRemote;
import ejb.session.stateless.TransactionControllerRemote;


public class Main {

    @EJB
    private static TransactionControllerRemote transactionControllerRemote;

    @EJB
    public static EmployeeControllerRemote employeeControllerRemote;
    @EJB
    public static RoomControllerRemote roomControllerRemote;
    @EJB
    public static RoomTypeControllerRemote roomTypeControllerRemote;
    @EJB
    public static RoomRateControllerRemote roomRateControllerRemote;
    @EJB
    public static BookingControllerRemote bookingControllerRemote;
    @EJB
    public static PartnerControllerRemote partnerControllerRemote;
    @EJB
    public static CustomerControllerRemote customerControllerRemote;
    @EJB
    public static ReservationControllerRemote reservationControllerRemote;
    @EJB
    public static SystemTimerSessionBeanRemote systemTimerSessionBeanRemote;
    @EJB
    public static RoomAllocationExceptionControllerRemote roomAllocationExceptionControllerRemote;

    
    public static void main(String[] args) {

        MainApp mainApp = new MainApp(employeeControllerRemote, roomTypeControllerRemote, roomControllerRemote, roomRateControllerRemote, bookingControllerRemote, partnerControllerRemote, customerControllerRemote, reservationControllerRemote, systemTimerSessionBeanRemote, transactionControllerRemote, roomAllocationExceptionControllerRemote);
        mainApp.runApp();


    }

}
