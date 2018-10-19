/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import Entity.EmployeeEntity;
import Entity.RoomRatesEntity;
import ejb.session.stateless.EmployeeControllerRemote;
import ejb.session.stateless.RoomRateControllerRemote;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author mdk12
 */
public class OperationManagerModule {
    private EmployeeEntity loggedInUser;
    private EmployeeControllerRemote employeeControllerRemote;
    private RoomRateControllerRemote roomRateControllerRemote;
    
    
    public OperationManagerModule(EmployeeEntity loggedInUser, EmployeeControllerRemote employeeControllerRemote) {
        this.loggedInUser = loggedInUser;
        this.employeeControllerRemote = employeeControllerRemote;
        this.roomRateControllerRemote = roomRateControllerRemote;
    }
    
    public void runModule() {
        Scanner sc = new Scanner(System.in);
        int input = 0;
        while(true) {
            System.out.println("==== Welcome to the Operation Manager Module ====");
          
            System.out.println("1: Create new room");
            System.out.println("2: View all rooms");
            System.out.println("3: Create New Room Type");
            System.out.println("4: View All Room Types"); //here can view room type details first then  delete or update also
            System.out.println("4: View Room Allocation Exception Report");
            System.out.println("5: Exit");
            input = 0;
            while(input < 1 || input > 5) {
                System.out.print(">");
                input = sc.nextInt();
                if(input == 1) {
                   // doCreateNewRoom(sc);
                }else if(input == 2){
                 //   doViewAllRooms();
                }else if(input == 3){
                    doCreateNewRoomType(sc);
                }else if(input == 4){
                 //   doViewAllRoomTypes();
                }else if(input == 5) {
                    break;
                }else{
                    System.out.println("Invalid input! Please try again!");
                }
                
            }
            if(input == 5) {
                break;
            }
        }
    }
//    
//        this();
//        this.roomList = roomList;
//        this.roomRateList = roomRateList;
//        this.roomName = roomName;
//        this.description = description;
//        this.size = size;
//        this.bed = bed;
//        this.amenities = amenities;
//        this.capacity = capacity;
//        this.isDisabled = false;    
    public void doCreateNewRoomType(Scanner sc) {
        sc.nextLine();
        System.out.println("Enter Room Type Name: \n");
                System.out.print(">");
        String name = sc.nextLine();
        System.out.println("Enter description: \n");
                System.out.print(">");
        String description = sc.nextLine();
        System.out.println("Enter size of room type (whole square meters): \n");
                System.out.print(">");
        Integer size = sc.nextInt();
        System.out.println("Enter Amenities provided: \n");
                System.out.print(">");
        String amenities = sc.nextLine();
        System.out.println("Enter room type capacity: \n");
                System.out.print(">");
        Integer capacity = sc.nextInt();

        int roomRateId = chooseRoomRate(sc);
        
       
    }
    
    public int chooseRoomRate(Scanner sc){
        List<RoomRatesEntity> roomRatesList = roomRateControllerRemote.retrieveRoomRatesList();
        int i = 1;
        
        System.out.println("Choose room rate for this room type : ");
                System.out.print(">");
        for(RoomRatesEntity roomRateEntity : roomRatesList){
            System.out.println(i + ". " + roomRateEntity.getName() + ", Room Rate Per Night : " + roomRateEntity.getRatePerNight());
        }
        return sc.nextInt();
    }
    
   
    
    
    
}
