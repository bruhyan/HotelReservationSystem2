/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import Entity.EmployeeEntity;
import Entity.GuestRelationOfficer;
import Entity.OperationManager;
import Entity.PartnerEntity;
import Entity.SalesManager;
import Entity.SystemAdministrator;
import ejb.session.stateless.EmployeeControllerRemote;
import ejb.session.stateless.PartnerControllerRemote;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Bryan
 */
public class SystemAdministratorModule {
    private EmployeeEntity loggedInUser;
    private EmployeeControllerRemote employeeControllerRemote;
    private PartnerControllerRemote partnerControllerRemote;

    public SystemAdministratorModule(EmployeeEntity loggedInUser, EmployeeControllerRemote employeeControllerRemote, PartnerControllerRemote partnerControllerRemote) {
        this.loggedInUser = loggedInUser;
        this.employeeControllerRemote = employeeControllerRemote;
        this.partnerControllerRemote = partnerControllerRemote;
    }
    
    public void runModule() {
        Scanner sc = new Scanner(System.in);
        int input = 0;
        while(true) {
            System.out.println("==== Welcome to the System Administrator Module ====");
            System.out.println("1: Create New Employee");
            System.out.println("2: View All Employees");
            System.out.println("3: Create New Partner");
            System.out.println("4: View All Partners");
            System.out.println("5: Exit");
            input = 0;
            while(input < 1 || input > 5) {
                System.out.print(">");
                input = sc.nextInt();
                if(input == 1) {
                    doCreateEmployee(sc);
                }else if(input == 2) {
                    doViewAllEmployees();
                }else if(input == 3) {
                    doCreatePartner(sc);
                }else if(input == 4) {
                    doViewAllPartners();
                }else if(input == 5) {
                    break;
                }
                
            }
            if(input == 5) {
                break;
            }
        }
    }
    
    public void doCreateEmployee(Scanner sc) {
        sc.nextLine();
        System.out.println("==== Create Employee Page ====");
        System.out.println("Enter name");
        String name = sc.nextLine();
        System.out.println("Enter contact number");
        String contact = sc.nextLine();
        System.out.println("Enter email");
        String email = sc.nextLine();
        System.out.println("Enter password");
        String password = sc.nextLine();
        System.out.println("Enter address");
        String address = sc.nextLine();
        int reply = chooseEmployeeType(sc);
        if(reply < 1 || reply > 4) {
            System.out.println("Invalid input");
        }else {
            if(reply == 1) {
                SystemAdministrator sa = new SystemAdministrator(name, contact, email, password, address);
                sa = employeeControllerRemote.createSystemAdministrator(sa);
                System.out.println("System Administrator "+sa.getEmail()+" Created Successfully.");
            }else if(reply == 2) {
                OperationManager om = new OperationManager(name, contact, email, password, address);
                om = employeeControllerRemote.createOperationManager(om);
                System.out.println("Operation Manager "+om.getEmail()+" Created Successfully.");
            }else if(reply == 3) {
                SalesManager sm = new SalesManager(name, contact, email, password, address);
                sm = employeeControllerRemote.createSalesManager(sm);
                System.out.println("Sales Manager "+sm.getEmail()+" Created Successfully.");
            }else if(reply == 4) {
                GuestRelationOfficer gro = new GuestRelationOfficer(name, contact, email, password, address);
                gro = employeeControllerRemote.createGuestRelationOfficer(gro);
                System.out.println("Guest Relation Officer "+gro.getEmail()+" Created Successfully.");
            }
        }
        //System.out.println("");
    }
    
    
    public int chooseEmployeeType(Scanner sc) {
        System.out.println("Choose Employee Type:");
        System.out.println("1: System Administrator");
        System.out.println("2: Operation Manager");
        System.out.println("3: Sales Manager");
        System.out.println("4: Guest Relation Officer");
        int reply = sc.nextInt();
        sc.nextLine();
        
        return reply;
        
    }
    
    public void doCreatePartner(Scanner sc) {
        sc.nextLine();
        System.out.println("==== Create Partner Page =====");
        System.out.println("Enter partner email: ");
        String email = sc.nextLine();
        System.out.println("Enter password: ");
        String password = sc.nextLine();
        System.out.println("Enter partner name: ");
        String partnerName = sc.nextLine();
        System.out.println("Enter contact number: ");
        String contactNum = sc.nextLine();
        Date createdAt = new Date();
        PartnerEntity newPartner = new PartnerEntity(email, password, partnerName, contactNum, createdAt);
        newPartner = partnerControllerRemote.createPartnerEntity(newPartner);
        System.out.println("New partner "+newPartner.getEmail()+" created successfully");
        
    }
    
    public void doViewAllEmployees() {
        System.out.println("=====================================================================================================================");
        List<EmployeeEntity> employees = employeeControllerRemote.retrieveAllEmployees();
        int index = 1;
        for(EmployeeEntity em : employees) {
            System.out.println("Index: "+index+" Name: "+em.getName()+" Email: "+em.getEmail()+" ContactNumber: "+em.getContactNumber()+" Address: "+em.getAddress());
            index++;
        }
        System.out.println("=====================================================================================================================");
    }
    
    
    public void doViewAllPartners() {
        System.out.println("=====================================================================================================================");
        List<PartnerEntity> partners = partnerControllerRemote.retrieveAllPartner();
        int index = 1;
        for(PartnerEntity pt : partners) {
            System.out.println("Index: "+index+" Name: "+pt.getName()+" Email: "+pt.getEmail()+" ContactNumber: "+pt.getContactNumber()+" Created At: "+pt.getCreatedAt());
            index++;
        }
        System.out.println("=====================================================================================================================");
        
    }
    
    
    
}
