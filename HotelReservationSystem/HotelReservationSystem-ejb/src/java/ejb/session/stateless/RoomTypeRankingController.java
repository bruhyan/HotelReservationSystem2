/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.RoomTypeEntity;
import Entity.RoomTypeRanking;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author mdk12
 */
@Stateless
@Remote(RoomTypeRankingControllerRemote.class)
@Local(RoomTypeRankingControllerLocal.class)
public class RoomTypeRankingController implements RoomTypeRankingControllerRemote, RoomTypeRankingControllerLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    @Override
    public RoomTypeRanking getRoomTypeRanking(){
        
        RoomTypeRanking roomRank = em.find(RoomTypeRanking.class, 1l);
//        List<RoomTypeEntity> roomTypes = 
                roomRank.getRoomTypes().size();
                
//       
//        for(RoomTypeEntity roomType : roomTypes){
//            roomType.get
//        }
        return roomRank;
        
    }
    public void setRoomTypeRank(Long roomTypeId, int rank){
        RoomTypeRanking roomTypeRanking = em.find(RoomTypeRanking.class, 1l);
        RoomTypeEntity roomType = em.find(RoomTypeEntity.class, roomTypeId);
        
        List<RoomTypeEntity> temporaryList = roomTypeRanking.getRoomTypes();
        
        List<RoomTypeEntity> newCloneList = new ArrayList(temporaryList);
        newCloneList.add(rank - 1, roomType);
        roomTypeRanking.getRoomTypes().clear();
        em.persist(roomTypeRanking);
        em.flush();
        
        roomTypeRanking.setRoomTypes(newCloneList);
        em.persist(roomTypeRanking);
        em.flush();
        
        //roomTypeRanking.getRoomTypes().add(rank - 1, roomType);
        
    }
}
