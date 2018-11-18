
package ejb.session.stateless;

import Entity.RoomEntity;
import Entity.RoomTypeEntity;
import java.util.Date;
import java.util.List;
import util.enumeration.RoomStatus;


public interface RoomControllerRemote {

    public List<RoomEntity> retrieveRoomListByTypeId(Long roomTypeId);

    public void createNewRoom(RoomEntity room);

    public List<RoomEntity> retrieveRoomList();

    public RoomEntity retrieveRoomById(Long id);

    public void deleteRoomById(Long id);
    
    public RoomEntity allocateRoom(Long roomTypeId);

    public RoomEntity walkInAllocateRoom(Long roomTypeId);

    public void changeRoomStatus(Long roomEntityId, RoomStatus status);

    public void changeIsReserved(Long roomId, boolean value);

    public RoomEntity heavyUpdateRoom(Long id, int roomNumber, RoomStatus newRoomStatus, long roomTypeId, boolean isDisabled);

    public void deleteAllDisabledRooms();

    public int getNumberOfBookableRoomType(RoomTypeEntity roomType, Date checkInDate, Date checkOutDate);
    
}
