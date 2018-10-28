/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.BookingEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author mdk12
 */
@Stateless
public class BookingController implements BookingControllerRemote, BookingControllerLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    public List<BookingEntity> retrieveBookingList(){
        Query query = em.createQuery("SELECT b FROM BookingEntity b");
        return query.getResultList();
    }
    
    @Override
    public BookingEntity createBooking(BookingEntity booking) {
        em.persist(booking);
        em.flush();
        return booking;
    }

  
}
