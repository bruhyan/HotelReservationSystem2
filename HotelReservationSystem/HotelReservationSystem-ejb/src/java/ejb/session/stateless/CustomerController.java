/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.CustomerEntity;
import Entity.ReservationEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author Bryan
 */
@Stateless
@Local(CustomerControllerLocal.class)
@Remote(CustomerControllerRemote.class)
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
        
        try{
            return (CustomerEntity)query.getSingleResult();
        }catch(NoResultException ex){
            System.out.println("Error!");
            throw new CustomerNotFoundException("Customer not found!");
        }
       
//        
//        
//        try {
//            query.setParameter("contactNum", contactNum);
//            cus = (CustomerEntity)query.getSingleResult();
//        } catch(NoResultException ex) {
//            System.out.println("Shit" + ex.getMessage());
//            throw new CustomerNotFoundException("Customer not found");
//        }
//        return cus;
    }
    
    public List<ReservationEntity> retrieveCustomerReservation(Long customerId) {
        //CustomerEntity cus = em.find(CustomerEntity.class, customerId);
        //List<ReservationEntity> reservations = cus.getReservations();
        //reservations.size();
        Query query = em.createQuery("SELECT r FROM ReservationEntity r WHERE r.customer.customerId = :customerId");
        query.setParameter("customerId", customerId);
        return query.getResultList();
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

    public void persist(Object object) {
        em.persist(object);
    }
    
}
