/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.RoomRatesEntity;
import Entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;
import util.enumeration.RateType;

/**
 *
 * @author mdk12
 */
@Local
public interface RoomTypeControllerLocal {

    public List<RoomTypeEntity> retrieveRoomTypeListByRates(RoomRatesEntity roomRates);
      //public RoomTypeEntity findPricierAvailableRoomTypeForWalkIn(Long roomTypeId);

    //public RoomTypeEntity findPricierAvailableRoomTypeForOnlineOrPartner(Long roomTypeId);
    public List<RoomTypeEntity> retrieveRoomTypesByRateType(RateType rateType);
}
