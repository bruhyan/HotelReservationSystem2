/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import Entity.EmployeeEntity;
import Entity.GuestRelationOfficer;
import Entity.OperationManager;
import Entity.RoomEntity;
import Entity.SystemAdministrator;
import ejb.session.stateless.BookingControllerRemote;
import ejb.session.stateless.EmployeeControllerRemote;
import ejb.session.stateless.PartnerControllerRemote;
import ejb.session.stateless.RoomControllerRemote;
import ejb.session.stateless.RoomRateControllerRemote;
import ejb.session.stateless.RoomTypeControllerRemote;
import java.util.List;
import java.util.Scanner;
import util.exception.EmployeeNotFoundException;

/**
 *
 * @author Bryan
 */
public class MainApp {
    private EmployeeControllerRemote employeeControllerRemote;
    private RoomTypeControllerRemote roomTypeControllerRemote;
    private RoomControllerRemote roomControllerRemote;
    private EmployeeEntity loggedInUser;
    private RoomRateControllerRemote roomRateControllerRemote;
    private BookingControllerRemote bookingControllerRemote;
    private PartnerControllerRemote partnerControllerRemote;

    public MainApp() {
    }
    
    public MainApp(EmployeeControllerRemote employeeControllerRemote, RoomTypeControllerRemote roomTypeControllerRemote, RoomControllerRemote roomControllerRemote, RoomRateControllerRemote roomRateControllerRemote, BookingControllerRemote bookingControllerRemote, PartnerControllerRemote partnerControllerRemote) {
        this();
        this.employeeControllerRemote = employeeControllerRemote;
        this.roomTypeControllerRemote = roomTypeControllerRemote;
        this.roomControllerRemote = roomControllerRemote;
        this.roomRateControllerRemote = roomRateControllerRemote;
        this.bookingControllerRemote = bookingControllerRemote;
        this.partnerControllerRemote = partnerControllerRemote;
    }
    
    public void test(){
        
        List<RoomEntity> roomList = roomControllerRemote.retrieveRoomListByTypeId(7l);
        for(RoomEntity room : roomList){
            System.out.println(room.getRoomNumber());
        }
    }
    
    public void runApp() {
        
        Scanner sc = new Scanner(System.in);
        int input = 0;
        while(true) {
            System.out.println("==== Welcome to the HoRS Management Client ====");
            if(loggedInUser == null) {
                System.out.println("1: Employee Login");
            }else {
                System.out.println("1: Do your shit");
            }
            System.out.println("2: Exit");
            if(loggedInUser != null) {
                System.out.println("3: Employee Logout");
            }
            input = 0;
            while(input < 1 || input > 3) {
                System.out.println(">");
                input = sc.nextInt();
                if(input == 1) {
                    if(loggedInUser == null) {
                        doLogin(sc);
                    }else {
                        //System.out.println("Employee Already Logged In !");
                        doStreamEmployee();
                    }
                }else if(input == 2) {
                    break;
                }else if(input == 3) {
                    if(loggedInUser != null) {
                        doLogout(sc);
                    }else {
                        System.out.println("No Employee Currently Logged In !");
                    }
                }
            }
            if(input == 2) {
                break;
            }
        }
        

    }
    
    public void doLogin(Scanner sc) {
        sc.nextLine();
        System.out.println("==== Employee Login Page ====");
        System.out.println("Enter email");
        String email = sc.nextLine();
        System.out.println("Enter password");
        String password = sc.nextLine();
        EmployeeEntity employee;
        try {
            employee = employeeControllerRemote.retrieveEmployeeByEmail(email);
            if(employee.getPassword().equals(password)) {
                System.out.println("Welcome "+employee.getName()+".");
                loggedInUser = employee;
            }else {
                System.out.println("Incorrect Password !");
            }
        } catch (EmployeeNotFoundException ex) {
            System.out.println("Employee does not exist !");
        }
        
        
    }
    
    public void doLogout(Scanner sc) {
        System.out.println(loggedInUser.getName()+" has been logged out.");
        loggedInUser = null;
    }
    
    public void doStreamEmployee() {
        if (loggedInUser instanceof SystemAdministrator) {
            //System.out.println("Hello System Admin");
            SystemAdministrationModule systemAdministratorModule = new SystemAdministrationModule(loggedInUser, employeeControllerRemote, partnerControllerRemote);
            systemAdministratorModule.runModule();
        }else if(loggedInUser instanceof GuestRelationOfficer){
            FrontOfficeModule frontOfficeModule = new FrontOfficeModule(loggedInUser, employeeControllerRemote, roomControllerRemote, roomTypeControllerRemote);
            frontOfficeModule.runModule();
        
        }else if(loggedInUser instanceof OperationManager){
            HotelOperationModule hotelOperationModule = new HotelOperationModule(loggedInUser, employeeControllerRemote, roomControllerRemote, roomRateControllerRemote, roomTypeControllerRemote, bookingControllerRemote);
            hotelOperationModule.runModule();
        }else {
            System.out.println("wtf?");
        }
    }
    
    
    
    
}
