/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.BookingEntity;
import Entity.ReservationEntity;
import Entity.TransactionEntity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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

    public List<ReservationEntity> retrieveTodayReservationList() {

        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        Date today = cal.getTime();

        List<ReservationEntity> todayReservation = new ArrayList<>();
        
        Query query = em.createQuery("SELECT r FROM ReservationEntity r ORDER BY r.reservationId ASC");

        List<ReservationEntity> allReservations = query.getResultList();

        for (ReservationEntity reservation : allReservations) {
            em.refresh(reservation);
            cal.setTime(reservation.getCheckInDateTime());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            if (today.equals(cal.getTime())) {
                em.refresh(reservation);
                reservation.getBookingList();
                todayReservation.add(reservation);
            }
        }
        
        return todayReservation;
    }
    
    public ReservationEntity retrieveReservationById(Long reservationId) {
        ReservationEntity reserv = em.find(ReservationEntity.class, reservationId);
        return reserv;
    }
    
    public List<ReservationEntity> retrieveReservationByPartnerId (Long partnerId) {
        Query query = em.createQuery("SELECT r FROM ReservationEntity r WHERE r.partner.partnerId = :partnerId");
        query.setParameter("partnerId", partnerId);
        return query.getResultList();
    }

}
