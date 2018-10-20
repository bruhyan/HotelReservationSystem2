/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.RoomEntity;
import Entity.RoomTypeEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author mdk12
 */
@Local
public interface RoomControllerLocal {

    public List<RoomEntity> retrieveRoomListByType(RoomTypeEntity roomType);
    
    public List<RoomEntity> retrieveRoomListByTypeId(Long roomTypeId);
    
}
