/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsreservationclient;

import Entity.CustomerEntity;
import Entity.ReservationEntity;
import ejb.session.stateless.CustomerControllerRemote;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author Bryan
 */
public class MainApp {
    private CustomerEntity loggedInUser;
    private CustomerControllerRemote customerControllerRemote;

    public MainApp() {
    }

    public MainApp(CustomerControllerRemote customerControllerRemote) {
        this.customerControllerRemote = customerControllerRemote;
    }
    
    
    
    public void runApp() {
        Scanner sc = new Scanner(System.in);
        int input = 0;
        while(true) {
            System.out.println("==== Welcome to the HoRS Reservation Client ====");
            if(loggedInUser == null) {
            System.out.println("1: Guest Login");
            System.out.println("2: Register as Guest");
            System.out.println("3: Search Hotel Room");
            System.out.println("4: Exit");
            }
            if(loggedInUser != null) {
                System.out.println("2: Register as Guest");
                System.out.println("3: Search Hotel Room");
                System.out.println("4: Exit");
                System.out.println("5: View My Reservation Details");
                System.out.println("6: View All My Reservations");
                System.out.println("7: Logout");
            }
            System.out.println("===============================================");
            input = 0;
            while(input < 1 || input > 7) {
                System.out.print(">");
                input = sc.nextInt();
                if(input == 1) {
                    if(loggedInUser == null) {
                        doLogin(sc);
                    }else {
                        System.out.println("Current logged in user: "+loggedInUser.getEmail());
                    }
                }else if(input == 2) {
                    doGuestRegistration(sc);
                }else if(input == 3) {
                    doSearchHotelRoom(sc);
                }else if(input == 4) {
                    break;
                }else if(input == 5 && loggedInUser != null) {
                    doViewMyReservationDetails(sc);
                }else if(input == 6 && loggedInUser != null) {
                    doViewAllMyReservations();
                }else if(input == 7 && loggedInUser != null) {
                    doLogout();
                }
            }
            if(input == 4) {
                break;
            }
        }
    }
    
    public void doSearchHotelRoom(Scanner sc) {
        System.out.println("Enter check in year: [YYYY]");
        int year = sc.nextInt();
        System.out.println("Enter check in month: [ 1(January)~12(December) ]");
        int month = sc.nextInt();
        month-=1;
        System.out.println("Enter check in date [1 ~ 31]"); //2pm check in
        int day = sc.nextInt();
        Date checkInDate = new Date(year, month, day, 14, 0);
        Calendar cal = Calendar.getInstance();
        cal.setTime(checkInDate);
        System.out.println("Enter how many nights of stay"); //12pm checkout
        int nights = sc.nextInt();
        cal.add(Calendar.DATE, nights);
        cal.set(Calendar.HOUR_OF_DAY, 12);
        Date checkOutDate = cal.getTime();
        
    }
    
    public void getAvailableRoomTypes(Date checkInDate, Date checkOutDate, Scanner sc) {
        
    }
    
    public void doViewMyReservationDetails(Scanner sc) {
        List<ReservationEntity> reservations = doViewAllMyReservations();
        System.out.println("Select which reservation to view");
        int choice = sc.nextInt();
        ReservationEntity selected = reservations.get(choice-=1);
        System.out.println("==============================================================");
        System.out.println("Reservation ID: "+selected.getReservationId());
        System.out.println("Reservation type: "+selected.getReservationType());
        System.out.println("Guest name: "+ selected.getCustomer().getFirstName()); //do i need to go through controller to get the customer?
        System.out.println("Guest email: "+selected.getCustomer().getEmail());
        System.out.println("Reservation date: "+selected.getDateOfReservation());
        System.out.println("Check in date: "+selected.getCheckInDateTime());
        System.out.println("Check out date: "+selected.getCheckOutDateTime());
        System.out.println("==============================================================");
        
    }
    
    public List<ReservationEntity> doViewAllMyReservations() {
        Long customerId = loggedInUser.getCustomerId();
        List<ReservationEntity> reservations = customerControllerRemote.retrieveCustomerReservation(customerId);
        int index = 1;
        System.out.println("============= Reservation List =================");
        for(ReservationEntity reservation : reservations) {
            System.out.println("#"+index+" Reservation ID: "+reservation.getReservationId());
            index++;
        }
        System.out.println("================================================");
        return reservations;
    }
    
    public void doLogin(Scanner sc) {
        sc.nextLine();
        System.out.println("===== Guest Login Page =====");
        System.out.println("Enter email");
        String email = sc.nextLine();
        System.out.println("Enter password");
        String password = sc.nextLine();
        CustomerEntity customer;
        try {
            customer = customerControllerRemote.retrieveCustomerByEmail(email);
            if(customer.getPassword().equals(password)) {
                System.out.println("Welcome "+customer.getFirstName()+".");
                loggedInUser = customer;
            }else {
                System.out.println("Incorrect Password!");
            }
        } catch (CustomerNotFoundException ex) {
            System.out.println("Invalid customer credentials!");
        }
        
        
    }
    
    public void doGuestRegistration(Scanner sc) {
        sc.nextLine();
        System.out.println("===== Guest Registration Page ======");
        System.out.println("Enter email:");
        String email = sc.nextLine();
        System.out.println("Enter contact number:");
        String contactNumber = sc.nextLine();
        System.out.println("Enter first name:");
        String firstName = sc.nextLine();
        System.out.println("Enter last name:");
        String lastName = sc.nextLine();
        System.out.println("Enter password:");
        String password = sc.nextLine();
        
        CustomerEntity customer = new CustomerEntity(email, contactNumber, firstName, lastName, password);
        customer = customerControllerRemote.createCustomerEntity(customer);
        System.out.println("Customer "+customer.getEmail()+" successfully created");
    }
    
    public void doLogout() {
        System.out.println(loggedInUser.getEmail()+" successfully logged out");
        
        loggedInUser = null;
    }
}
