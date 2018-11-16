/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.CustomerEntity;
import Entity.ReservationEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CustomerNotFoundException;
import util.exception.NoReservationFoundException;

/**
 *
 * @author Bryan
 */
public interface CustomerControllerRemote {

    public CustomerEntity createCustomerEntity(CustomerEntity cus);

    public CustomerEntity retrieveCustomerEntityById(long customerId) throws CustomerNotFoundException;

    public CustomerEntity retrieveCustomerEntityByContactNumber(String contactNum) throws CustomerNotFoundException;

    public List<ReservationEntity> retrieveCustomerReservation(Long customerId);

    public void nullCustomerReservation(Long customerId);

    public CustomerEntity retrieveCustomerByEmail(String email) throws CustomerNotFoundException;

    public ReservationEntity retrieveCustomerLatestReservation(Long customerId) throws NoReservationFoundException;

}
