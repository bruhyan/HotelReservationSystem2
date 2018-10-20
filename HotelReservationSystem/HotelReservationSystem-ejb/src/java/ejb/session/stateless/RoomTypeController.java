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
import javax.ejb.EJB;
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

    @EJB
    private RoomControllerLocal roomControllerLocal;
    @EJB
    private RoomControllerRemote roomControllerRemote;
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
    
    public RoomTypeEntity heavyUpdateRoom(long id, String name, String description, int size, String bed, String amenities, int capacity){
        RoomTypeEntity roomType = retrieveRoomTypeById(id);
        roomType.setAmenities(amenities);
        roomType.setRoomName(name);
        roomType.setDescription(description);
        roomType.setSize(size);
        roomType.setBed(bed);
        roomType.setCapacity(capacity);
        updateRoomType(roomType);
        return roomType;
    }

    public void deleteRoomTypeById(long id){
        RoomTypeEntity roomType = retrieveRoomTypeById(id);
        //call room controller local to find list by Type
        List<RoomEntity> roomList = roomControllerLocal.retrieveRoomListByTypeId(id); //this is returning null. Maybe EJB cannot call other ejb this way?
        if(!roomList.isEmpty()){
            //set all to be disabled
            for(RoomEntity room : roomList){
                room.setIsDisabled(true);
                //roomControllerLocal.updateRoom(room);
            }
            //set roomType to be disabled as well.
            roomType.setIsDisabled(true);
            updateRoomType(roomType);
        
        }else{
            em.remove(roomType);
        }
        
        
    }
    
    public RoomTypeEntity retrieveSingleRoomType(){
        Query query = em.createQuery("SELECT r FROM RoomTypeEntity r");
        return (RoomTypeEntity) query.getSingleResult();
    }

    public List<RoomTypeEntity> retrieveRoomTypeList(){
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
