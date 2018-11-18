
package ejb.session.stateless;

import Entity.RoomRatesEntity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import util.enumeration.RateType;


public interface RoomRateControllerLocal {

    public List<RoomRatesEntity> retrieveRoomRatesList();

     public RoomRatesEntity createNewRoomRate(RoomRatesEntity roomRates);

    public void deleteRoomRatesById(Long id);

    public RoomRatesEntity retrieveRoomRatesById(Long id);

    public RoomRatesEntity heavyUpdateRoomRate(Long roomRateId, String roomRateName, BigDecimal ratePerNight, Date dateStart, Date dateEnd);
    
    public void addRoomTypeById(Long roomRateId,Long roomTypeId);

    public List<RoomRatesEntity> retrieveRoomRateListExcludeRoomType(Long roomTypeId);

    public RoomRatesEntity retriveRoomRateByRateType(RateType rateType);
    
    public void deleteAllDisabledRoomRates();

}
