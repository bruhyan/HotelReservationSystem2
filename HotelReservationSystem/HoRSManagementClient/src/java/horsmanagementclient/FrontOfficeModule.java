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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private RoomRateControllerRemote roomRateControllerRemote;
    private TransactionControllerRemote transactionControllerRemote;

    public FrontOfficeModule() {

    }

    public FrontOfficeModule(EmployeeEntity loggedInUser, EmployeeControllerRemote employeeControllerRemote, RoomControllerRemote roomControllerRemote, RoomTypeControllerRemote roomTypeControllerRemote, CustomerControllerRemote customerControllerRemote, ReservationControllerRemote reservationControllerRemote, BookingControllerRemote bookingControllerRemote, RoomRateControllerRemote roomRateControllerRemote, TransactionControllerRemote transactionControllerRemote) {
        this();
        this.loggedInUser = loggedInUser;
        this.employeeControllerRemote = employeeControllerRemote;
        this.roomControllerRemote = roomControllerRemote;
        this.roomTypeControllerRemote = roomTypeControllerRemote;
        this.customerControllerRemote = customerControllerRemote;
        this.reservationControllerRemote = reservationControllerRemote;
        this.bookingControllerRemote = bookingControllerRemote;
        this.roomRateControllerRemote = roomRateControllerRemote;
        this.transactionControllerRemote = transactionControllerRemote;
    }

    public void runModule() {
        Scanner sc = new Scanner(System.in);
        int input = 0;
        while (true) {
            System.out.println("==== Welcome to the Front Office Module ====");
            System.out.println("1: Walk-in Search Room");
            //System.out.println("2: Walk-in Reserve Room");
            System.out.println("2: Check-in Guest");
            System.out.println("3: Check-out Guest");
            System.out.println("4: Exit");
            input = 0;
            while (input < 1 || input > 5) {
                System.out.print(">");
                input = sc.nextInt();
                if (input == 1) {
                    doWalkInSearchRoom(sc);
                } else if (input == 2) {
                    doCheckInGuest(sc);
                } else if (input == 3) {
                    doCheckOutGuest(sc);
                } else if (input == 4) {
                    break;
                }

            }
            if (input == 4) {
                break;
            }
        }
    }

    public void doWalkInSearchRoom(Scanner sc) {
        System.out.println("Enter check in year: [YYYY]");
        int year = sc.nextInt();
        year-=1900;
        System.out.println("Enter check in month: [ 1(January)~12(December) ]");
        int month = sc.nextInt();
        month -= 1;
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
        while (true) {
            List<RoomTypeEntity> availRoomTypes = getAvailableRoomTypes(checkInDate);
            int index = 1;
            System.out.println("==== Room Types with available rooms =====");
            for (RoomTypeEntity roomType : availRoomTypes) {
                System.out.println("#" + index + " RoomType: " + roomType.getRoomTypeName());
                index++;
            }
            System.out.println("==========================================");
            System.out.println("Select desired room type by index");
            int choice = sc.nextInt();
            desiredRoomTypes.add(availRoomTypes.get(choice -= 1));
            System.out.println("Enter 1 to continue adding more room types");
            if (sc.nextInt() != 1) {
                break;
            }
        }
        BigDecimal totalPrice = calculateTotalPrice(desiredRoomTypes, nights);
        System.out.println("Total price: $" + totalPrice);
        //initiate reserve room
        System.out.println("Do you want to reserve rooms?");
        System.out.println("Enter 1 to reserve, Enter 2 to exit");
        int reply = sc.nextInt();
        if (reply == 1) {
            doWalkInReserveRoom(checkInDate, checkOutDate, desiredRoomTypes, sc, totalPrice);
        }

    }

    public List<RoomTypeEntity> getAvailableRoomTypes(Date checkInDate) {
        List<RoomTypeEntity> availRoomTypes = new ArrayList<>();

        //Retrieve all roomtypes that are published and available
        List<RoomTypeEntity> publishedRoomTypes = roomTypeControllerRemote.retrieveRoomTypesByRateType(RateType.PUBLISHED);

        for (RoomTypeEntity roomType : publishedRoomTypes) {
            if (roomControllerRemote.checkAvailabilityOfRoomByRoomTypeId(roomType.getRoomTypeId(), checkInDate)) {
                availRoomTypes.add(roomType);
            }
        }

        return availRoomTypes;
    }

    public void doWalkInReserveRoom(Date checkInDate, Date checkOutDate, List<RoomTypeEntity> desiredRoomTypes, Scanner sc, BigDecimal totalPrice) {
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
            cus = customerControllerRemote.createCustomerEntity(cus); //partial customer. not registered with password, cannot access online reservation
        }
        //create new Reservation
        ReservationEntity reservation = new ReservationEntity(new Date(), checkInDate, checkOutDate, false, cus, ReservationType.WalkIn);
        reservation = reservationControllerRemote.createNewReservation(reservation);

        //allocate room on the spot if want to check in on the spot. if rserve for future date, use timer to allocate.
        Date today = new Date();
        /*SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String testCheckInDate = df.format(checkInDate);
        String testToday = df.format(today);*/
        System.out.println("CheckInDate: "+checkInDate+" today date: "+today);
        
        if(checkInDate.after(today)) {//if future
            //System.out.println("Future");
            for (RoomTypeEntity roomType : desiredRoomTypes) {
                BookingEntity booking = new BookingEntity(roomType, reservation);
                booking = bookingControllerRemote.createBooking(booking);
                reservationControllerRemote.addBookings(reservation.getReservationId(), booking);
            }
        }else {//if today
            //System.out.println("Today");
            for (RoomTypeEntity roomType : desiredRoomTypes) {
                RoomEntity room = roomControllerRemote.walkInAllocateRoom(roomType.getRoomTypeId());
                BookingEntity booking = new BookingEntity(room, reservation);
                booking = bookingControllerRemote.createBooking(booking);
                reservationControllerRemote.addBookings(reservation.getReservationId(), booking);
                
            }
        }

        //create unpaid transaction
        TransactionEntity transaction = new TransactionEntity(totalPrice, loggedInUser, reservation);
        transaction = transactionControllerRemote.createNewTransaction(transaction);
        reservationControllerRemote.addTransaction(reservation.getReservationId(), transaction);

        System.out.println("Reservation " + reservation.getReservationId() + " successfully created");
        System.out.println("==== Finalized bookings and room types : =====");
        System.out.println("Date of reservation: "+reservation.getDateOfReservation());
        List<BookingEntity> finalBookings = reservationControllerRemote.retrieveBookingListByReservationId(reservation.getReservationId());
        for (BookingEntity bookingz : finalBookings) {
            System.out.println("BookingID: " + bookingz.getBookingId() + " Room Type: " + bookingz.getRoomType().getRoomTypeName());
        }
        System.out.println("Total price: $"+transaction.getTotalCost());
        System.out.println("=============================================");
    }

    //what if there is more than 1 published rate for particular room type on that night
    public BigDecimal calculateTotalPrice(List<RoomTypeEntity> roomTypes, int nights) {
        BigDecimal totalAmount = new BigDecimal(0.00);
        for (int i = 0; i < nights; i++) {
            for (RoomTypeEntity roomType : roomTypes) {
                List<RoomRatesEntity> roomRateList = roomTypeControllerRemote.retrieveRoomRateListById(roomType.getRoomTypeId());
                for (RoomRatesEntity roomRate : roomRateList) {
                    if (roomRate.getRateType() == RateType.PUBLISHED) {
                        System.out.println("Rate per night: "+roomRate.getRatePerNight());
                        totalAmount = totalAmount.add(roomRate.getRatePerNight());
                        
                    }
                }
            }
        }
        System.out.println("Total: "+totalAmount);
        return totalAmount;
    }

    public void doCheckInGuest(Scanner sc) {
        sc.nextLine();
        System.out.println("Enter customer contact number:");
        String contactNum = sc.nextLine();
        CustomerEntity cus;
        try {
            cus = customerControllerRemote.retrieveCustomerEntityByContactNumber(contactNum);
            ReservationEntity reservation = customerControllerRemote.retrieveCustomerLatestReservation(cus.getCustomerId());
            List<BookingEntity> bookingList = reservationControllerRemote.retrieveBookingListByReservationId(reservation.getReservationId());

            int index = 1;
            System.out.println("===== Reservation rooms information =====");
            for (BookingEntity booking : bookingList) {
                System.out.print("#:" + index + " Room Type: " + bookingControllerRemote.retriveRoomTypeEntityByBookingId(booking.getBookingId()).getRoomTypeName());
                RoomEntity room = bookingControllerRemote.retrieveRoomEntityByBookingId(booking.getBookingId());
                int roomNum = room.getRoomNumber();
                //System.out.print(" Room Number: " + bookingControllerRemote.retrieveRoomEntityByBookingId(booking.getBookingId()).getRoomNumber() );
                System.out.println("Room Number: "+roomNum);
                index++;
                System.out.println();
            }
            System.out.println("=========================================");
            System.out.println("Enter 1 to confirm check in");
            int reply = sc.nextInt();
            if (reply == 1) {
                for (BookingEntity booking : bookingList) {
                    RoomEntity room = bookingControllerRemote.retrieveRoomEntityByBookingId(booking.getBookingId());
                    roomControllerRemote.changeRoomStatus(room.getRoomId(), RoomStatus.OCCUPIED);
                }
                reservationControllerRemote.customerShowedUp(reservation.getReservationId());
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
            ReservationEntity reservation = customerControllerRemote.retrieveCustomerLatestReservation(cus.getCustomerId());
            List<BookingEntity> bookingList = reservationControllerRemote.retrieveBookingListByReservationId(reservation.getReservationId());
            TransactionEntity transaction = reservationControllerRemote.retrieveTransactionByReservationId(reservation.getReservationId());

            int index = 1;
            System.out.println("===== Reservation rooms information =====");
            for (BookingEntity booking : bookingList) {
                System.out.print("#:" + index + " Room Type: " + bookingControllerRemote.retriveRoomTypeEntityByBookingId(booking.getBookingId()).getRoomTypeName());
                System.out.print(" Room Number: " + bookingControllerRemote.retrieveRoomEntityByBookingId(booking.getBookingId()).getRoomNumber());
                index++;
                System.out.println();
            }
            System.out.println("Total price: $"+transaction.getTotalCost());
            System.out.println("=========================================");
            System.out.println("Enter 1 to confirm check out");
            int reply = sc.nextInt();
            if (reply == 1) {
                for (BookingEntity booking : bookingList) {
                    RoomEntity room = bookingControllerRemote.retrieveRoomEntityByBookingId(booking.getBookingId());
                    roomControllerRemote.changeRoomStatus(room.getRoomId(), RoomStatus.AVAILABLE);
                }
                System.out.println("Rooms set to available");
                //pay transaction
                System.out.println("Enter 1 to make payment");
                reply = sc.nextInt();
                if (reply == 1) {
                    transaction = transactionControllerRemote.payTransaction(transaction.getTransactionId());
                    System.out.println("Transaction " + transaction.getTransactionId() + " paid successfully on " + transaction.getDatePaid());
                    System.out.println("Check out completed.");
                } else {
                    System.out.println("why don't want pay? ):");
                }
            }

        } catch (CustomerNotFoundException ex) {
            System.out.println("Customer not found !");
        }
    }

}
