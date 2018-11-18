
package ejb.session.stateless;

import Entity.BookingEntity;
import Entity.ReservationEntity;
import Entity.TransactionEntity;
import java.util.List;
import util.exception.NoReservationFoundException;

public interface ReservationControllerLocal {
    public ReservationEntity createNewReservation(ReservationEntity reserv);

    public void addBookings(long reservationId, BookingEntity booking);

    public List<BookingEntity> retrieveBookingListByReservationId(long reservationId);

    public void addTransaction(Long reservationId, TransactionEntity transaction)  throws NoReservationFoundException;

    public TransactionEntity retrieveTransactionByReservationId(Long reservationId);

    public List<ReservationEntity> retrieveTodayReservationList();

    public ReservationEntity retrieveReservationById(Long reservationId);

    public List<ReservationEntity> retrieveReservationByPartnerId(Long partnerId);
    
    
}
