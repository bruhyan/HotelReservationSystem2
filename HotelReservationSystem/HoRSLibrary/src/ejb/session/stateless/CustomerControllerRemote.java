
package ejb.session.stateless;

import Entity.CustomerEntity;
import Entity.ReservationEntity;
import java.util.List;
import util.exception.CustomerNotFoundException;
import util.exception.NoReservationFoundException;


public interface CustomerControllerRemote {

    public CustomerEntity createCustomerEntity(CustomerEntity cus);

    public CustomerEntity retrieveCustomerEntityById(long customerId) throws CustomerNotFoundException;

    public CustomerEntity retrieveCustomerEntityByContactNumber(String contactNum) throws CustomerNotFoundException;

    public List<ReservationEntity> retrieveCustomerReservation(Long customerId);

    public void nullCustomerReservation(Long customerId);

    public CustomerEntity retrieveCustomerByEmail(String email) throws CustomerNotFoundException;

    public ReservationEntity retrieveCustomerLatestReservation(Long customerId) throws NoReservationFoundException;

    public List<ReservationEntity> retrieveCustomerUnpaidReservation(Long customerId);

    public List<ReservationEntity> retrieveReservationsForCheckIn(Long customerId);

}
