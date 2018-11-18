
package ejb.session.stateless;

import Entity.BookingEntity;
import Entity.RoomEntity;
import Entity.RoomTypeEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Stateless
@Local(BookingControllerLocal.class)
@Remote(BookingControllerRemote.class)
public class BookingController implements BookingControllerRemote, BookingControllerLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public List<BookingEntity> retrieveBookingList(){
        Query query = em.createQuery("SELECT b FROM BookingEntity b");
        List<BookingEntity> bookings =  query.getResultList();
        
        for(BookingEntity booking : bookings){
            booking.getReservation();
        }
        return bookings;
    }
    
    
    @Override
    public BookingEntity createBooking(BookingEntity booking) {
        em.persist(booking);
        em.flush();
        return booking;
    }
    
    public RoomTypeEntity retriveRoomTypeEntityByBookingId(Long bookingId) {
        BookingEntity booking = em.find(BookingEntity.class, bookingId);
        return booking.getRoomType();
    }
    
    public RoomEntity retrieveRoomEntityByBookingId(Long bookingId) {
        BookingEntity booking = em.find(BookingEntity.class, bookingId);
        return booking.getRoom();
    }

  
}
