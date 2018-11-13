/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import Entity.PartnerEntity;
import ejb.session.stateless.BookingControllerLocal;
import ejb.session.stateless.CustomerControllerLocal;
import ejb.session.stateless.PartnerControllerLocal;
import ejb.session.stateless.ReservationControllerLocal;
import ejb.session.stateless.RoomRateControllerLocal;
import ejb.session.stateless.RoomTypeControllerLocal;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.exception.PartnerNotFoundException;

/**
 *
 * @author Bryan
 */
@WebService(serviceName = "HoRSWebService")
@Stateless

public class HoRSWebService {

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
    public void partnerSearchRoom() {
        
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