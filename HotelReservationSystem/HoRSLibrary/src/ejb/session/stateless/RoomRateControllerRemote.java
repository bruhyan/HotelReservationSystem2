/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.RoomRatesEntity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author mdk12
 */
@Remote
public interface RoomRateControllerRemote {
        public List<RoomRatesEntity> retrieveRoomRatesList();

    public void createNewRoomRate(RoomRatesEntity roomRates);

    public void updateRoomRates(RoomRatesEntity roomRates);

    public void deleteRoomRatesById(Long id);

    public RoomRatesEntity retrieveRoomRatesById(Long id);

    public RoomRatesEntity heavyUpdateRoomRate(Long roomRateId, String roomRateName, BigDecimal ratePerNight, Date dateStart, Date dateEnd);
    public void addRoomTypeById(Long roomRateId,Long roomTypeId);

    public List<RoomRatesEntity> retrieveRoomRateListExcludeRoomType(Long roomTypeId);
}
