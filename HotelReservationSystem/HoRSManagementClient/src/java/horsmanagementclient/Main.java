/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import Entity.SystemAdministrator;
import java.util.Scanner;
import javax.ejb.EJB;
import ejb.session.stateless.EmployeeControllerRemote;

/**
 *
 * @author Bryan
 */
public class Main {
    @EJB
    public static EmployeeControllerRemote employeeControllerRemote;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(employeeControllerRemote);
        mainApp.runApp();
        // create employee shortcut
//        Scanner sc = new Scanner(System.in);
//        System.out.println("Enter name");
//        String name = sc.nextLine();
//        System.out.println("Enter contact number");
//        String contact = sc.nextLine();
//        System.out.println("Enter email");
//        String email = sc.nextLine();
//        System.out.println("Enter password");
//        String password = sc.nextLine();
//        System.out.println("Enter address");
//        String address = sc.nextLine();
//        SystemAdministrator sa = new SystemAdministrator(name, contact, email, password, address);
//        sa = employeeControllerRemote.createSystemAdministrator(sa);
//        System.out.println("Test success");
        
        
        

    }
    
}
