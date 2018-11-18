
package ejb.session.stateless;

import Entity.BookingEntity;
import Entity.ReservationEntity;
import Entity.TransactionEntity;
import java.util.List;
import util.exception.NoReservationFoundException;


public interface ReservationControllerRemote {

    public ReservationEntity createNewReservation(ReservationEntity reserv);

    public void addBookings(long reservationId, BookingEntity booking);

    public List<BookingEntity> retrieveBookingListByReservationId(long reservationId);

    public void addTransaction(Long reservationId, TransactionEntity transaction) throws NoReservationFoundException;

    public TransactionEntity retrieveTransactionByReservationId(Long reservationId);

    public void customerShowedUp(Long reservationId);
    
    public ReservationEntity retrieveReservationById(Long reservationId);
    
}
