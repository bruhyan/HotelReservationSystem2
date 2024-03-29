
package ejb.session.stateless;

import Entity.RoomEntity;
import Entity.RoomRatesEntity;
import Entity.RoomTypeEntity;
import java.util.Date;
import java.util.List;
import util.enumeration.RateType;
import util.exception.NoAvailableOnlineRoomRateException;


public interface RoomTypeControllerRemote {

    public RoomTypeEntity createNewRoomType(RoomTypeEntity roomType);

    public void addRoomRateById(Long roomTypeId, Long roomRateId);

    public List<RoomTypeEntity> retrieveRoomTypeList();

    public RoomTypeEntity retrieveRoomTypeById(long id);

    public void deleteRoomTypeById(long id);

    public RoomTypeEntity heavyUpdateRoom(long id, String name, String description, int size, String bed, String amenities, int capacity);

    public RoomTypeEntity retrieveSingleRoomType();

    public List<RoomEntity> retrieveRoomEntityByRoomType(RoomTypeEntity roomType);

    public List<RoomRatesEntity> retrieveRoomRateListById(Long roomTypeId);
    
    public List<RoomTypeEntity> retrieveRoomTypeListByRates(RoomRatesEntity roomRates);

    public List<RoomTypeEntity> retrieveRoomTypesByRateType(RateType rateType);

    public void removeRoomRate(Long roomTypeId, Long roomRateId);

    public List<RoomTypeEntity> retrieveRoomTypeByRanking();

    public void updateRoomRank(int rank, Long roomTypeId);

    public void deleteAllDisabledRoomType();

    public RoomRatesEntity findOnlineRateForRoomType(Long roomTypeId, Date currentDate) throws NoAvailableOnlineRoomRateException;

    public boolean checkIfHavePublished(Long roomTypeId);

    public boolean checkIfHaveNormal(Long roomTypeId);
}
