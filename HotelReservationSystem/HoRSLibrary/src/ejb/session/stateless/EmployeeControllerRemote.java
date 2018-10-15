/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.EmployeeEntity;
import Entity.GuestRelationOfficer;
import Entity.OperationManager;
import Entity.SalesManager;
import Entity.SystemAdministrator;
import javax.ejb.Remote;
import util.exception.EmployeeNotFoundException;

/**
 *
 * @author Bryan
 */
@Remote
public interface EmployeeControllerRemote {

    public SystemAdministrator createSystemAdministrator(SystemAdministrator sa);

    public EmployeeEntity retrieveEmployeeByEmail(String email) throws EmployeeNotFoundException;

    public EmployeeEntity retrieveEmployeeByEmployeeId(long employeeId) throws EmployeeNotFoundException;

    public OperationManager createOperationManager(OperationManager om);

    public SalesManager createSalesManager(SalesManager sm);

    public GuestRelationOfficer createGuestRelationOfficer(GuestRelationOfficer gro);
    
}
