/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.BookingEntity;
import Entity.RoomEntity;
import Entity.RoomTypeEntity;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author mdk12
 */
public interface BookingControllerRemote {

    public List<BookingEntity> retrieveBookingList();

    public BookingEntity createBooking(BookingEntity booking);

    public RoomTypeEntity retriveRoomTypeEntityByBookingId(Long bookingId);

    public RoomEntity retrieveRoomEntityByBookingId(Long bookingId);
    
}
