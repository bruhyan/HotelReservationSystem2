/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.BookingEntity;
import Entity.ReservationEntity;
import Entity.TransactionEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Bryan
 */
@Stateless
@Local(ReservationControllerLocal.class)
@Remote(ReservationControllerRemote.class)
public class ReservationController implements ReservationControllerRemote, ReservationControllerLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public ReservationEntity createNewReservation(ReservationEntity reserv) {
        em.persist(reserv);
        em.flush();
        return reserv;
    }
    
    public void addBookings(long reservationId, BookingEntity booking) {
        ReservationEntity reserv = em.find(ReservationEntity.class, reservationId);
        reserv.getBookingList().size();
        reserv.getBookingList().add(booking);
        
    }
    
    public List<BookingEntity> retrieveBookingListByReservationId(long reservationId) {
        ReservationEntity reserv = em.find(ReservationEntity.class, reservationId);
        reserv.getBookingList().size();
        return reserv.getBookingList();
    }
    
    public void addTransaction(Long reservationId, TransactionEntity transaction) {
        ReservationEntity reserv = em.find(ReservationEntity.class, reservationId);
        reserv.setTransaction(transaction);
    }
    
    public TransactionEntity retrieveTransactionByReservationId(Long reservationId) {
        ReservationEntity reserv = em.find(ReservationEntity.class, reservationId);
        return reserv.getTransaction();
    }
    
    public void customerShowedUp(Long reservationId) {
        ReservationEntity reserv = em.find(ReservationEntity.class, reservationId);
        reserv.setShowedUp(true);
    }
    
    

    
}
