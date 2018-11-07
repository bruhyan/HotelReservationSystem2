/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsreservationclient;

import Entity.CustomerEntity;
import ejb.session.stateless.CustomerControllerRemote;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
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
            }else {
                System.out.println("1: Do your shit");
            }
            
            System.out.println("2: Guest Registration");
            System.out.println("3: Exit");
            if(loggedInUser != null) {
                System.out.println("4: Guest Logout");
            }
            input = 0;
            while(input < 1 || input > 5) {
                System.out.print(">");
                input = sc.nextInt();
                if(input == 1) {
                    if(loggedInUser == null) {
                        doLogin(sc);
                    }else {
                        doGuestOperations(sc);
                    }
                }else if(input == 2) {
                    doGuestRegistration(sc);
                }else if(input == 3) {
                    break;
                }else if(input == 4 && loggedInUser != null) {
                    doLogout();
                }
            }
            if(input == 3) {
                break;
            }
        }
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
    
    public void doGuestOperations(Scanner sc) {
        
    }
    
    public void doLogout() {
        System.out.println(loggedInUser.getEmail()+" successfully logged out");
        
        loggedInUser = null;
    }
}
