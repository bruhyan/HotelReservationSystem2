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
import javafx.util.Pair;
import util.enumeration.RateType;
import util.enumeration.ReservationType;
import util.enumeration.RoomStatus;
import util.exception.CustomerNotFoundException;
import util.exception.NoReservationFoundException;

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

        boolean valid = false;
        Date checkInDate = null;
        while (!valid) {
            System.out.println("Enter check in year: [YYYY]");
            int year = sc.nextInt();
            year -= 1900;
            System.out.println("Enter check in month: [ 1(January)~12(December) ]");
            int month = sc.nextInt();
            month -= 1;
            System.out.println("Enter check in date [1 ~ 31]"); //2pm check in
            int day = sc.nextInt();
            Date today = new Date();
            checkInDate = new Date(year, month, day, 14, 0);
            Date registerDate = new Date(year, month, day, 23, 59);
            if (registerDate.before(today)) {
                System.out.println("You've entered a date that's before your current date or before our check in time of 2pm. Please try a later date!");
            } else {
                valid = true;
            }
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(checkInDate);
        System.out.println("Enter how many nights of stay"); //12pm checkout
        int nights = sc.nextInt();
        cal.add(Calendar.DATE, nights);
        cal.set(Calendar.HOUR_OF_DAY, 12);
        Date checkOutDate = cal.getTime();
        List<RoomTypeEntity> allRoomTypes = roomTypeControllerRemote.retrieveRoomTypeList();
        List<RoomTypeEntity> desiredRoomTypes = new ArrayList<>();
        List<Pair<RoomTypeEntity, Integer>> listOfRoomTypePairs = new ArrayList<>();
        boolean retrieved = false;
        while (true) {
            //List<RoomTypeEntity> availRoomTypes = getAvailableRoomTypes(checkInDate);

            if (retrieved == false) {
                for (RoomTypeEntity roomType : allRoomTypes) {
                    if (roomType.isIsDisabled()) {
                        continue;
                    }
                    Integer roomTypeCount = roomControllerRemote.getNumberOfBookableRoomType(roomType, checkInDate, checkOutDate);
                    Pair<RoomTypeEntity, Integer> roomTypePair = new Pair<>(roomType, roomTypeCount);
                    listOfRoomTypePairs.add(roomTypePair);
                }
            }
            retrieved = true;
            int index = 1;
            System.out.println("==== Room Types with available rooms =====");
            for (Pair<RoomTypeEntity, Integer> roomTypePair : listOfRoomTypePairs) {
                System.out.println("#" + index + " RoomType: " + roomTypePair.getKey().getRoomTypeName() + ". Available Room Count : " + roomTypePair.getValue());
                index++;
            }
            System.out.println("==========================================");
            System.out.println("Select desired room type by index");
            int choice = sc.nextInt();
            choice--;
            if (listOfRoomTypePairs.get(choice).getValue() == 0) {
                System.out.println("Sorry! There are no available room for this room type during the given check in and out date!");
                System.out.println("Please choose another room type.");
            } else {
                desiredRoomTypes.add(listOfRoomTypePairs.get(choice).getKey());
                Pair<RoomTypeEntity, Integer> newPair = new Pair<RoomTypeEntity, Integer>(listOfRoomTypePairs.get(choice).getKey(), listOfRoomTypePairs.get(choice).getValue() - 1);
                listOfRoomTypePairs.set(choice, newPair);
                System.out.println("Enter 1 to continue adding more room types");
                if (sc.nextInt() != 1) {
                    break;
                }
            }
        }
        BigDecimal totalPrice = calculateTotalPrice(desiredRoomTypes, nights);
        System.out.println("Total price: $" + totalPrice);
        //initiate reserve room
        System.out.println("Do you want to reserve rooms?");
        System.out.println("Enter 1 to reserve, Enter 2 to exit");
        int reply = sc.nextInt();
        if (reply == 1) {
            if (loggedInUser != null) {
                doWalkInReserveRoom(checkInDate, checkOutDate, desiredRoomTypes, sc, totalPrice);
            } else {
                System.out.println("You must be logged in to reserve rooms.");
            }
        }

    }

    public List<RoomTypeEntity> getAvailableRoomTypes(Date checkInDate) {
        List<RoomTypeEntity> availRoomTypes = new ArrayList<>();

        //Retrieve all roomtypes that are published and available
        List<RoomTypeEntity> publishedRoomTypes = roomTypeControllerRemote.retrieveRoomTypesByRateType(RateType.PUBLISHED);

        // available + published filter , Need see if available now + available in future here.
        for (RoomTypeEntity roomType : publishedRoomTypes) {

            if (!roomType.isIsDisabled()) {
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
        System.out.println("CheckInDate: " + checkInDate + " today date: " + today);

        if (checkInDate.after(today)) {//if future
            //System.out.println("Future");
            for (RoomTypeEntity roomType : desiredRoomTypes) {
                BookingEntity booking = new BookingEntity(roomType, reservation);
                booking = bookingControllerRemote.createBooking(booking);
                reservationControllerRemote.addBookings(reservation.getReservationId(), booking);
            }
        } else {//if today
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
        try {
            reservationControllerRemote.addTransaction(reservation.getReservationId(), transaction);
        } catch (NoReservationFoundException ex) {
            System.out.println("No reservation was found with the given ID!");
        }

        System.out.println("Reservation " + reservation.getReservationId() + " successfully created");
        System.out.println("==== Finalized bookings and room types : =====");
        System.out.println("Date of reservation: " + reservation.getDateOfReservation());
        List<BookingEntity> finalBookings = reservationControllerRemote.retrieveBookingListByReservationId(reservation.getReservationId());
        for (BookingEntity bookingz : finalBookings) {
            System.out.println("BookingID: " + bookingz.getBookingId() + " Room Type: " + bookingz.getRoomType().getRoomTypeName());
        }
        System.out.println("Total price: $" + transaction.getTotalCost());
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
                        //System.out.println("Rate per night: "+roomRate.getRatePerNight());
                        totalAmount = totalAmount.add(roomRate.getRatePerNight());

                    }
                }
            }
        }
        //System.out.println("Total: "+totalAmount);
        return totalAmount;
    }

    public void doCheckInGuest(Scanner sc) {
        sc.nextLine();
        System.out.println("Enter customer contact number:");
        String contactNum = sc.nextLine();
        CustomerEntity cus;
        try {
            cus = customerControllerRemote.retrieveCustomerEntityByContactNumber(contactNum);
            boolean exit = false;
            while (exit == false) {
                List<ReservationEntity> reservationsForCheckIn = customerControllerRemote.retrieveReservationsForCheckIn(cus.getCustomerId());
                if (reservationsForCheckIn.size() == 0) {
                    System.out.println("Customer :" + cus.getEmail() + " has no more reservations to check in");
                    exit = true;
                } else {
                    System.out.println("===== List of unpaid reservations of customer: " + cus.getEmail() + " =====");
                    int index = 1;
                    for (ReservationEntity reservation : reservationsForCheckIn) {
                        System.out.println("#" + index + " Reservation ID: " + reservation.getReservationId() + " Reservation Date: " + reservation.getDateOfReservation());
                    }
                    System.out.println("========================================================================");
                    System.out.println();
                    System.out.println("Select reservation to check out by index number #");
                    int reply = sc.nextInt();
                    ReservationEntity selectedReservation = reservationsForCheckIn.get(reply -= 1);
                    List<BookingEntity> bookingList = reservationControllerRemote.retrieveBookingListByReservationId(selectedReservation.getReservationId());
                    TransactionEntity transaction = reservationControllerRemote.retrieveTransactionByReservationId(selectedReservation.getReservationId());

                    index = 1;
                    System.out.println("===== Reservation rooms information =====");
                    for (BookingEntity booking : bookingList) {
                        System.out.print("#:" + index + " Room Type: " + bookingControllerRemote.retriveRoomTypeEntityByBookingId(booking.getBookingId()).getRoomTypeName());
                        System.out.print(" Room Number: " + bookingControllerRemote.retrieveRoomEntityByBookingId(booking.getBookingId()).getRoomNumber());
                        index++;
                        System.out.println();
                    }
                    System.out.println("Total price: $" + transaction.getTotalCost());
                    System.out.println("=========================================");
                    System.out.println("Enter 1 to confirm check in");
                    reply = sc.nextInt();
                    if (reply == 1) {
                        for (BookingEntity booking : bookingList) {
                            RoomEntity room = bookingControllerRemote.retrieveRoomEntityByBookingId(booking.getBookingId());
                            roomControllerRemote.changeRoomStatus(room.getRoomId(), RoomStatus.OCCUPIED);
                            roomControllerRemote.changeIsReserved(room.getRoomId(), false);
                        }
                        reservationControllerRemote.customerShowedUp(selectedReservation.getReservationId());
                        System.out.println("Check in completed");
                        System.out.println("Enter 1 to continue, 2 to exit");
                        int input = sc.nextInt();
                        if (input == 2) {
                            exit = true;
                        }
                    } else {
                        exit = true;
                    }
                }
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

            boolean exit = false;
            while (exit == false) {
                List<ReservationEntity> unpaidReservations = customerControllerRemote.retrieveCustomerUnpaidReservation(cus.getCustomerId());
                if (unpaidReservations.size() == 0) {
                    System.out.println("Customer: " + cus.getEmail() + " has no more reservations to check out.");
                    exit = true;
                } else {
                    int index = 1;
                    System.out.println("===== List of unpaid reservations of customer: " + cus.getEmail() + " =====");
                    for (ReservationEntity reservation : unpaidReservations) {
                        System.out.println("#" + index + " Reservation ID: " + reservation.getReservationId() + " Reservation Date: " + reservation.getDateOfReservation());
                        index++;
                    }
                    System.out.println("========================================================================");
                    System.out.println();
                    System.out.println("Select reservation to check out by index number #");
                    int reply = sc.nextInt();
                    ReservationEntity selectedReservation = unpaidReservations.get(reply -= 1);
                    List<BookingEntity> bookingList = reservationControllerRemote.retrieveBookingListByReservationId(selectedReservation.getReservationId());
                    TransactionEntity transaction = reservationControllerRemote.retrieveTransactionByReservationId(selectedReservation.getReservationId());

                    index = 1;
                    System.out.println("===== Reservation rooms information =====");
                    for (BookingEntity booking : bookingList) {
                        System.out.print("#:" + index + " Room Type: " + bookingControllerRemote.retriveRoomTypeEntityByBookingId(booking.getBookingId()).getRoomTypeName());
                        System.out.print(" Room Number: " + bookingControllerRemote.retrieveRoomEntityByBookingId(booking.getBookingId()).getRoomNumber());
                        index++;
                        System.out.println();
                    }
                    System.out.println("Total price: $" + transaction.getTotalCost());
                    System.out.println("=========================================");
                    System.out.println("Enter 1 to confirm check out");
                    reply = sc.nextInt();
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
                            System.out.println("Enter 1 to continue, Enter 2 to exit");
                            int input = sc.nextInt();
                            if (input == 2) {
                                exit = true;
                            }
                        } else {
                            System.out.println("why don't want pay? ):");
                            exit = true;
                        }
                    } else {
                        exit = true;
                    }
                }
            }

        } catch (CustomerNotFoundException ex) {
            System.out.println("Customer not found !");
        }
    }

}
