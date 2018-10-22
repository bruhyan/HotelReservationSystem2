/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import Entity.EmployeeEntity;
import ejb.session.stateless.EmployeeControllerRemote;
import ejb.session.stateless.RoomRateControllerRemote;
import java.util.Scanner;

/**
 *
 * @author mdk12
 */
public class FrontOfficeModule {
        private EmployeeEntity loggedInUser;
    private EmployeeControllerRemote employeeControllerRemote;

    
    public FrontOfficeModule(){
    
    }
    
    public FrontOfficeModule(EmployeeEntity loggedInUser, EmployeeControllerRemote employeeControllerRemote){
        this();
        this.loggedInUser = loggedInUser;
        this.employeeControllerRemote = employeeControllerRemote;
    }
    
    public void runModule() {
        Scanner sc = new Scanner(System.in);
        int input = 0;
        while(true) {
            System.out.println("==== Welcome to the Front Office Module ====");
            System.out.println("1: Walk-in Search Room");
            System.out.println("2: Walk-in Reserve Room");
            System.out.println("3: Check-in Guest");
            System.out.println("4: Check-out Guest");
            System.out.println("5: Exit");
            input = 0;
            while(input < 1 || input > 5) {
                System.out.print(">");
                input = sc.nextInt();
                if(input == 1) {
                    
                }else if(input == 2) {
                   
                }else if(input == 3) {
                    
                }else if(input == 4) {
                    
                }else if(input == 5) {
                    break;
                }
                
            }
            if(input == 5) {
                break;
            }
        }
    }
    
    public void doWalkInSearchRoom(Scanner sc) {
        System.out.println();
    }
    
    
    
    
}
