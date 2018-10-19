/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import Entity.EmployeeEntity;
import ejb.session.stateless.EmployeeControllerRemote;
import ejb.session.stateless.RoomRateControllerRemote;

/**
 *
 * @author mdk12
 */
public class GuestRelationOfficerModule {
        private EmployeeEntity loggedInUser;
    private EmployeeControllerRemote employeeControllerRemote;

    
    public GuestRelationOfficerModule(){
    
    }
    
    public GuestRelationOfficerModule(EmployeeEntity loggedInUser, EmployeeControllerRemote employeeControllerRemote){
        this();
        this.loggedInUser = loggedInUser;
        this.employeeControllerRemote = employeeControllerRemote;
    }
    
    public void runModule(){
    
    }
}
