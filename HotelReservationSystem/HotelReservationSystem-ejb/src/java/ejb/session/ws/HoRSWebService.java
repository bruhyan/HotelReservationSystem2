/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import Entity.PartnerEntity;
import Entity.RoomTypeEntity;
import ejb.session.stateless.BookingControllerLocal;
import ejb.session.stateless.CustomerControllerLocal;
import ejb.session.stateless.PartnerControllerLocal;
import ejb.session.stateless.ReservationControllerLocal;
import ejb.session.stateless.RoomControllerLocal;
import ejb.session.stateless.RoomRateControllerLocal;
import ejb.session.stateless.RoomTypeControllerLocal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.enumeration.RateType;
import util.exception.PartnerNotFoundException;

/**
 *
 * @author Bryan
 */
@WebService(serviceName = "HoRSWebService")
@Stateless

public class HoRSWebService {

    @EJB
    private RoomControllerLocal roomControllerLocal;
    @EJB
    private CustomerControllerLocal customerControllerLocal;
    @EJB
    private BookingControllerLocal bookingControllerLocal;
    @EJB
    private RoomRateControllerLocal roomRateControllerLocal;
    @EJB
    private RoomTypeControllerLocal roomTypeControllerLocal;
    @EJB
    private ReservationControllerLocal reservationControllerLocal;
    @EJB
    private PartnerControllerLocal partnerControllerLocal;
    
    
    
    

    
    @WebMethod(operationName = "partnerLogin") 
    public PartnerEntity partnerLogin(@WebParam(name ="email") String email, @WebParam(name = "password") String password) {
        PartnerEntity partner;
        try{
            partner = partnerControllerLocal.retrievePartnerByEmail(email);
            if(!partner.getPassword().equals(password)) {
                System.out.println("Incorrect password.");
                return null;
            }else {
                return partner;
            }
        } catch (PartnerNotFoundException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }
    
    @WebMethod(operationName = "partnerSearchRoom")
    public void partnerSearchRoom(@WebParam(name = "checkInDate") Date checkInDate, @WebParam(name = "checkOutDate") Date checkOutDate) {
        
        //use this if cannot pass in date
        /*
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        Date checkInDateF = null;
        Date checkOutDateF = null;
        try {
            checkInDateF = dateFormat.parse(checkInDate);
            checkOutDateF = dateFormat.parse(checkOutDate);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }*/
        
        List<RoomTypeEntity> availRoomTypes = getAvailableRoomTypes(checkInDate);
        
        
    }
    
    public List<RoomTypeEntity> getAvailableRoomTypes(Date checkInDate) {
        List<RoomTypeEntity> availRoomTypes = new ArrayList<>();
        List<RoomTypeEntity> onlineRoomTypes = roomTypeControllerLocal.retrieveRoomTypesByRateType(RateType.NORMAL);
        for(RoomTypeEntity roomType : onlineRoomTypes) {
            if(roomControllerLocal.checkAvailabilityOfRoomByRoomTypeId(roomType.getRoomTypeId(), checkInDate)) {
                availRoomTypes.add(roomType);
            }
        }   
        return availRoomTypes;
        
    }
    
    @WebMethod(operationName = "partnerReserveRoom")
    public void partnerReserveRoom() {
        
    }
    
    @WebMethod(operationName = "viewPartnerReservationDetails")
    public void viewPartnerReservationDetails() {
        
    }
    
    @WebMethod(operationName = "viewAllPartnerReservations")
    public void viewAllPartnerReservations() {
        
    }

    
}
