/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.Employee;
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

    public Employee retrieveEmployeeByEmail(String email) throws EmployeeNotFoundException;

    public Employee retrieveEmployeeByEmployeeId(long employeeId) throws EmployeeNotFoundException;
    
}
