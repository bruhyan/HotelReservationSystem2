/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.RoomTypeEntity;
import Entity.RoomTypeRanking;
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
        
        return em.find(RoomTypeRanking.class, 1l);
        
    }
    public void setRoomTypeRank(Long roomTypeId, int rank){
        RoomTypeRanking roomTypeRanking = em.find(RoomTypeRanking.class, 1l);
        RoomTypeEntity roomType = em.find(RoomTypeEntity.class, roomTypeId);
        
        roomTypeRanking.getRoomTypes().add(rank - 1, roomType);
        
    }
}
