
package ejb.session.stateless;

import Entity.EmployeeEntity;
import Entity.GuestRelationOfficer;
import Entity.OperationManager;
import Entity.SalesManager;
import Entity.SystemAdministrator;
import java.util.List;
import util.exception.EmployeeNotFoundException;


public interface EmployeeControllerRemote {

    public SystemAdministrator createSystemAdministrator(SystemAdministrator sa);

    public EmployeeEntity retrieveEmployeeByEmail(String email) throws EmployeeNotFoundException;

    public EmployeeEntity retrieveEmployeeByEmployeeId(long employeeId) throws EmployeeNotFoundException;

    public OperationManager createOperationManager(OperationManager om);

    public SalesManager createSalesManager(SalesManager sm);

    public GuestRelationOfficer createGuestRelationOfficer(GuestRelationOfficer gro);

    public List<EmployeeEntity> retrieveAllEmployees();
    
}
