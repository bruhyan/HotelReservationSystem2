/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import Entity.EmployeeEntity;
import Entity.GuestRelationOfficer;
import Entity.OperationManager;
import Entity.RoomEntity;
import Entity.RoomRatesEntity;
import Entity.RoomTypeEntity;
import Entity.RoomTypeRanking;
import Entity.SalesManager;
import Entity.SystemAdministrator;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.RateType;

/**
 *
 * @author mdk12
 */
@Singleton
@LocalBean
@Startup

public class InitialDataSessionBean {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @PostConstruct
    public void postConstruct() {
        EmployeeEntity e = em.find(EmployeeEntity.class, 1l);
        RoomRatesEntity r1 = em.find(RoomRatesEntity.class, 1l);
        RoomTypeEntity r2 = em.find(RoomTypeEntity.class, 1l);
        RoomEntity r3 = em.find(RoomEntity.class, 1l);
        RoomTypeRanking r4 = em.find(RoomTypeRanking.class, 1l);

        if (r4 == null) {
            initaliseRoomRankingList();
        }
        if (e == null) {
            initialiseEmployeeData();
        }

        if (r1 == null) {
            initialiseRoomRates();
        }

        if (r2 == null) {
            initialiseRoomType();
            setRoomTypesToRoomRates();
        }

        if (r3 == null) {
            initialiseRooms();
        }

        // setupTestRanks();
    }

    public void initaliseRoomRankingList() {
        RoomTypeRanking roomRanking = new RoomTypeRanking();
        em.persist(roomRanking);
        em.flush();

    }

    public void initialiseEmployeeData() {
        //String name, String contactNumber, String email, String password, String address
        SystemAdministrator e1 = new SystemAdministrator("System Admin", "90001000", "1", "1", "NUS Computing");
        OperationManager e2 = new OperationManager("Operation Manager", "91234567", "2", "1", "Merlion Hotel");
        SalesManager e3 = new SalesManager("Sales Manager", "99999999", "3", "1", "Merlion Hotel");
        GuestRelationOfficer e4 = new GuestRelationOfficer("Guest Officer", "62353535", "4", "1", "Merlion Hotel");

        em.persist(e1);
        em.persist(e2);
        em.persist(e3);
        em.persist(e4);
        em.flush();
    }

    public void initialiseRoomRates() {
        //String name, BigDecimal ratePerNight, Date validityStart, Date validityEnd, RateType rateType

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");

        //valid period
        Date date1 = new Date(2018, 11, 10);
        Date date2 = new Date(2018, 12, 25);

        //expired
        Date date3 = new Date(2018, 02, 10);
        Date date4 = new Date(2018, 04, 12);

        //haven't start
        Date date5 = new Date(2019, 01, 01);
        Date date6 = new Date(2019, 02, 02);

        List<RoomRatesEntity> roomRates = new ArrayList<>();

        //valid published, remember possible to have null.
        roomRates.add(new RoomRatesEntity("Published Test Rate 1 : $150", BigDecimal.valueOf(150.00), date1, date2, RateType.PUBLISHED));
        roomRates.add(new RoomRatesEntity("Normal Test Rate 1 : $200", BigDecimal.valueOf(100.00), date1, date2, RateType.NORMAL));
        roomRates.add(new RoomRatesEntity("Published Test Rate 2 : 200", BigDecimal.valueOf(200.00), date1, date2, RateType.PUBLISHED));
        roomRates.add(new RoomRatesEntity("Normal Test Rate 2 : $150", BigDecimal.valueOf(150.00), date1, date2, RateType.NORMAL));
        roomRates.add(new RoomRatesEntity("Published Test Rate 3 : $500", BigDecimal.valueOf(500.00), date1, date2, RateType.PUBLISHED));
        roomRates.add(new RoomRatesEntity("Normal Test Rate 3 : $350", BigDecimal.valueOf(350.00), date1, date2, RateType.NORMAL));
        roomRates.add(new RoomRatesEntity("Published Test Rate 4 : $800", BigDecimal.valueOf(800.00), date1, date2, RateType.PUBLISHED));
        roomRates.add(new RoomRatesEntity("Normal Test Rate 4 : $550", BigDecimal.valueOf(550.00), date1, date2, RateType.NORMAL));
        roomRates.add(new RoomRatesEntity("Published Test Rate 5 : $9500", BigDecimal.valueOf(9500.00), date1, date2, RateType.PUBLISHED));
        roomRates.add(new RoomRatesEntity("Normal Test Rate 5 : $5000", BigDecimal.valueOf(5000.00), date1, date2, RateType.NORMAL));
//        roomRates.add(new RoomRatesEntity("Published Valid Test Rate 3", BigDecimal.valueOf(500.00), date1, date2, RateType.PUBLISHED));
//        roomRates.add(new RoomRatesEntity("Published Valid Test Rate 4", BigDecimal.valueOf(5000.00), date1, date2, RateType.PUBLISHED));
//        roomRates.add(new RoomRatesEntity("Published Valid Test Rate 4", BigDecimal.valueOf(5000.00), date1, date2, RateType.PUBLISHED));

//        //invalid published
//        roomRates.add(new RoomRatesEntity("Published Invalid Test Rate 5", BigDecimal.valueOf(150.00), date3, date4, RateType.PUBLISHED));
//        roomRates.add(new RoomRatesEntity("Published Invalid Test Rate 6", BigDecimal.valueOf(5000.00), date5, date6, RateType.PUBLISHED));
//        //6
//
//        //normal
//        //valid normal, remember possible to have null.
//        roomRates.add(new RoomRatesEntity("Normal Valid Test Rate 1", BigDecimal.valueOf(50.00), date1, date2, RateType.NORMAL));
//        roomRates.add(new RoomRatesEntity("Normal Valid Test Rate 2", BigDecimal.valueOf(150.00), date1, date2, RateType.NORMAL));
//        roomRates.add(new RoomRatesEntity("Normal Valid Test Rate 3", BigDecimal.valueOf(500.00), date1, date2, RateType.NORMAL));
//        roomRates.add(new RoomRatesEntity("Normal Valid Test Rate 4", BigDecimal.valueOf(5000.00), date1, date2, RateType.NORMAL));
//
//        //invalid normal
//        roomRates.add(new RoomRatesEntity("Normal Invalid Test Rate 5", BigDecimal.valueOf(150.00), date3, date4, RateType.NORMAL));
//        roomRates.add(new RoomRatesEntity("Normal Invalid Test Rate 6", BigDecimal.valueOf(5000.00), date5, date6, RateType.NORMAL));
//        //12
//
//        //peak
//        //valid peak, remember possible to have null.
//        roomRates.add(new RoomRatesEntity("Peak Valid Test Rate 1", BigDecimal.valueOf(69.00), date1, date2, RateType.PEAK));
//        roomRates.add(new RoomRatesEntity("Peak Valid Test Rate 2", BigDecimal.valueOf(200.00), date1, date2, RateType.PEAK));
//        roomRates.add(new RoomRatesEntity("Peak Valid Test Rate 3", BigDecimal.valueOf(750.00), date1, date2, RateType.PEAK));
//        roomRates.add(new RoomRatesEntity("Peak Valid Test Rate 4", BigDecimal.valueOf(7000.00), date1, date2, RateType.PEAK));
//
//        //invalid peak
//        roomRates.add(new RoomRatesEntity("Peak Invalid Test Rate 5", BigDecimal.valueOf(150.00), date3, date4, RateType.PEAK));
//        roomRates.add(new RoomRatesEntity("Peak Invalid Test Rate 6", BigDecimal.valueOf(5000.00), date5, date6, RateType.PEAK));
//        //18
//
//        //promo
//        //valid Promo, remember possible to have null.
//        roomRates.add(new RoomRatesEntity("Promo Valid Test Rate 1", BigDecimal.valueOf(32.00), date1, date2, RateType.PROMOTIONAL));
//        roomRates.add(new RoomRatesEntity("Promo Valid Test Rate 2", BigDecimal.valueOf(130.00), date1, date2, RateType.PROMOTIONAL));
//        roomRates.add(new RoomRatesEntity("Promo Valid Test Rate 3", BigDecimal.valueOf(400.00), date1, date2, RateType.PROMOTIONAL));
//        roomRates.add(new RoomRatesEntity("Promo Valid Test Rate 4", BigDecimal.valueOf(4000.00), date1, date2, RateType.PROMOTIONAL));
//
//        //invalid Promo
//        roomRates.add(new RoomRatesEntity("Promo Invalid Test Rate 5", BigDecimal.valueOf(150.00), date3, date4, RateType.PROMOTIONAL));
//        roomRates.add(new RoomRatesEntity("Promo Invalid Test Rate 6", BigDecimal.valueOf(5000.00), date5, date6, RateType.PROMOTIONAL));
//        //24
        for (RoomRatesEntity roomRate : roomRates) {
            em.persist(roomRate);
            em.flush(); //for the right ordering
        }

    }

    public void initialiseRoomType() {
        //String roomName, String description, Integer size, String bed, String amenities, Integer capacity
        List<RoomTypeEntity> roomTypes = new ArrayList<>();

        roomTypes.add(new RoomTypeEntity("Deluxe Room Rank 1", "Amazing room yo", 2, "1 Queen size bed", "Free shampoo", 2, 1));

        roomTypes.add(new RoomTypeEntity("Premier Room Rank 2", "Amazing room yo", 2, "1 Queen size bed", "Free shampoo", 2, 2));

        roomTypes.add(new RoomTypeEntity("Family Room Rank 3", "Amazing room yo", 2, "1 Queen size bed", "Free shampoo", 2, 3));

        roomTypes.add(new RoomTypeEntity("Junior Suite Rank 4", "Amazing room yo", 2, "1 Queen size bed", "Free shampoo", 2, 4));

        roomTypes.add(new RoomTypeEntity("Grand Suite Rank 5", "Amazing room yo", 2, "1 Queen size bed", "Free shampoo", 2, 5));

//        //published and normal
//        roomTypes.add(new RoomTypeEntity("Published And Normal Rank 1", "Should apply either", 2, "3 double size", "Free air", 5, 1));
//        roomTypes.add(new RoomTypeEntity("Published And Normal Rank 2", "Should apply either", 2, "3 double size", "Free air", 5, 2));
//        roomTypes.add(new RoomTypeEntity("Published And Normal Rank 3", "Should apply either", 2, "3 double size", "Free air", 5, 3));
//        roomTypes.add(new RoomTypeEntity("Published And Normal Rank 4", "Should apply either", 2, "3 double size", "Free air", 5, 4));
//        roomTypes.add(new RoomTypeEntity("Invalid Published And Normal $500 Rank 5", "Should apply either", 2, "3 double size", "Free air", 5, 5));
//        roomTypes.add(new RoomTypeEntity("Invalid Published And Normal $5000 Rank 6", "Should apply either", 2, "3 double size", "Free air", 5));
//
//        //Normal And Promo
//        roomTypes.add(new RoomTypeEntity("Normal And Promo Rank 7", "Should apply promo", 2, "3 double size", "Free air", 5));
//        roomTypes.add(new RoomTypeEntity("Normal And Promo Rank 8", "Should apply promo", 2, "3 double size", "Free air", 5));
//        roomTypes.add(new RoomTypeEntity("Normal And Promo Rank 9", "Should apply promo", 2, "3 double size", "Free air", 5));
//        roomTypes.add(new RoomTypeEntity("Normal And Promo Rank 10", "Should apply promo", 2, "3 double size", "Free air", 5));
//        roomTypes.add(new RoomTypeEntity("Invalid Normal And Promo $500 Rank 11", "Should apply promo", 2, "3 double size", "Free air", 5));
//        roomTypes.add(new RoomTypeEntity("Invalid Normal And Promo $5000 Rank 12", "Should apply promo", 2, "3 double size", "Free air", 5));
//        //12
//        //Normal And Peak
//        roomTypes.add(new RoomTypeEntity("Normal and peak Rank 13", "Should apply peak", 2, "3 double size", "Free air", 5));
//        roomTypes.add(new RoomTypeEntity("Normal and peak Rank 14", "Should apply peak", 2, "3 double size", "Free air", 5));
//        roomTypes.add(new RoomTypeEntity("Normal and peak Rank 15", "Should apply peak", 2, "3 double size", "Free air", 5));
//        roomTypes.add(new RoomTypeEntity("Normal and peak Rank 16", "Should apply peak", 2, "3 double size", "Free air", 5));
//        roomTypes.add(new RoomTypeEntity("Invalid Normal and peak $500 Rank 17", "Should apply peak", 2, "3 double size", "Free air", 5));
//        roomTypes.add(new RoomTypeEntity("Invalid Normal and peak $5000 Rank 18", "Should apply peak", 2, "3 double size", "Free air", 5));
//        //18
//        //Promo And Peak
//        roomTypes.add(new RoomTypeEntity("Promo and peak Rank 19", "Should apply peak", 2, "3 double size", "Free air", 5));
//        roomTypes.add(new RoomTypeEntity("Promo and peak Rank 20", "Should apply peak", 2, "3 double size", "Free air", 5));
//        roomTypes.add(new RoomTypeEntity("Promo and peak Rank 21", "Should apply peak", 2, "3 double size", "Free air", 5));
//        roomTypes.add(new RoomTypeEntity("Promo and peak Rank 22", "Should apply peak", 2, "3 double size", "Free air", 5));
//        roomTypes.add(new RoomTypeEntity("Invalid Promo and peak $500 Rank 23", "Should apply peak", 2, "3 double size", "Free air", 5));
//        roomTypes.add(new RoomTypeEntity("Invalid Promo and peak $5000 Rank 24", "Should apply peak", 2, "3 double size", "Free air", 5));
//        //24
//        //Normal Promo And Peak
//        roomTypes.add(new RoomTypeEntity("Normal, promo and peak Rank 25", "Should apply promo", 2, "3 double size", "Free air", 5));
//        roomTypes.add(new RoomTypeEntity("Normal, promo and peak Rank 26", "Should apply promo", 2, "3 double size", "Free air", 5));
//        roomTypes.add(new RoomTypeEntity("Normal, promo and peak Rank 27", "Should apply promo", 2, "3 double size", "Free air", 5));
//        roomTypes.add(new RoomTypeEntity("Normal, promo and peak Rank 28", "Should apply promo", 2, "3 double size", "Free air", 5));
//        roomTypes.add(new RoomTypeEntity("Invalid Normal, promo and peak $500 Rank 29", "Should apply promo", 2, "3 double size", "Free air", 5));
//        roomTypes.add(new RoomTypeEntity("Invalid Normal, promo and peak $5000 Rank 30", "Should apply promo", 2, "3 double size", "Free air", 5));
//        //30
//        
//        RoomTypeRanking roomRank = em.find(RoomTypeRanking.class, 1l);
//            initaliseRoomRankingList();
        for (RoomTypeEntity roomType : roomTypes) {

            em.persist(roomType);
            em.flush();//for the right ordering
//            roomRank.getRoomTypes().add(roomType);
        }
    }

    public void setRoomRanks(List<RoomTypeEntity> roomTypes) {
        RoomTypeRanking roomRank = em.find(RoomTypeRanking.class, 1l);
        for (RoomTypeEntity roomType : roomTypes) {
            roomRank.getRoomTypes().add(roomType);
            em.persist(roomType);
            em.flush();//for the right ordering
        }
    }

    public void setupTestRanks() {
        Query query = em.createQuery("SELECT r FROM RoomTypeEntity r");
        List<RoomTypeEntity> roomTypes = query.getResultList();

        setRoomRanks(roomTypes);

    }

    public void setRoomTypesToRoomRates() {
        int j = 1;
        //Pair project show case
        for (int i = 1; i <= 5; i++) {
               
            RoomTypeEntity roomType = em.find(RoomTypeEntity.class, Long.valueOf(i));
            RoomRatesEntity roomRatePublished = em.find(RoomRatesEntity.class, Long.valueOf(j));
            j++;
            roomType.getRoomRateList().add(roomRatePublished);
            roomRatePublished.getRoomTypeList().add(roomType);

            RoomRatesEntity roomRateNormal = em.find(RoomRatesEntity.class, Long.valueOf(j));
            j++;
            roomType.getRoomRateList().add(roomRateNormal);
            roomRateNormal.getRoomTypeList().add(roomType);

        }

//        //Settle the first rule, Published And Normal, both valid and invalid
//        for (int i = 1; i <= 6; i++) {
//            RoomTypeEntity roomType = em.find(RoomTypeEntity.class, Long.valueOf(i));
//            RoomRatesEntity roomRatePublished = em.find(RoomRatesEntity.class, Long.valueOf(i));
//            roomType.getRoomRateList().add(roomRatePublished);
//            roomRatePublished.getRoomTypeList().add(roomType);
//
//            RoomRatesEntity roomRateNormal = em.find(RoomRatesEntity.class, Long.valueOf(i + 6));
//            roomType.getRoomRateList().add(roomRateNormal);
//            roomRateNormal.getRoomTypeList().add(roomType);
//
//        }
//
//        //Normal and promo
//        for (int i = 7; i <= 12; i++) {
//            RoomTypeEntity roomType = em.find(RoomTypeEntity.class, Long.valueOf(i));
//            RoomRatesEntity roomRateNormal = em.find(RoomRatesEntity.class, Long.valueOf(i));
//            roomType.getRoomRateList().add(roomRateNormal);
//            roomRateNormal.getRoomTypeList().add(roomType);
//
//            RoomRatesEntity roomRatePromo = em.find(RoomRatesEntity.class, Long.valueOf(i + 12));
//            roomType.getRoomRateList().add(roomRatePromo);
//            roomRatePromo.getRoomTypeList().add(roomType);
//        }
//
//        //Normal and Peak
//        for (int i = 13; i <= 18; i++) {
//            RoomTypeEntity roomType = em.find(RoomTypeEntity.class, Long.valueOf(i));
//
//            RoomRatesEntity roomRatePeak = em.find(RoomRatesEntity.class, Long.valueOf(i));
//            roomType.getRoomRateList().add(roomRatePeak);
//            roomRatePeak.getRoomTypeList().add(roomType);
//
//            RoomRatesEntity roomRateNormal = em.find(RoomRatesEntity.class, Long.valueOf(i - 6));
//            roomType.getRoomRateList().add(roomRateNormal);
//            roomRateNormal.getRoomTypeList().add(roomType);
//        }
//
//        //Promo and Peak
//        for (int i = 19; i <= 24; i++) {
//            RoomTypeEntity roomType = em.find(RoomTypeEntity.class, Long.valueOf(i));
//
//            RoomRatesEntity roomRatePromo = em.find(RoomRatesEntity.class, Long.valueOf(i));
//            roomType.getRoomRateList().add(roomRatePromo);
//            roomRatePromo.getRoomTypeList().add(roomType);
//
//            RoomRatesEntity roomRatePeak = em.find(RoomRatesEntity.class, Long.valueOf(i - 6));
//            roomType.getRoomRateList().add(roomRatePeak);
//            roomRatePeak.getRoomTypeList().add(roomType);
//        }
//
//        //Normal, Promo, Peak
//        for (int i = 25; i <= 30; i++) {
//            RoomTypeEntity roomType = em.find(RoomTypeEntity.class, Long.valueOf(i));
//
//            RoomRatesEntity roomRatePeak = em.find(RoomRatesEntity.class, Long.valueOf(i - 12));
//            roomType.getRoomRateList().add(roomRatePeak);
//            roomRatePeak.getRoomTypeList().add(roomType);
//
//            RoomRatesEntity roomRateNormal = em.find(RoomRatesEntity.class, Long.valueOf(i - 18));
//            roomType.getRoomRateList().add(roomRateNormal);
//            roomRateNormal.getRoomTypeList().add(roomType);
//
//            RoomRatesEntity roomRatePromo = em.find(RoomRatesEntity.class, Long.valueOf(i - 6));
//            roomType.getRoomRateList().add(roomRatePromo);
//            roomRatePromo.getRoomTypeList().add(roomType);
//
//        }
    }

    public void initialiseRooms() {
        //Integer roomNumber, RoomTypeEntity roomType
        //every room got one room type
        Query query = em.createQuery("SELECT r FROM RoomTypeEntity r");

        List<RoomTypeEntity> roomTypes = query.getResultList();

        int floorNumber = 100;
        int index = 1;
        int roomName = 1;
        for (RoomTypeEntity roomType : roomTypes) {
            if (index == 25) {
                break;
            }

            for (int i = 0; i < 5; i++) {
                int roomName2 = floorNumber + roomName;
                RoomEntity room = new RoomEntity(roomName2, roomType);
                roomType.getRoomList().add(room);
                em.persist(room);
                em.flush();
                if (index % 5 == 0) {
                    floorNumber += 100;
                    roomName = 0;
                }
                index++;
                roomName++;
            }

        }

    }

}
