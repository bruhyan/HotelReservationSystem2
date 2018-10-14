/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.Employee;
import Entity.SystemAdministrator;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.EmployeeNotFoundException;

/**
 *
 * @author Bryan
 */
@Stateless
public class EmployeeController implements EmployeeControllerRemote, EmployeeControllerLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public SystemAdministrator createSystemAdministrator(SystemAdministrator sa) {
        em.persist(sa);
        em.flush();
        return sa;
    }
    
    @Override
     public Employee retrieveEmployeeByEmployeeId(long employeeId) throws EmployeeNotFoundException
    {
        Employee customerEntity = em.find(Employee.class, employeeId);
        
        if(customerEntity != null)
        {
            return customerEntity;
        }
        else
        {
            throw new EmployeeNotFoundException("Employee ID " + employeeId + " does not exist!");
        }               
    }
     
    @Override
     public Employee retrieveEmployeeByEmail(String email) throws EmployeeNotFoundException {
         Query query = em.createQuery("SELECT e FROM Employee e WHERE e.email = :inEmail");
         query.setParameter("inEmail", email);
         Employee employee = (Employee)query.getSingleResult();
         return employee;
     }

    
}
