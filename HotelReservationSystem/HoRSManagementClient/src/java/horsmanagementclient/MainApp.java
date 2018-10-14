/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import Entity.EmployeeEntity;
import ejb.session.stateless.EmployeeControllerRemote;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.exception.EmployeeNotFoundException;

/**
 *
 * @author Bryan
 */
public class MainApp {
    private EmployeeControllerRemote employeeControllerRemote;
    private EmployeeEntity loggedInUser;

    public MainApp() {
    }
    
    public MainApp(EmployeeControllerRemote employeeControllerRemote) {
        this();
        this.employeeControllerRemote = employeeControllerRemote;
    }
    
    public void runApp() {
        
        Scanner sc = new Scanner(System.in);
        int input = 0;
        while(true) {
            System.out.println("==== Welcome to the HoRS Management Client ====");
            if(loggedInUser == null) {
            System.out.println("1: Employee Login");
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
                        System.out.println("Employee Already Logged In !");
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
    
    
    
    
}
