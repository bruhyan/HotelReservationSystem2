
package ejb.session.stateless;

import Entity.BookingEntity;
import Entity.RoomEntity;
import Entity.RoomTypeEntity;
import java.util.List;
import util.exception.BookingNotFoundException;

public interface BookingControllerRemote {

    public List<BookingEntity> retrieveBookingList();

    public BookingEntity createBooking(BookingEntity booking);

    public RoomTypeEntity retriveRoomTypeEntityByBookingId(Long bookingId) throws BookingNotFoundException;

  public RoomEntity retrieveRoomEntityByBookingId(Long bookingId) throws BookingNotFoundException  ;
    
}
