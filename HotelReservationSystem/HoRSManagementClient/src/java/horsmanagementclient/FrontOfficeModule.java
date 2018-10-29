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
import Entity.RoomTypeEntity;
import ejb.session.stateless.BookingControllerRemote;
import ejb.session.stateless.CustomerControllerRemote;
import ejb.session.stateless.EmployeeControllerRemote;
import ejb.session.stateless.ReservationControllerRemote;
import ejb.session.stateless.RoomControllerRemote;
import ejb.session.stateless.RoomTypeControllerRemote;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
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
            System.out.println("2: Walk-in Reserve Room");
            System.out.println("3: Check-in Guest");
            System.out.println("4: Check-out Guest");
            System.out.println("5: Exit");
            input = 0;
            while(input < 1 || input > 5) {
                System.out.print(">");
                input = sc.nextInt();
                if(input == 1) {
                    doWalkInSearchRoom(sc);
                    /*List<RoomEntity> availRooms = doWalkInSearchRoom(sc);
                    int index = 1;
                    System.out.println("==== Available Rooms By Room Types =====");
                    for(RoomEntity availRoom : availRooms) {
                        System.out.println("Index: "+index+" Room Type: "+availRoom.getRoomType().getRoomName()+" Room Number: "+availRoom.getRoomNumber());
                    }*/
                }else if(input == 2) {
                   
                }else if(input == 3) {
                    
                }else if(input == 4) {
                    
                }else if(input == 5) {
                    break;
                }
                
            }
            if(input == 5) {
                break;
            }
        }
    }
    
    public void doWalkInSearchRoom(Scanner sc) {
        int year = Calendar.getInstance().get(Calendar.YEAR);
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
        List<RoomEntity> availRooms = getAvailableRooms(checkInDate, checkOutDate);
        int index = 1;
        System.out.println("==== Available Rooms By Room Types =====");
        for(RoomEntity availRoom : availRooms) {
            System.out.println("Index: "+index+" Room Type: "+availRoom.getRoomType().getRoomName()+" Room Number: "+availRoom.getRoomNumber());
        }
        
        //initiate reserve room
        System.out.println("Do you want to reserve rooms?");
        System.out.println("Enter 1 to reserve, Enter 2 to exit");
        int reply = sc.nextInt();
        if(reply == 1) {
            doWalkInReserveRoom(checkInDate, checkOutDate, availRooms, sc);
        }
        
    }
    
    public List<RoomEntity> getAvailableRooms(Date checkInDate, Date checkOutDate) {
        List<RoomEntity> availRooms = new ArrayList<>();
        //retrieve all room types
        List<RoomTypeEntity> allRoomTypes = roomTypeControllerRemote.retrieveRoomTypeList();
        for(RoomTypeEntity roomType : allRoomTypes) {
            List<RoomEntity> roomsByType = roomTypeControllerRemote.retrieveRoomEntityByRoomType(roomType);
            for(RoomEntity room : roomsByType) {
                if(room.getBooking() == null || checkInDate.after(room.getBooking().getReservation().getCheckOutDateTime()) || checkOutDate.before(room.getBooking().getReservation().getCheckInDateTime())) {
                    availRooms.add(room);
                    break;
                }
            }
        }
        return availRooms;   
    }
    
    public void doWalkInReserveRoom(Date checkInDate, Date checkOutDate, List<RoomEntity> availRooms, Scanner sc) {
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
        ReservationEntity reservation = new ReservationEntity(new Date(), checkInDate, checkOutDate, false, cus);
        reservation = reservationControllerRemote.createNewReservation(reservation);
        while(true) {
            int index = 1;
            System.out.println("Select room to book by index");
            for(RoomEntity availRoom : availRooms) {
                System.out.println("Index: "+index+" Room Type: "+availRoom.getRoomType().getRoomName()+" Room Number: "+availRoom.getRoomNumber());
                index++;
            }
            int selection = sc.nextInt();
            RoomEntity room = availRooms.get(selection-=1);
            BookingEntity booking = new BookingEntity(room, reservation);
            booking = bookingControllerRemote.createBooking(booking);
            reservationControllerRemote.addBookings(reservation.getReservationId(), booking);
            System.out.println("Booking "+booking.getBookingId()+" created successfully");
            System.out.println("Enter 1 to make more bookings, Enter 2 to confirm reservation");
            int reply = sc.nextInt();
            if(reply == 1) {
                availRooms = getAvailableRooms(checkInDate, checkOutDate);
            }else {
                System.out.println("Reservation "+reservation.getReservationId()+" created successfully");
                List<BookingEntity> finalBookings = reservationControllerRemote.retrieveBookingListByReservationId(reservation.getReservationId());
                for(BookingEntity bookingz : finalBookings) {
                    System.out.println("BookingID: "+bookingz.getBookingId() +" Room number: "+bookingz.getRoom().getRoomNumber());
                }
                break;
            }
        }
           
        
    }
    
}
