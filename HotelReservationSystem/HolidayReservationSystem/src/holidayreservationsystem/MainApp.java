/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystem;

import Entity.PartnerEntity;
import java.util.Scanner;

/**
 *
 * @author mdk12
 */
public class MainApp {

    public MainApp() {

    }

    public void run() {

        Scanner sc = new Scanner(System.in);
        int input = 0;
        PartnerEntity partner = null;
        while (true) {
            System.out.println("==== Welcome to the Holiday Reservation System ====");
            if (partner == null) {
                System.out.println("1: Partner Login \n");
                System.out.println("2: Exit \n");
            } else {
                System.out.println("1: Partner Search Room \n");
                System.out.println("2: Partner Reserve Room \n");
                System.out.println("3: View Partner Reservation Details \n");
                System.out.println("4: View All Partner Reservations\n");
                System.out.println("5: Logout\n");
                System.out.println("6: Exit \n");
            }

            if (partner != null) {
                while (input < 1 || input > 6) {
                    if (input == 1) {
                        doPartnerSearchRoom(sc);
                    } else if (input == 2) {
                        doPartnerReserveRoom(sc);
                    } else if (input == 3) {
                        doViewPartnerReservationDetails(sc);
                    } else if (input == 4) {
                        doViewAllPartnerReservation();
                    } else if (input == 5) {
                        doPartnerLogout();
                    } else if (input == 6) {
                        break;
                    } else {
                        System.out.println("Invalid response! Please try again. \n");
                    }
                }
                while (input < 1 || input > 2) {
                    if (input == 1) {
                        doPartnerLogin(sc);
                    } else if (input == 2) {
                        break;
                    } else {
                        System.out.println("Invalid response! Please try again. \n");
                    }
                }

            }
        }
    }

    public void doPartnerLogin(Scanner sc) {
        System.out.println("Please enter your email: ");
        System.out.print(">");
        String email = sc.nextLine();
    }

    public void doPartnerSearchRoom(Scanner sc) {
    }

    public void doPartnerReserveRoom(Scanner sc) {
    }

    public void doViewPartnerReservationDetails(Scanner sc) {
    }

    public void doViewAllPartnerReservation() {
    }

    public void doPartnerLogout() {
    }

    private static holidayreservationsystem.PartnerEntity partnerLogin(java.lang.String email, java.lang.String password) throws PartnerNotFoundException_Exception {
        holidayreservationsystem.HoRSWebService_Service service = new holidayreservationsystem.HoRSWebService_Service();
        holidayreservationsystem.HoRSWebService port = service.getHoRSWebServicePort();
        return port.partnerLogin(email, password);
    }

    

}
