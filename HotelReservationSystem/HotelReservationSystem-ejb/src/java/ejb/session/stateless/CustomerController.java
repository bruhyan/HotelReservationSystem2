/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.CustomerEntity;
import Entity.ReservationEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author Bryan
 */
@Stateless
public class CustomerController implements CustomerControllerRemote, CustomerControllerLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    public CustomerEntity createCustomerEntity(CustomerEntity cus) {
        em.persist(cus);
        em.flush();
        return cus;
    }
    
    public CustomerEntity retrieveCustomerEntityById(long customerId) throws CustomerNotFoundException {
        return em.find(CustomerEntity.class, customerId);  
    }
    
    @Override
    public CustomerEntity retrieveCustomerEntityByContactNumber(String contactNum) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT c FROM CustomerEntity c WHERE c.contactNumber = :contactNum");
        query.setParameter("contactNum", contactNum);
        return (CustomerEntity)query.getSingleResult();
        
    }
    
    public List<ReservationEntity> retrieveCustomerReservation(Long customerId) {
        CustomerEntity cus = em.find(CustomerEntity.class, customerId);
        return cus.getReservations();
    }
    
    public void nullCustomerReservation(Long customerId) {
        CustomerEntity cus = em.find(CustomerEntity.class, customerId);
        cus.getReservations().clear();
    }
    
    public CustomerEntity retrieveCustomerByEmail(String email) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT c FROM CustomerEntity c WHERE c.email = :email");
        query.setParameter("email", email);
        return (CustomerEntity)query.getSingleResult();
    }
    
    public ReservationEntity retrieveCustomerLatestReservation(Long customerId){
        CustomerEntity customer = em.find(CustomerEntity.class, customerId);
        Query query = em.createQuery("SELECT r FROM ReservationEntity r WHERE r.customer = :customer ORDER BY r.reservationId DESC");
        query.setParameter("customer", customer);
        
        return (ReservationEntity) query.getResultList().get(0);
        
        
    }
    
}
