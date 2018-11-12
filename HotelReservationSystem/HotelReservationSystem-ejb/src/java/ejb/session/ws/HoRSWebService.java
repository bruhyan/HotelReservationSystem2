/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

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
    public void partnerLogin(@WebParam(name ="email") String email, @WebParam(name = "password") String password) {
        
    }

    
}
