/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsreservationclient;

import Entity.BookingEntity;
import Entity.CustomerEntity;
import Entity.ReservationEntity;
import Entity.RoomEntity;
import Entity.RoomRatesEntity;
import Entity.RoomTypeEntity;
import Entity.TransactionEntity;
import ejb.session.stateless.BookingControllerRemote;
import ejb.session.stateless.CustomerControllerRemote;
import ejb.session.stateless.ReservationControllerRemote;
import ejb.session.stateless.RoomControllerRemote;
import ejb.session.stateless.RoomTypeControllerRemote;
import ejb.session.stateless.TransactionControllerRemote;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import util.enumeration.RateType;
import util.enumeration.ReservationType;
import util.exception.CustomerNotFoundException;
import util.exception.NoAvailableOnlineRoomRateException;
import util.exception.NoReservationFoundException;

/**
 *
 * @author Bryan
 */
public class MainApp {

    private CustomerEntity loggedInUser;
    private CustomerControllerRemote customerControllerRemote;
    private RoomTypeControllerRemote roomTypeControllerRemote;
    private RoomControllerRemote roomControllerRemote;
    private ReservationControllerRemote reservationControllerRemote;
    private BookingControllerRemote bookingControllerRemote;
    private TransactionControllerRemote transactionControllerRemote;

    public MainApp() {
    }

    public MainApp(CustomerControllerRemote customerControllerRemote, RoomTypeControllerRemote roomTypeControllerRemote, RoomControllerRemote roomControllerRemote, ReservationControllerRemote reservationControllerRemote, BookingControllerRemote bookingControllerRemote, TransactionControllerRemote transactionControllerRemote) {
        this.customerControllerRemote = customerControllerRemote;
        this.roomTypeControllerRemote = roomTypeControllerRemote;
        this.roomControllerRemote = roomControllerRemote;
        this.reservationControllerRemote = reservationControllerRemote;
        this.bookingControllerRemote = bookingControllerRemote;
        this.transactionControllerRemote = transactionControllerRemote;

    }

    public void runApp() {
        Scanner sc = new Scanner(System.in);
        int input = 0;
        while (true) {
            System.out.println("==== Welcome to the HoRS Reservation Client ====");
            if (loggedInUser == null) {
                System.out.println("1: Guest Login");
                System.out.println("2: Register as Guest");
                System.out.println("3: Search Hotel Room");
                System.out.println("4: Exit");
            }
            if (loggedInUser != null) {
                System.out.println("1: Guest Logout");
                System.out.println("2: Search Hotel Room");
                System.out.println("3: Exit");
                System.out.println("4: View My Reservation Details");
                System.out.println("5: View All My Reservations");
            }
            System.out.println("===============================================");
            input = 0;
            while (input < 1 || input > 5) {
                System.out.print(">");
                input = sc.nextInt();
                if (input == 1) {
                    if (loggedInUser == null) {
                        doLogin(sc);
                    } else {
                        //System.out.println("Current logged in user: "+loggedInUser.getEmail());
                        doLogout();
                    }
                } else if (input == 2 && loggedInUser == null) {
                    doGuestRegistration(sc);
                } else if (input == 2 && loggedInUser != null || input == 3 && loggedInUser == null) {
                    doSearchHotelRoom(sc);
                } else if (input == 3 && loggedInUser != null || input == 4 && loggedInUser == null) {
                    break;
                } else if (input == 4 && loggedInUser != null) {
                    doViewMyReservationDetails(sc);
                } else if (input == 5 && loggedInUser != null) {
                    doViewAllMyReservations();
                }
            }
            if (input == 3 && loggedInUser != null || input == 4 && loggedInUser == null) {
                break;
            }
        }
    }

    public void doSearchHotelRoom(Scanner sc) {
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
            checkInDate = new Date(year, month, day, 14, 0);
            Date registerDate = new Date(year, month, day, 23, 59);
            Date today = new Date();
            if (registerDate.before(today)) {
                System.out.println("You've entered a date that's before the present. Please try a later date!");
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

            if (retrieved == false) {
                for (RoomTypeEntity roomType : allRoomTypes) {
                    if (roomType.isIsDisabled() || !roomTypeControllerRemote.checkIfHaveNormal(roomType.getRoomTypeId())) {
                        continue;
                    }
                    Integer roomTypeCount = roomControllerRemote.getNumberOfBookableRoomType(roomType, checkInDate, checkOutDate);
                    Pair<RoomTypeEntity, Integer> roomTypePair = new Pair<>(roomType, roomTypeCount);
                    listOfRoomTypePairs.add(roomTypePair);
                }
            }
            retrieved = true;
            //  List<RoomTypeEntity> availRoomTypes = getAvailableRoomTypes();
            int index = 1;
            System.out.println("==========================================");
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
        BigDecimal totalPrice = calculateTotalPrice(desiredRoomTypes, nights, checkInDate);
        System.out.println("Total price : $" + totalPrice);
        //initiate reserve room
        System.out.println("Do you want to reserve rooms?");
        System.out.println("Enter 1 to reserve, Enter 2 to exit");
        int reply = sc.nextInt();
        if (reply == 1) {
            if (loggedInUser != null) {
                doOnlineReserveRoom(checkInDate, checkOutDate, desiredRoomTypes, sc, totalPrice);
            } else {
                System.out.println("You must be logged in to reserve rooms.");
            }
        }
    }

    public void doOnlineReserveRoom(Date checkInDate, Date checkOutDate, List<RoomTypeEntity> desiredRoomTypes, Scanner sc, BigDecimal totalPrice) {
        try {
            sc.nextLine();

            //create new reservation
            ReservationEntity reservation = new ReservationEntity(new Date(), checkInDate, checkOutDate, false, loggedInUser, ReservationType.Online);
            reservation = reservationControllerRemote.createNewReservation(reservation);

            //allocate room on the spot if want to check in on the spot. if rserve for future date, use timer to allocate.
            Date today = new Date();
            //System.out.println("CheckInDate: " + checkInDate + " today date: " + today);

            if (checkInDate.after(today)) { //if future
                for (RoomTypeEntity roomType : desiredRoomTypes) {
                    BookingEntity booking = new BookingEntity(roomType, reservation);
                    booking = bookingControllerRemote.createBooking(booking);
                    reservationControllerRemote.addBookings(reservation.getReservationId(), booking);
                }
            } else { //if today
                for (RoomTypeEntity roomType : desiredRoomTypes) {
                    RoomEntity room = roomControllerRemote.walkInAllocateRoom(roomType.getRoomTypeId());
                    BookingEntity booking = new BookingEntity(room, reservation);
                    booking = bookingControllerRemote.createBooking(booking);
                    reservationControllerRemote.addBookings(reservation.getReservationId(), booking);
                }
            }

            //create unpaid transaction
            TransactionEntity transaction = new TransactionEntity(totalPrice, null, reservation);
            transaction = transactionControllerRemote.createNewTransaction(transaction);
            reservationControllerRemote.addTransaction(reservation.getReservationId(), transaction);

            System.out.println("Reservation " + reservation.getReservationId() + " successfully created");
            System.out.println("==== Finalized bookings and room types : ====");
            System.out.println("Date of reservation: " + reservation.getDateOfReservation());
            List<BookingEntity> finalBookings = reservationControllerRemote.retrieveBookingListByReservationId(reservation.getReservationId());
            for (BookingEntity bookings : finalBookings) {
                System.out.println("BookingID: " + bookings.getBookingId() + " Room Type: " + bookings.getRoomType().getRoomTypeName());
            }
            System.out.println("Total price: $" + transaction.getTotalCost());
            System.out.println("=============================================");
        } catch (NoReservationFoundException ex) {

            System.out.println("Reservation Not Found !");

        }

    }

    public Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days); //minus number would decrement the days
        return calendar.getTime();
    }

    public BigDecimal calculateTotalPrice(List<RoomTypeEntity> roomTypes, int nights, Date currentDay) {
        BigDecimal totalPrice = new BigDecimal(0.00);

        for (int i = 0; i < nights; i++) {
            for (RoomTypeEntity roomType : roomTypes) {
                try {

                    RoomRatesEntity prevailingRate = roomTypeControllerRemote.findOnlineRateForRoomType(roomType.getRoomTypeId(), currentDay);
                    BigDecimal priceForTheNight = prevailingRate.getRatePerNight();
                    totalPrice = totalPrice.add(priceForTheNight);

                } catch (NoAvailableOnlineRoomRateException ex) {
                    System.out.println(ex.getMessage());
                }
            }

            currentDay = addDays(currentDay, 1);
        }
        return totalPrice;
    }

    //something wong: what if not available now but will be available before check in
    public List<RoomTypeEntity> getAvailableRoomTypes() {
        List<RoomTypeEntity> availRoomTypes = new ArrayList<>();
        List<RoomTypeEntity> onlineRoomTypes = roomTypeControllerRemote.retrieveRoomTypesByRateType(RateType.NORMAL);
        for (RoomTypeEntity roomType : onlineRoomTypes) {

            if (!roomType.isIsDisabled()) {
                availRoomTypes.add(roomType);

            }
        }
        return availRoomTypes;
    }

    public void doViewMyReservationDetails(Scanner sc) {
        List<ReservationEntity> reservations = doViewAllMyReservations();
        System.out.println("Select which reservation to view");
        int choice = sc.nextInt();
        ReservationEntity selected = reservations.get(choice -= 1);
        selected = reservationControllerRemote.retrieveReservationById(selected.getReservationId());
        System.out.println("==============================================================");
        System.out.println("Reservation ID: " + selected.getReservationId());
        System.out.println("Reservation type: " + selected.getReservationType());
        System.out.println("Guest name: " + selected.getCustomer().getFirstName()); //do i need to go through controller to get the customer?
        System.out.println("Guest email: " + selected.getCustomer().getEmail());
        System.out.println("Reservation date: " + selected.getDateOfReservation());
        System.out.println("Check in date: " + selected.getCheckInDateTime());
        System.out.println("Check out date: " + selected.getCheckOutDateTime());
        System.out.println("==============================================================");
        System.out.println("Room types booked with this reservation :");
        List<BookingEntity> bookingList = selected.getBookingList();
        for (BookingEntity booking : bookingList) {
            System.out.println("Booking ID : " + booking.getBookingId() + ". Room Type : " + booking.getRoomType().getRoomTypeName());
        }
        System.out.println("The total price for your reservation is : $" + selected.getTransaction().getTotalCost());
        System.out.println("==============================================================");
    }

    public List<ReservationEntity> doViewAllMyReservations() {
        Long customerId = loggedInUser.getCustomerId();
        List<ReservationEntity> reservations = customerControllerRemote.retrieveCustomerReservation(customerId);
        int index = 1;
        System.out.println("============= Reservation List =================");
        for (ReservationEntity reservation : reservations) {

            System.out.println("#" + index + " Reservation ID: " + reservation.getReservationId());
            index++;
        }
        System.out.println("================================================");
        return reservations;
    }

    public void doLogin(Scanner sc) {
        sc.nextLine();
        System.out.println("===== Guest Login Page =====");
        System.out.println("Enter email");
        String email = sc.nextLine();
        System.out.println("Enter password");
        String password = sc.nextLine();
        CustomerEntity customer;
        try {
            customer = customerControllerRemote.retrieveCustomerByEmail(email);
            if (customer.getPassword().equals(password)) {
                System.out.println("Welcome " + customer.getFirstName() + ".");
                loggedInUser = customer;
            } else {
                System.out.println("Incorrect Password!");
            }
        } catch (CustomerNotFoundException ex) {
            System.out.println("Invalid customer credentials!");
        }

    }

    public void doGuestRegistration(Scanner sc) {
        sc.nextLine();
        System.out.println("===== Guest Registration Page ======");
        System.out.println("Enter email:");
        String email = sc.nextLine();
        System.out.println("Enter contact number:");
        String contactNumber = sc.nextLine();
        System.out.println("Enter first name:");
        String firstName = sc.nextLine();
        System.out.println("Enter last name:");
        String lastName = sc.nextLine();
        System.out.println("Enter password:");
        String password = sc.nextLine();

        CustomerEntity customer = new CustomerEntity(email, contactNumber, firstName, lastName, password);
        customer = customerControllerRemote.createCustomerEntity(customer);
        System.out.println("Customer " + customer.getEmail() + " successfully created");
    }

    public void doLogout() {
        System.out.println(loggedInUser.getEmail() + " successfully logged out");

        loggedInUser = null;
    }
}
