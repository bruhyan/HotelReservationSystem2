/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.RoomEntity;
import Entity.RoomRatesEntity;
import Entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.RateType;

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
    @EJB
    private RoomRateControllerRemote roomRateControllerRemote;
    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public RoomTypeEntity createNewRoomType(RoomTypeEntity roomType) {
        em.persist(roomType);
        em.flush();

        return roomType;
    }

    public void updateRoomType(RoomTypeEntity roomType) { //not sure if this will work because of the lists and many things to change. Come back to check
        em.merge(roomType);

    }

    public void addRoomRateById(Long roomTypeId, Long roomRateId) {
        RoomRatesEntity roomRate = em.find(RoomRatesEntity.class, roomRateId);
        RoomTypeEntity roomType = em.find(RoomTypeEntity.class, roomTypeId);

        roomType.addRoomRate(roomRate);

        em.merge(roomType);
    }

    public RoomTypeEntity retrieveRoomTypeById(long id) {
        return em.find(RoomTypeEntity.class, id);

    }

    public RoomTypeEntity heavyUpdateRoom(long id, String name, String description, int size, String bed, String amenities, int capacity) {
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

    public void deleteRoomTypeById(long id) {
        RoomTypeEntity roomType = retrieveRoomTypeById(id);
        //call room controller local to find list by Type
        List<RoomEntity> roomList = roomControllerLocal.retrieveRoomListByTypeId(id); //this is returning null. Maybe EJB cannot call other ejb this way?
        if (!roomList.isEmpty()) {
            //set all to be disabled
            for (RoomEntity room : roomList) {
                room.setIsDisabled(true);
                //roomControllerLocal.updateRoom(room);
            }
            //set roomType to be disabled as well.
            roomType.setIsDisabled(true);
            updateRoomType(roomType);

        } else {
            em.remove(roomType);
        }

    }

    public RoomTypeEntity retrieveSingleRoomType() {
        Query query = em.createQuery("SELECT r FROM RoomTypeEntity r");
        return (RoomTypeEntity) query.getSingleResult();
    }

    public List<RoomTypeEntity> retrieveRoomTypeList() {
        Query query = em.createQuery("SELECT r FROM RoomTypeEntity r");

        return query.getResultList();
    }

    public List<RoomRatesEntity> retrieveRoomRateListById(Long roomTypeId) {
        Query query = em.createQuery("SELECT r.roomRateList FROM RoomTypeEntity r WHERE r.roomTypeId = :roomTypeId");
        query.setParameter("roomTypeId", roomTypeId);

        return query.getResultList();

    }

    @Override
    public List<RoomTypeEntity> retrieveRoomTypeListByRates(RoomRatesEntity roomRates) {
        Long roomRateId = roomRates.getRoomRatesId();
        Query query = em.createQuery("SELECT r FROM RoomTypeEntity r JOIN r.roomRateList rl WHERE rl.roomRatesId = :roomRateId");
        query.setParameter("roomRateId", roomRateId);

        return query.getResultList();

    }

    @Override
    public List<RoomEntity> retrieveRoomEntityByRoomType(RoomTypeEntity roomType) {
        Long roomTypeId = roomType.getRoomTypeId();
        Query query = em.createQuery("SELECT r FROM RoomEntity r JOIN r.roomType r1 WHERE r1.roomTypeId = :roomTypeId");
        query.setParameter("roomTypeId", roomTypeId);
        return query.getResultList();

    }

    @Override
    public RoomTypeEntity findPricierAvailableRoomType(Long roomTypeId) {

        //find actual price of this roomType.
        Query query = em.createQuery("SELECT r FROM RoomRatesEntity r JOIN r.roomTypeList r1 WHERE r1.roomTypeId = :roomTypeId");
        query.setParameter("roomTypeId", roomTypeId);

        List<RoomRatesEntity> roomRateList = query.getResultList(); //gets me a list of rates, all rate type included
        //filter room rates to find published.

        //find priciest of published
        //check if null later, come back
        BigDecimal lowestRate = roomRateList.get(0).getRatePerNight();

        for (RoomRatesEntity roomRate : roomRateList) {
            if (roomRate.getRateType() == RateType.PUBLISHED) {
                lowestRate = lowestRate.min(roomRate.getRatePerNight());
            }
        }

        //highest rate is the priciest, now find another room type that is even higher.
        //hash map ordering  published rates?
        //sort rates first, then find the one after highest rate. 
        //one after highest rate shouldn't be same roomType.
        List<RoomRatesEntity> roomRateListExclude = roomRateControllerRemote.retrieveRoomRateListExcludeRoomType(roomTypeId);

        //now we sort out, either equal pricing, or next higher.
        Collections.sort(roomRateListExclude, (RoomRatesEntity r1, RoomRatesEntity r2)
                -> r1.getRatePerNight().compareTo(r2.getRatePerNight()));

        //sorted, now compare if == or >, get first one.
        RoomRatesEntity pricierRoomRate = null;
        for (RoomRatesEntity roomRate : roomRateListExclude) {
            System.out.println(lowestRate + " " + roomRate.getRatePerNight());
            if (lowestRate.compareTo(roomRate.getRatePerNight()) == 0 || lowestRate.compareTo(roomRate.getRatePerNight()) < 0) {
                pricierRoomRate = em.find(RoomRatesEntity.class,roomRate.getRoomRatesId());
                
                
                //check if this list have any available roomType.
                List<RoomTypeEntity> pricierRoomTypes = pricierRoomRate.getRoomTypeList();
                
                for (RoomTypeEntity roomType : pricierRoomTypes) {
                    if (roomControllerLocal.checkAvailabilityOfRoomByRoomTypeId(roomType.getRoomTypeId())) {
                        return roomType;
                    }
                }
                //means haven't found yet in current price range.
                    
                
            }
            //go to next higher price, repeat.
        }

        //Means no roomType available.
        return null;

    }


}
