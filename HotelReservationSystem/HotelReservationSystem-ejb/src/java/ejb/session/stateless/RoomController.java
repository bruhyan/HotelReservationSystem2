/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.RoomEntity;
import Entity.RoomTypeEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.RoomStatus;

/**
 *
 * @author mdk12
 */
@Stateless
public class RoomController implements RoomControllerRemote, RoomControllerLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    public void createNewRoom(RoomEntity room){
        em.persist(room);
        em.flush();
    }
    
    public void updateRoom(RoomEntity room){
        em.merge(room);
    }
    
    public void deleteRoomById(Long id){
        RoomEntity room = findRoomById(id);
        //Double check here
        if(room.getRoomStatus() == RoomStatus.OCCUPIED ){
            room.setIsDisabled(true);
            em.merge(room);
        }else{
            em.remove(room);
        }
    }
    
    public RoomEntity findRoomById(Long id){
        return em.find(RoomEntity.class, id);
        
    }
    
    //get list of room by room type.
    
    public List<RoomEntity> retrieveRoomListByType(RoomTypeEntity roomType){
        Long id = roomType.getRoomTypeId();
        Query query = em.createQuery("SELECT r FROM room WHERE ROOMTYPE_ROOMTYPEID = :roomTypeId"); //find out if this is correct
        query.setParameter("roomTypeId", id);
        
        return query.getResultList();
        
    }
    

}
