/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.CustomerEntity;
import Entity.ReservationEntity;
import javax.ejb.Remote;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author Bryan
 */
@Remote
public interface CustomerControllerRemote {

    public CustomerEntity createCustomerEntity(CustomerEntity cus);

    public CustomerEntity retrieveCustomerEntityById(long customerId) throws CustomerNotFoundException;

    public CustomerEntity retrieveCustomerEntityByContactNumber(String contactNum) throws CustomerNotFoundException;

    public ReservationEntity retrieveCustomerReservation(Long customerId);

    public void nullCustomerReservation(Long customerId);

    public CustomerEntity retrieveCustomerByEmail(String email) throws CustomerNotFoundException;
    
}
