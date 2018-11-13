/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import ejb.session.stateless.BookingControllerRemote;
import ejb.session.stateless.CustomerControllerRemote;
import javax.ejb.EJB;
import ejb.session.stateless.EmployeeControllerRemote;
import ejb.session.stateless.PartnerControllerRemote;
import ejb.session.stateless.ReservationControllerRemote;
import ejb.session.stateless.RoomControllerRemote;
import ejb.session.stateless.RoomRateControllerRemote;
import ejb.session.stateless.RoomTypeControllerRemote;
import ejb.session.stateless.RoomTypeRankingControllerRemote;
import ejb.session.stateless.SystemTimerSessionBeanRemote;

/**
 *
 * @author Bryan
 */
public class Main {

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
    public static RoomTypeRankingControllerRemote roomTypeRankingControllerRemote;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //int year = Calendar.getInstance().get(Calendar.YEAR);
        //System.out.println(year);

//         create employee shortcut
        /*Scanner sc = new Scanner(System.in);
        System.out.println("Enter name");
        String name = sc.nextLine();
        System.out.println("Enter contact number");
        String contact = sc.nextLine();
        System.out.println("Enter email");
        String email = sc.nextLine();
        System.out.println("Enter password");
        String password = sc.nextLine();
        System.out.println("Enter address");
        String address = sc.nextLine();
        SystemAdministrator sa = new SystemAdministrator(name, contact, email, password, address);
        sa = employeeControllerRemote.createSystemAdministrator(sa);
        System.out.println("Test success");*/
        MainApp mainApp = new MainApp(employeeControllerRemote, roomTypeControllerRemote, roomControllerRemote, roomRateControllerRemote, bookingControllerRemote, partnerControllerRemote, customerControllerRemote, reservationControllerRemote, systemTimerSessionBeanRemote, roomTypeRankingControllerRemote);
        mainApp.runApp();

    }

}
