/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//henlo
package ejb.session.stateless;

import Entity.CustomerEntity;
import Entity.ReservationEntity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CustomerNotFoundException;
import util.exception.NoReservationFoundException;

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

    @Override
    public CustomerEntity retrieveCustomerEntityById(long customerId) throws CustomerNotFoundException {
        CustomerEntity customer = em.find(CustomerEntity.class, customerId);
        if(customer == null){
            throw new CustomerNotFoundException("No customer found with the given customer ID");
        }
        return customer;
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
        List<ReservationEntity> reservations =  query.getResultList();
        return reservations;
    }

    public void nullCustomerReservation(Long customerId) {
        CustomerEntity cus = em.find(CustomerEntity.class, customerId);
        cus.getReservations().clear();
    }

    public CustomerEntity retrieveCustomerByEmail(String email) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT c FROM CustomerEntity c WHERE c.email = :email");
        query.setParameter("email", email);

        try{
        return (CustomerEntity)query.getSingleResult();
        }catch(NoResultException ex){
            throw new CustomerNotFoundException("No customer with the given email was found!");
        }
    }

    @Override
    public ReservationEntity retrieveCustomerLatestReservation(Long customerId) throws NoReservationFoundException{
        CustomerEntity customer = em.find(CustomerEntity.class, customerId);
        Query query = em.createQuery("SELECT r FROM ReservationEntity r WHERE r.customer = :customer ORDER BY r.reservationId DESC");
        query.setParameter("customer", customer);

        try{
        return (ReservationEntity) query.getResultList().get(0);
        }catch(ArrayIndexOutOfBoundsException ex){
            throw new NoReservationFoundException("No new reservation was found for this customer!");
        }

    }

    public List<ReservationEntity> retrieveCustomerUnpaidReservation(Long customerId) {
        CustomerEntity customer = em.find(CustomerEntity.class, customerId);
        Query query = em.createQuery("SELECT r FROM ReservationEntity r WHERE r.customer = :customer AND r.transaction.isPaid = false AND r.showedUp = true");
        query.setParameter("customer", customer);
        return query.getResultList();
    }

    public List<ReservationEntity> retrieveReservationsForCheckIn(Long customerId) {
        CustomerEntity customer = em.find(CustomerEntity.class, customerId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        Date limitDate = new Date();
        String dateString = sdf2.format(limitDate);
        String lowerLimitDateS = dateString+" 23:59:59";
        String upperLimitDateS = dateString+" 14:00:00";
        Date lowerLimitDateF;
        Date upperLimitDateF;
        try {
            lowerLimitDateF = sdf.parse(lowerLimitDateS);
            upperLimitDateF = sdf2.parse(upperLimitDateS);
        } catch (ParseException ex) {
            Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
            lowerLimitDateF = null;
            upperLimitDateF = null;
        }
        
        //can check in later ON THE STIPULATED DAY BUT CANNOT CHECK IN LATER THAN STIPULATED DAY
        //need upper window and lower window --> current day 2pm to 11.59pm
        //cannot see reservations that have check in later than today
        System.out.println(lowerLimitDateF);
        System.out.println(upperLimitDateF);
        Query query = em.createQuery("SELECT r FROM ReservationEntity r WHERE r.customer = :customer AND r.showedUp = false AND r.checkInDateTime <= :lowerLimitDateF AND r.checkInDateTime >= :upperLimitDateF");
        query.setParameter("customer", customer);
        query.setParameter("lowerLimitDateF", lowerLimitDateF);
        query.setParameter("upperLimitDateF", upperLimitDateF);
        return query.getResultList();
    }


}
