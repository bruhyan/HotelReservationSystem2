/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.RoomEntity;
import Entity.RoomRatesEntity;
import Entity.RoomTypeEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author mdk12
 */


@Stateless
public class RoomTypeController implements RoomTypeControllerRemote, RoomTypeControllerLocal {

    private RoomControllerLocal roomControllerLocal;
    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public RoomTypeEntity createNewRoomType(RoomTypeEntity roomType){
        em.persist(roomType);
        em.flush();
        
        return roomType;
    }
    
    public void updateRoomType(RoomTypeEntity roomType){ //not sure if this will work because of the lists and many things to change. Come back to check
        em.merge(roomType);
        
    }
    
    public void addRoomRateById(Long roomTypeId, Long roomRateId){
        RoomRatesEntity roomRate = em.find(RoomRatesEntity.class, roomRateId);
        RoomTypeEntity roomType = em.find(RoomTypeEntity.class, roomTypeId);
        
        roomType.addRoomRate(roomRate);
        
        em.merge(roomType);
    }
    
    public RoomTypeEntity retrieveRoomTypeById(long id){
        return em.find(RoomTypeEntity.class, id);
        
    }
    
    public void deleteRoomTypeById(long id){
        RoomTypeEntity roomType = retrieveRoomTypeById(id);
        //call room controller local to find list by Type
        List<RoomEntity> roomList = roomControllerLocal.retrieveRoomListByType(roomType);
        if(!roomList.isEmpty()){
            //set all to be disabled
            for(RoomEntity room : roomList){
                room.setIsDisabled(true);
            }
            //set roomType to be disabled as well.
            roomType.setIsDisabled(true);
            updateRoomType(roomType);
        
        }else{
            em.remove(roomType);
        }
        
        
    }
    

    public List<RoomTypeEntity> retrieveRoomTypeList(RoomTypeEntity roomType){
        Query query = em.createQuery("SELECT r FROM RoomTypeEntity r");
       
        return query.getResultList();
    }
    
    @Override
    public List<RoomTypeEntity> retrieveRoomTypeListByRates(RoomRatesEntity roomRates){
        Long roomRateId = roomRates.getRoomRatesId();
        Query query = em.createQuery("SELECT r FROM RoomTypeEntity r JOIN r.roomRateList rl WHERE rl.id = :roomRateId");
        query.setParameter("roomRateId", roomRateId);
        
        return query.getResultList();
        
        
                
    }
    

}
