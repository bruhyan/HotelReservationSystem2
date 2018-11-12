/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.BookingEntity;
import Entity.RoomEntity;
import Entity.RoomTypeEntity;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.ScheduleExpression;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.ReservationType;

/**
 *
 * @author mdk12
 */
@Stateless
@Local(SystemTimerSessionBeanLocal.class)
@Remote(SystemTimerSessionBeanRemote.class)
public class SystemTimerSessionBean implements SystemTimerSessionBeanRemote, SystemTimerSessionBeanLocal {

    @Resource
    private SessionContext sessionContext;
    @EJB
    private BookingControllerLocal bookingControllerLocal;
    @EJB
    private RoomControllerLocal roomControllerLocal;
    @EJB
    private RoomTypeControllerLocal roomTypeControllerLocal;
    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    public SystemTimerSessionBean() {

    }

    public void init() {
        TimerService timerService = sessionContext.getTimerService();
        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setInfo("SystemRoomAllocation_Info");
        ScheduleExpression schedule = new ScheduleExpression();
        schedule.dayOfWeek("*").hour("2");
        timerService.createCalendarTimer(schedule, timerConfig);
    }

    @Timeout
    @Override
    public void roomAllocation() {
        //business rule : booking requires room of room type, no more room type left, allocate one that is higher (More expensive? How to determine is a room type is better?
        // more expensive, higher capacity)
        //If no more, then just dont allocate.

        List<BookingEntity> bookingList = bookingControllerLocal.retrieveBookingList();

        for (BookingEntity booking : bookingList) {

            RoomTypeEntity roomTypeNeeded = booking.getRoomType();
            //check if roomtype is available.
            Long roomTypeId = roomTypeNeeded.getRoomTypeId();

            if (roomControllerLocal.checkAvailabilityOfRoomByRoomTypeId(roomTypeId)) {
                doRegularAllocation(booking.getBookingId(), roomTypeId);
            } else {

                doUpgradeAllocation(booking.getBookingId(), roomTypeId, booking.getReservation().getReservationType());

            }

        }
    }

    public void doRegularAllocation(Long bookingId, Long roomTypeId) {
        BookingEntity booking = em.find(BookingEntity.class, bookingId);
        RoomTypeEntity roomType = em.find(RoomTypeEntity.class, roomTypeId);

        RoomEntity room = roomControllerLocal.allocateRoom(roomTypeId);

        booking.setRoom(room);
        em.merge(booking);

    }

    //for online/partner
    public void doUpgradeAllocation(Long bookingId, Long roomTypeId, ReservationType reservationType) {
        BookingEntity booking = em.find(BookingEntity.class, bookingId);
        RoomTypeEntity oldRoomType = em.find(RoomTypeEntity.class, roomTypeId);
        RoomTypeEntity roomType;

        if (reservationType == ReservationType.WalkIn) {
            roomType = roomTypeControllerLocal.findPricierAvailableRoomTypeForOnlineOrPartner(roomTypeId);
        } else {
            roomType = roomTypeControllerLocal.findPricierAvailableRoomTypeForWalkIn(roomTypeId);
        }

        if (roomType == null) {
            //cannot allocate, report exception use case 16
            System.out.println("Booking id : " + booking.getBookingId() + "'s room cannot be allocated! All upgraded room type are unavailable!");
            return;
        }
        Long upgradedId = roomType.getRoomTypeId();
        roomType = em.find(RoomTypeEntity.class, upgradedId);
        RoomEntity room = roomControllerLocal.allocateRoom(upgradedId);

        booking.setRoom(room);
        em.merge(booking);
        System.out.println("Booking id : " + booking.getBookingId() + "'s room type have been upgraded from " + oldRoomType.getRoomName() + " to " + roomType.getRoomName() + "."); //Use case 16
    }


}
