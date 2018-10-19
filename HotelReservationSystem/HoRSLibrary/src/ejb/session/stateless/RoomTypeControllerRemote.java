/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.RoomTypeEntity;
import javax.ejb.Remote;

/**
 *
 * @author mdk12
 */
@Remote
public interface RoomTypeControllerRemote {

    public RoomTypeEntity createNewRoomType(RoomTypeEntity roomType);

    public void addRoomRateById(Long roomTypeId, Long roomRateId);
    
}
