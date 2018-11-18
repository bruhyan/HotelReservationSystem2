
package ejb.session.stateless;

import Entity.BookingEntity;
import Entity.RoomEntity;
import Entity.RoomTypeEntity;
import java.util.List;

public interface BookingControllerLocal {
    public List<BookingEntity> retrieveBookingList();

    public BookingEntity createBooking(BookingEntity booking);

    public RoomTypeEntity retriveRoomTypeEntityByBookingId(Long bookingId);

    public RoomEntity retrieveRoomEntityByBookingId(Long bookingId);
    
}
