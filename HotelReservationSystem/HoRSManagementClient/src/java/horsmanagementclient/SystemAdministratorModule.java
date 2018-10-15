/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import Entity.EmployeeEntity;
import ejb.session.stateless.EmployeeControllerRemote;

/**
 *
 * @author Bryan
 */
public class SystemAdministratorModule {
    private EmployeeEntity loggedInUser;
    private EmployeeControllerRemote employeeControllerRemote;

    public SystemAdministratorModule(EmployeeEntity loggedInUser, EmployeeControllerRemote employeeControllerRemote) {
        this.loggedInUser = loggedInUser;
        this.employeeControllerRemote = employeeControllerRemote;
    }
    
    public void runModule() {
        System.out.println("==== Welcome to the System Administrator Module ====");
        System.out.println("1: Create New Employee");
        System.out.println("2: View All Employees");
        System.out.println("3: Create New Partner");
        System.out.println("4: View All Partners");
    }
    
    
    
}
