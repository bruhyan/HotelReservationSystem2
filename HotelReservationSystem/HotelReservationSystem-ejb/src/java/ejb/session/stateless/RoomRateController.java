/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.RoomRatesEntity;
import Entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.util.Date;
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
public class RoomRateController implements RoomRateControllerRemote, RoomRateControllerLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    @EJB
    private RoomTypeControllerLocal roomTypeControllerLocal;
    
    public void createNewRoomRate(RoomRatesEntity roomRates){
        em.persist(roomRates);
        em.flush();
    }
    
    public void updateRoomRates(RoomRatesEntity roomRates){
        em.merge(roomRates);
    }
    
    public void deleteRoomRatesById(Long id){
    RoomRatesEntity roomRates = retrieveRoomRatesById(id);
        //call room controller local to find list by Type
        List<RoomTypeEntity> roomTypes = roomTypeControllerLocal.retrieveRoomTypeListByRates(roomRates);
        if(!roomTypes.isEmpty()){
            //set all to be disabled
            for(RoomTypeEntity roomType : roomTypes){
                roomType.setIsDisabled(true); //set all the type disable
                
                
            }
            roomRates.setIsDisabled(true);
        
        }else{
            em.remove(roomRates);
        }
    }
    

    public RoomRatesEntity retrieveRoomRatesById(Long id){
        return em.find(RoomRatesEntity.class, id);
        
    }
    
    @Override
    public List<RoomRatesEntity> retrieveRoomRatesList(){
        Query query = em.createQuery("SELECT r FROM RoomRatesEntity r");
        return query.getResultList();
    }
    //heavyUpdateRoomRate(roomRate.getRoomRatesId(), roomRateName, ratePerNight, date2, date3);
    public RoomRatesEntity heavyUpdateRoomRate(Long roomRateId, String roomRateName, BigDecimal ratePerNight, Date dateStart, Date dateEnd){
            RoomRatesEntity roomRates = retrieveRoomRatesById(roomRateId);
        roomRates.setName(roomRateName);
        roomRates.setRatePerNight(ratePerNight);
        roomRates.setValidityStart(dateStart);
        roomRates.setValidityEnd(dateEnd);
        updateRoomRate(roomRates);
        return roomRates;
    
    }
    
        public void updateRoomRate(RoomRatesEntity roomRates){ //not sure if this will work because of the lists and many things to change. Come back to check
        em.merge(roomRates);
        
    }
    //get list of room by room type.
    
//    public List<RoomRatesEntity> retrieveRoomRatesListByType(RoomTypeEntity roomType){
//
//        Query query = em.createQuery("SELECT r FROM RoomEntity r WHERE r.roomType = :roomType"); //find out if this is correct
//        query.setParameter("roomType", roomType);
//        
//        return query.getResultList();
//        
//    }
//    public List<RoomEntity> retrieveRoomListByTypeId(Long roomTypeId){
//        
//        Query query = em.createQuery("SELECT r FROM RoomEntity r WHERE r.roomType  = :roomType"); //find out if this is correct
//        RoomTypeEntity roomType = em.find(RoomTypeEntity.class, roomTypeId);
//        query.setParameter("roomType", roomType);
//        
//        return query.getResultList();
//        
//    }
}
