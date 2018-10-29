/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.BookingEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author mdk12
 */
@Local
public interface BookingControllerLocal {
        public List<BookingEntity> retrieveBookingList();
}
