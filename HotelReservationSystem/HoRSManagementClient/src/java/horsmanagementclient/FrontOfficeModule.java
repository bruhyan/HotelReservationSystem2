/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import Entity.BookingEntity;
import Entity.CustomerEntity;
import Entity.EmployeeEntity;
import Entity.ReservationEntity;
import Entity.RoomEntity;
import Entity.RoomRatesEntity;
import Entity.RoomTypeEntity;
import Entity.TransactionEntity;
import ejb.session.stateless.BookingControllerRemote;
import ejb.session.stateless.CustomerControllerRemote;
import ejb.session.stateless.EmployeeControllerRemote;
import ejb.session.stateless.ReservationControllerRemote;
import ejb.session.stateless.RoomControllerRemote;
import ejb.session.stateless.RoomRateControllerRemote;
import ejb.session.stateless.RoomTypeControllerRemote;
import ejb.session.stateless.TransactionControllerRemote;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import javax.ejb.EJB;
import util.enumeration.RateType;
import util.enumeration.ReservationType;
import util.enumeration.RoomStatus;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author mdk12
 */
public class FrontOfficeModule {
        private EmployeeEntity loggedInUser;
        private EmployeeControllerRemote employeeControllerRemote;
        private RoomControllerRemote roomControllerRemote;
        private RoomTypeControllerRemote roomTypeControllerRemote;
        private CustomerControllerRemote customerControllerRemote;
        private ReservationControllerRemote reservationControllerRemote;
        private BookingControllerRemote bookingControllerRemote;
        @EJB
        private RoomRateControllerRemote roomRateControllerRemote;
        @EJB
        private TransactionControllerRemote transactionControllerRemote;

    
    public FrontOfficeModule(){
    
    }
    
    public FrontOfficeModule(EmployeeEntity loggedInUser, EmployeeControllerRemote employeeControllerRemote, RoomControllerRemote roomControllerRemote, RoomTypeControllerRemote roomTypeControllerRemote, CustomerControllerRemote customerControllerRemote, ReservationControllerRemote reservationControllerRemote, BookingControllerRemote bookingControllerRemote){
        this();
        this.loggedInUser = loggedInUser;
        this.employeeControllerRemote = employeeControllerRemote;
        this.roomControllerRemote = roomControllerRemote;
        this.roomTypeControllerRemote = roomTypeControllerRemote;
        this.customerControllerRemote = customerControllerRemote;
        this.reservationControllerRemote = reservationControllerRemote;
        this.bookingControllerRemote = bookingControllerRemote;
    }
    
    public void runModule() {
        Scanner sc = new Scanner(System.in);
        int input = 0;
        while(true) {
            System.out.println("==== Welcome to the Front Office Module ====");
            System.out.println("1: Walk-in Search Room");
            //System.out.println("2: Walk-in Reserve Room");
            System.out.println("2: Check-in Guest");
            System.out.println("3: Check-out Guest");
            System.out.println("4: Exit");
            input = 0;
            while(input < 1 || input > 5) {
                System.out.print(">");
                input = sc.nextInt();
                if(input == 1) {
                    doWalkInSearchRoom(sc);
                }else if(input == 2) {
                   doCheckInGuest(sc);
                }else if(input == 3) {
                    doCheckOutGuest(sc);
                }else if(input == 4) {
                  break;  
                }
                
            }
            if(input == 4) {
                break;
            }
        }
    }
    
    public void doWalkInSearchRoom(Scanner sc) {
        System.out.println("Enter check in year: [YYYY]");
        int year = sc.nextInt();
        System.out.println("Enter check in month: [ 1(January)~12(December) ]");
        int month = sc.nextInt();
        month-=1;
        System.out.println("Enter check in date [1 ~ 31]"); //2pm check in
        int day = sc.nextInt();
        Date checkInDate = new Date(year, month, day, 14, 0);
        Calendar cal = Calendar.getInstance();
        cal.setTime(checkInDate);
        System.out.println("Enter how many nights of stay"); //12pm checkout
        int nights = sc.nextInt();
        cal.add(Calendar.DATE, nights);
        cal.set(Calendar.HOUR_OF_DAY, 12);
        Date checkOutDate = cal.getTime();
        
        List<RoomTypeEntity> desiredRoomTypes = new ArrayList<>();
        while(true) {
            List<RoomTypeEntity> availRoomTypes = getAvailableRoomTypes(checkInDate, checkOutDate, sc);
            int index = 1;
            System.out.println("==== Room Types with available rooms =====");
            for(RoomTypeEntity roomType : availRoomTypes) {
                System.out.println("Index: "+ index + "RoomType: "+roomType.getRoomName());
            }
            System.out.println("========================================");
            System.out.println("Select desired room type by index");
            int choice = sc.nextInt();
            desiredRoomTypes.add(availRoomTypes.get(choice-=1));
            System.out.println("Enter 1 to continue adding more room types");
            if(sc.nextInt() != 1) {
                break;
            }
        }
        BigDecimal totalPrevailingRate = calculatePrevailingRate(desiredRoomTypes, nights);
        System.out.println("Total prevailing rate : "+totalPrevailingRate);
        //initiate reserve room
        System.out.println("Do you want to reserve rooms?");
        System.out.println("Enter 1 to reserve, Enter 2 to exit");
        int reply = sc.nextInt();
        if(reply == 1) {
            doWalkInReserveRoom(checkInDate, checkOutDate, desiredRoomTypes, sc, totalPrevailingRate);
        }
        
    }
    
    public List<RoomTypeEntity> getAvailableRoomTypes(Date checkInDate, Date checkOutDate, Scanner sc) {
        List<RoomTypeEntity> availRoomTypes = new ArrayList<>();
        
        //retrieve published room rate entity
        RoomRatesEntity publishedRate = roomRateControllerRemote.retriveRoomRateByRateType(RateType.PUBLISHED);
        //retrieve all room types that has publisheded rate
        List<RoomTypeEntity> allRoomTypes = roomTypeControllerRemote.retrieveRoomTypeListByRates(publishedRate);
        
        for(RoomTypeEntity roomType : allRoomTypes) {
            if(roomControllerRemote.checkAvailabilityOfRoomByRoomTypeId(roomType.getRoomTypeId())) {
                    availRoomTypes.add(roomType);
            }
                
        }
        return availRoomTypes;   
    }
    
    public void doWalkInReserveRoom(Date checkInDate, Date checkOutDate, List<RoomTypeEntity> desiredRoomTypes, Scanner sc, BigDecimal totalPrevailingRate) {
        sc.nextLine();
        //check if customer exists, retrieve if exists
        System.out.println("Enter customer contact number");
        String contactNum = sc.nextLine();
        CustomerEntity cus;
            try {
                cus = customerControllerRemote.retrieveCustomerEntityByContactNumber(contactNum);
                System.out.println("Customer is a registered guest");
            } catch (CustomerNotFoundException ex) {
                System.out.println("Customer is not a registered guest");
                System.out.println("Enter customer email");
                String email = sc.nextLine();
                System.out.println("Enter customer first name");
                String firstName = sc.nextLine();
                System.out.println("Enter customer last name");
                String lastName = sc.nextLine();
                cus = new CustomerEntity(email, contactNum, firstName, lastName);
                cus = customerControllerRemote.createCustomerEntity(cus);
            }
        //create new Reservation
        ReservationEntity reservation = new ReservationEntity(new Date(), checkInDate, checkOutDate, false, cus, ReservationType.WalkIn);
        reservation = reservationControllerRemote.createNewReservation(reservation);
        
        for(RoomTypeEntity roomType : desiredRoomTypes) {
            RoomEntity room = roomControllerRemote.walkInAllocateRoom(roomType.getRoomTypeId());
            BookingEntity booking = new BookingEntity(room, reservation);
            reservationControllerRemote.addBookings(reservation.getReservationId(), booking);
        }
        
        //create transaction
        TransactionEntity transaction = new TransactionEntity(totalPrevailingRate, loggedInUser, reservation);
        transaction = transactionControllerRemote.createNewTransaction(transaction);
        reservationControllerRemote.addTransaction(reservation.getReservationId(), transaction);
        
        System.out.println("Reservation "+reservation.getReservationId()+" successfully created");
        System.out.println("==== Finalized bookings and rooms : =====");
        List<BookingEntity> finalBookings = reservationControllerRemote.retrieveBookingListByReservationId(reservation.getReservationId());
        for(BookingEntity bookingz : finalBookings) {
            System.out.println("BookingID: "+bookingz.getBookingId() +" Room number: "+bookingz.getRoom().getRoomNumber());
        }
        
        
    }
    
    
    public BigDecimal calculatePrevailingRate(List<RoomTypeEntity> roomTypes, int nights) {
        BigDecimal totalAmount = new BigDecimal(0.00);
        for(int i = 0; i < nights; i++) {
            for(RoomTypeEntity roomType : roomTypes) {
                List<RoomRatesEntity> roomRateList = roomTypeControllerRemote.retrieveRoomRateListById(roomType.getRoomTypeId());
                for(RoomRatesEntity roomRate : roomRateList) {
                    if(roomRate.getRateType() == RateType.PUBLISHED) {
                        totalAmount.add(roomRate.getRatePerNight());
                    }
                }
            }
        }
        return totalAmount;
    }
    
    public void doCheckInGuest(Scanner sc) {
        sc.nextLine();
        System.out.println("Enter customer contact number:");
        String contactNum = sc.nextLine();
        CustomerEntity cus;
            try {
                cus = customerControllerRemote.retrieveCustomerEntityByContactNumber(contactNum);
                ReservationEntity reservation = customerControllerRemote.retrieveCustomerReservation(cus.getCustomerId());
                List<BookingEntity> bookingList = reservationControllerRemote.retrieveBookingListByReservationId(reservation.getReservationId());
                
                int index = 1;
                System.out.println("===== Reservation rooms information =====");
                for(BookingEntity booking : bookingList) {
                    System.out.print("Index: "+index+" Room Type: "+bookingControllerRemote.retriveRoomTypeEntityByBookingId(booking.getBookingId()).getRoomName());
                    System.out.print(" Room Number: "+bookingControllerRemote.retrieveRoomEntityByBookingId(booking.getBookingId()).getRoomNumber());
                    index++;
                    System.out.println();
                }
                System.out.println("=========================================");
                System.out.println("Enter 1 to confirm check in");
                int reply = sc.nextInt();
                if(reply == 1) {
                    for(BookingEntity booking : bookingList) {
                        RoomEntity room = bookingControllerRemote.retrieveRoomEntityByBookingId(booking.getBookingId());
                        roomControllerRemote.changeRoomStatus(room.getRoomId(), RoomStatus.OCCUPIED);
                    }
                    System.out.println("Check in completed");
                }
                
                
            } catch (CustomerNotFoundException ex) {
                System.out.println("Customer not found !");
            }
        
    }
    
    public void doCheckOutGuest(Scanner sc) {
        sc.nextLine();
        System.out.println("Enter customer contact number:");
        String contactNum = sc.nextLine();
        CustomerEntity cus;
            try {
                cus = customerControllerRemote.retrieveCustomerEntityByContactNumber(contactNum);
                ReservationEntity reservation = customerControllerRemote.retrieveCustomerReservation(cus.getCustomerId());
                List<BookingEntity> bookingList = reservationControllerRemote.retrieveBookingListByReservationId(reservation.getReservationId());
                
                int index = 1;
                System.out.println("===== Reservation rooms information =====");
                for(BookingEntity booking : bookingList) {
                    System.out.print("Index: "+index+" Room Type: "+bookingControllerRemote.retriveRoomTypeEntityByBookingId(booking.getBookingId()).getRoomName());
                    System.out.print(" Room Number: "+bookingControllerRemote.retrieveRoomEntityByBookingId(booking.getBookingId()).getRoomNumber());
                    index++;
                    System.out.println();
                }
                System.out.println("=========================================");
                System.out.println("Enter 1 to confirm check out");
                int reply = sc.nextInt();
                if(reply == 1) {
                    for(BookingEntity booking : bookingList) {
                        RoomEntity room = bookingControllerRemote.retrieveRoomEntityByBookingId(booking.getBookingId());
                        roomControllerRemote.changeRoomStatus(room.getRoomId(), RoomStatus.AVAILABLE);
                    }
                    System.out.println("Rooms set to available");
                    //pay transaction
                    System.out.println("Enter 1 to make payment");
                    reply = sc.nextInt();
                    if(reply == 1) {
                        customerControllerRemote.nullCustomerReservation(cus.getCustomerId());
                        TransactionEntity transaction = reservationControllerRemote.retrieveTransactionByReservationId(reservation.getReservationId());
                        transaction = transactionControllerRemote.payTransaction(transaction.getTransactionId());
                        System.out.println("Transaction "+transaction.getTransactionId()+" paid successfully on "+transaction.getDatePaid());
                        System.out.println("Check out completed");
                    }else {
                        System.out.println("why don't want pay? ):");
                    }
                }
                
                
            } catch (CustomerNotFoundException ex) {
                System.out.println("Customer not found !");
            }
    }
    
}


