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
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
    public OperationManager createOperationManager(OperationManager om) {
        em.persist(om);
        em.flush();
        return om;
    }
    
    @Override
    public SalesManager createSalesManager(SalesManager sm) {
        em.persist(sm);
        em.flush();
        return sm;
    }
    
    @Override
    public GuestRelationOfficer createGuestRelationOfficer(GuestRelationOfficer gro) {
        em.persist(gro);
        em.flush();
        return gro;
    }
    
    @Override
     public EmployeeEntity retrieveEmployeeByEmployeeId(long employeeId) throws EmployeeNotFoundException
    {
        EmployeeEntity employee = em.find(EmployeeEntity.class, employeeId);
        
        if(employee != null)
        {
            return employee;
        }
        else
        {
            throw new EmployeeNotFoundException("Employee ID " + employeeId + " does not exist!");
        }               
    }
     
    @Override
     public EmployeeEntity retrieveEmployeeByEmail(String email) throws EmployeeNotFoundException {
         Query query = em.createQuery("SELECT e FROM EmployeeEntity e WHERE e.email = :inEmail");
         query.setParameter("inEmail", email);
         
         try{
         EmployeeEntity employee = (EmployeeEntity)query.getSingleResult();
         return employee;
         }catch(NoResultException ex){
             throw new EmployeeNotFoundException("No employee with the given email was found!");
         }
     }
     
    @Override
     public List<EmployeeEntity> retrieveAllEmployees() {
         Query query = em.createQuery("SELECT e FROM EmployeeEntity e");
         return query.getResultList();
     }
     
     

    
}
