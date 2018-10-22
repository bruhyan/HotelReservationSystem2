/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import Entity.EmployeeEntity;
import Entity.RoomEntity;
import Entity.RoomTypeEntity;
import ejb.session.stateless.EmployeeControllerRemote;
import ejb.session.stateless.RoomControllerRemote;
import ejb.session.stateless.RoomTypeControllerRemote;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author mdk12
 */
public class FrontOfficeModule {
        private EmployeeEntity loggedInUser;
        private EmployeeControllerRemote employeeControllerRemote;
        private RoomControllerRemote roomControllerRemote;
        private RoomTypeControllerRemote roomTypeControllerRemote;

    
    public FrontOfficeModule(){
    
    }
    
    public FrontOfficeModule(EmployeeEntity loggedInUser, EmployeeControllerRemote employeeControllerRemote, RoomControllerRemote roomControllerRemote, RoomTypeControllerRemote roomTypeControllerRemote){
        this();
        this.loggedInUser = loggedInUser;
        this.employeeControllerRemote = employeeControllerRemote;
        this.roomControllerRemote = roomControllerRemote;
        this.roomTypeControllerRemote = roomTypeControllerRemote;
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
        
        List<RoomEntity> availRooms = new ArrayList<>();
        
        //retrieve all room types
        List<RoomTypeEntity> allRoomTypes = roomTypeControllerRemote.retrieveRoomTypeList();
        for(RoomTypeEntity roomType : allRoomTypes) {
            List<RoomEntity> roomsByType = roomTypeControllerRemote.retrieveRoomEntityByRoomType(roomType);
            for(RoomEntity room : roomsByType) {
                if(room.getBooking() == null || checkInDate.after(room.getBooking().getReservation().getCheckOutDateTime())) {
                    availRooms.add(room);
                    break;
                }
            }
        }
        
        for(RoomEntity availRoom : availRooms) {
            System.out.println("Room Type: "+availRoom.getRoomType()+" Room Number: "+availRoom.getRoomNumber());
        }
        
        
    }
    
    
    
    
}
