/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.BookingEntity;
import Entity.RoomEntity;
import Entity.RoomTypeEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
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
@Local(RoomControllerLocal.class)
@Remote(RoomControllerRemote.class)
public class RoomController implements RoomControllerRemote, RoomControllerLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    public void createNewRoom(RoomEntity room){
        em.persist(room);
        em.flush();
    }
    
    public List<RoomEntity> retrieveRoomList(){
        Query query = em.createQuery("SELECT r FROM RoomEntity r");
        return query.getResultList();
    }
    public void updateRoom(RoomEntity room){
        em.merge(room);
    }
    
    public void deleteRoomById(Long id){
        RoomEntity room = retrieveRoomById(id);
        //Double check here
        //Delete previous rooms that was set to disabled, or mass deletion by checking if smt is disabled when system starts.
        if(room.getRoomStatus() == RoomStatus.OCCUPIED ){
            room.setIsDisabled(true);
            //actually after is disabled for room, still must delete if a room is disabled.
            em.merge(room);
        }else{
            em.remove(room);
        }
    }
    
    public RoomEntity retrieveRoomById(Long id){
        return em.find(RoomEntity.class, id);
        
    }
        //Overloaded method
        public RoomEntity heavyUpdateRoom(Long id, int roomNumber, RoomStatus newRoomStatus, long roomTypeId){
        RoomEntity room = em.find(RoomEntity.class, id);
        room.setRoomNumber(roomNumber);
        room.setRoomStatus(newRoomStatus);

        
        RoomTypeEntity roomType = em.find(RoomTypeEntity.class, roomTypeId);
        room.setRoomType(roomType);
        em.merge(room);
        
        return room;
    }
    @Override
    public RoomEntity heavyUpdateRoom(Long id, int roomNumber, RoomStatus newRoomStatus, long bookingId, long roomTypeId){
        RoomEntity room = em.find(RoomEntity.class, id);
        room.setRoomNumber(roomNumber);
        room.setRoomStatus(newRoomStatus);
        BookingEntity booking = em.find(BookingEntity.class, bookingId);
        
        room.setBooking(booking);
        
        RoomTypeEntity roomType = em.find(RoomTypeEntity.class, roomTypeId);
        room.setRoomType(roomType);
        em.merge(room);
        
        return room;
    }
    //get list of room by room type.
    
    @Override
    public List<RoomEntity> retrieveRoomListByType(RoomTypeEntity roomType){

        Query query = em.createQuery("SELECT r FROM RoomEntity r WHERE r.roomType = :roomType"); 
        query.setParameter("roomType", roomType);
        
        return query.getResultList();
        
    }
    @Override
    public List<RoomEntity> retrieveRoomListByTypeId(Long roomTypeId){
        
        Query query = em.createQuery("SELECT r FROM RoomEntity r WHERE r.roomType  = :roomType"); 
        RoomTypeEntity roomType = em.find(RoomTypeEntity.class, roomTypeId);
        query.setParameter("roomType", roomType);
        
        return query.getResultList();
        
    }
    
    //check if room type has available rooms
    @Override
    public boolean checkAvailabilityOfRoomByRoomTypeId(Long roomTypeId, Date checkInDate){
        
        RoomTypeEntity roomType = em.find((RoomTypeEntity.class), roomTypeId);
        Query query = em.createQuery("SELECT r FROM RoomEntity r WHERE r.roomType = :roomType AND r.roomStatus = :roomStatus");
        query.setParameter("roomType", roomType);
        query.setParameter("roomStatus", RoomStatus.AVAILABLE);
        
        //if no available rooms, check if there are any rooms that will be available by the time of incoming customer check in
        if(query.getResultList().isEmpty()){
            query = em.createQuery("SELECT r FROM ReservationEntity r WHERE r.checkOutDateTime < :checkInDate");
            query.setParameter("checkInDate", checkInDate);
            //if no such rooms
            if(query.getResultList().isEmpty()) {
            return false;
            }
            
        }
        return true;
        
    }
    
    public RoomEntity allocateRoom(Long roomTypeId){
        RoomTypeEntity roomType = em.find((RoomTypeEntity.class), roomTypeId);
        Query query = em.createQuery("SELECT r FROM RoomEntity r WHERE r.roomType = :roomType AND r.roomStatus = :roomStatus");
        query.setParameter("roomType", roomType);
        query.setParameter("roomStatus", RoomStatus.AVAILABLE);
        
        List<RoomEntity> roomList = query.getResultList();
        RoomEntity room = roomList.get(0);
        room.setRoomStatus(RoomStatus.OCCUPIED);
        em.merge(room);
        return  room;
        
    }
    
    public RoomEntity walkInAllocateRoom(Long roomTypeId) {
        RoomTypeEntity roomType = em.find((RoomTypeEntity.class), roomTypeId);
        Query query = em.createQuery("SELECT r FROM RoomEntity r WHERE r.roomType = :roomType AND r.roomStatus = :roomStatus");
        query.setParameter("roomType", roomType);
        query.setParameter("roomStatus", RoomStatus.AVAILABLE);
        
        List<RoomEntity> roomList = query.getResultList();
        RoomEntity room = roomList.get(0);
        room.setRoomStatus(RoomStatus.RESERVED);
        em.merge(room);
        return  room;
    }
    
    public void changeRoomStatus(Long roomEntityId, RoomStatus status) {
        RoomEntity room = em.find(RoomEntity.class, roomEntityId);
        room.setRoomStatus(status);
    }
    
    


}
