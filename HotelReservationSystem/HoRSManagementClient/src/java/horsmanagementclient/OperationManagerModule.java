/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import Entity.EmployeeEntity;
import Entity.RoomRatesEntity;
import Entity.RoomTypeEntity;
import ejb.session.stateless.EmployeeControllerRemote;
import ejb.session.stateless.RoomControllerRemote;
import ejb.session.stateless.RoomRateControllerRemote;
import ejb.session.stateless.RoomTypeControllerRemote;
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
    private RoomControllerRemote roomControllerRemote;
    private RoomTypeControllerRemote roomTypeControllerRemote;

    public OperationManagerModule(EmployeeEntity loggedInUser, EmployeeControllerRemote employeeControllerRemote, RoomControllerRemote roomControllerRemote, RoomRateControllerRemote roomRateControllerRemote, RoomTypeControllerRemote roomTypeControllerRemote) {
        this.loggedInUser = loggedInUser;
        this.employeeControllerRemote = employeeControllerRemote;
        this.roomRateControllerRemote = roomRateControllerRemote;
        this.roomControllerRemote = roomControllerRemote;
        this.roomTypeControllerRemote = roomTypeControllerRemote;
    }

    public void runModule() {
        Scanner sc = new Scanner(System.in);
        int input = 0;
        while (true) {
            System.out.println("==== Welcome to the Operation Manager Module ====");

            System.out.println("1: Create new room");
            System.out.println("2: View all rooms");
            System.out.println("3: Create New Room Type");
            System.out.println("4: View All Room Types"); //here can view room type details first then  delete or update also
            System.out.println("4: View Room Allocation Exception Report");
            System.out.println("5: Exit");
            input = 0;
            while (input < 1 || input > 5) {
                System.out.print(">");
                input = sc.nextInt();
                if (input == 1) {
                    // doCreateNewRoom(sc);
                } else if (input == 2) {
                    //   doViewAllRooms();
                } else if (input == 3) {
                    doCreateNewRoomType(sc);
                } else if (input == 4) {
                    doViewAllRoomTypes(sc);
                } else if (input == 5) {
                    break;
                } else {
                    System.out.println("Invalid input! Please try again!");
                }

            }
            if (input == 5) {
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

    public void doViewAllRoomTypes(Scanner sc) {
        
        List<RoomTypeEntity> roomTypeList = roomTypeControllerRemote.retrieveRoomTypeList();
        if(roomTypeList.isEmpty()){
            System.out.println("You currently have no room types available! Please create a new one first.");

        }else{
            System.out.println("Select the room type you'd wish to view in more detail, update or delete :");
        for (RoomTypeEntity roomType : roomTypeList) {

            System.out.println(roomType.getRoomTypeId() + ". Room type name : " + roomType.getRoomName() + " .");

        }
        System.out.print(">");
        Long roomTypeId = sc.nextLong();
        sc.nextLine();
        viewSingleRoomType(roomTypeId, sc);
        }
    }

    public void viewSingleRoomType(Long roomTypeId, Scanner sc) {
        RoomTypeEntity roomType = roomTypeControllerRemote.retrieveRoomTypeById(roomTypeId);
        int response = 0;
        System.out.println("============= Selected Room Type Information:  ===========");
        System.out.println("Room Type Name: " + roomType.getRoomName());
        System.out.println("Room Type description: " + roomType.getDescription());
        System.out.println("Room Type amenities: " + roomType.getAmenities());
        System.out.println("Room Type bed: " + roomType.getBed());
        System.out.println("Room Type capacity: " + roomType.getCapacity());
        System.out.println("Room Type Size: " + roomType.getSize() + " metres square.");
        System.out.println("Room Type disabled: " + roomType.isIsDisabled());

        System.out.println("==============================================================");
        while (response < 1 || response > 3) {
            System.out.println("Would you like to 1. Update 2. Delete 3. Exit ?");
            response = sc.nextInt();
            sc.nextLine();
            if (response == 1) {
                doUpdateRoomType(roomType, sc); //probably just print all the artibutes, only thing same is the ID
            } else if (response == 2) {
                doDeleteRoomType(roomTypeId);
            } else if (response == 3) {
                break;
            } else {
                System.out.println("Invalid response! Please try again.");
            }
        }

    }
    
    public void doUpdateRoomType(RoomTypeEntity roomType, Scanner sc){
        System.out.println("Enter new Room Type Name: ( previous: " + roomType.getRoomName() + " ) \n");
        System.out.print(">");
        String name = sc.nextLine();
        System.out.println("Enter new description: ( previous: " + roomType.getDescription() + " ) \n");
        System.out.print(">");
        String description = sc.nextLine();
        System.out.println("Enter new size of room type (whole square meters): ( previous: " + roomType.getSize() + " ) \n");
        System.out.print(">");
        Integer size = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter new Bed provided: ( previous: " + roomType.getBed() + " ) \n");
        System.out.print(">");
        String bed = sc.nextLine();
        System.out.println("Enter new Amenities provided: ( previous: " + roomType.getAmenities() + " ) \n");
        System.out.print(">");
        String amenities = sc.nextLine();
        System.out.println("Enter new room type capacity: ( previous: " + roomType.getCapacity() + " ) \n");
        System.out.print(">");
        Integer capacity = sc.nextInt();
        
        roomTypeControllerRemote.heavyUpdateRoom(roomType.getRoomTypeId(), name, description, size, bed, amenities, capacity);
        System.out.println("Room type has been updated!");
    }
    
    public void doDeleteRoomType(Long roomTypeId){
        roomTypeControllerRemote.deleteRoomTypeById(roomTypeId);
        System.out.println("Room type has been deleted.");
    }

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
        sc.nextLine();
        System.out.println("Enter Bed provided: \n");
        System.out.print(">");
        String bed = sc.nextLine();
        System.out.println("Enter Amenities provided: \n");
        System.out.print(">");
        String amenities = sc.nextLine();
        System.out.println("Enter room type capacity: \n");
        System.out.print(">");
        Integer capacity = sc.nextInt();

        Long roomRateId = chooseRoomRate(sc);
        sc.nextLine();

        RoomTypeEntity newRoomType = new RoomTypeEntity(name, description, size, bed, amenities, capacity);
        newRoomType = roomTypeControllerRemote.createNewRoomType(newRoomType);

        System.out.println(" Room Type and room rate id here " + newRoomType.getRoomTypeId() + " " + roomRateId);
        roomTypeControllerRemote.addRoomRateById(newRoomType.getRoomTypeId(), roomRateId);
        int response = 0;
        //add another roomrate here if needed
        while (true) {
            while (response < 1 || response > 2) {
                System.out.println("Add another room rate?");
                System.out.println("1. Yes");
                System.out.println("2. No, System will create the room type and return you to the operation manager page.");
                System.out.print(">");
                response = sc.nextInt();
                sc.nextLine();
                if (response == 1) {
                    addAnotherRoomRate(newRoomType, sc);
                } else if (response == 2) {
                    break;
                }
            }
            break;
        }

    }

    public void addAnotherRoomRate(RoomTypeEntity roomType, Scanner sc) {
        Long roomRateId = chooseRoomRate(sc);
        roomTypeControllerRemote.addRoomRateById(roomType.getRoomTypeId(), roomRateId);
    }

    public Long chooseRoomRate(Scanner sc) {
        List<RoomRatesEntity> roomRatesList = roomRateControllerRemote.retrieveRoomRatesList();
        int i = 1;

        System.out.println("Choose room rate for this room type : ");

        for (RoomRatesEntity roomRateEntity : roomRatesList) { //come back check for date validity next time.
            System.out.println(i + ". " + roomRateEntity.getName() + ", Room Rate : $" + roomRateEntity.getRatePerNight() + " Per Night");
            i++;
        }
        System.out.print(">");
        return sc.nextLong();
    }

}
