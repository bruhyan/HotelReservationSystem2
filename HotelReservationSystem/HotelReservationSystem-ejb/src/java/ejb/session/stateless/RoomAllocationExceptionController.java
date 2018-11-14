/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.RoomAllocationException;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author mdk12
 */
@Stateless
@Remote(RoomAllocationExceptionControllerRemote.class)
@Local(RoomAllocationExceptionControllerLocal.class)
public class RoomAllocationExceptionController implements RoomAllocationExceptionControllerRemote, RoomAllocationExceptionControllerLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    public RoomAllocationException saveException(RoomAllocationException exception){
        
        em.persist(exception);
        em.flush();
        
        
        
        return em.find(RoomAllocationException.class, exception.getRoomAllocationExceptionId());
        
    }
    
    public List<RoomAllocationException> retrieveTodayException(){
        //basically latest one.
        Query query;
        query = em.createQuery("SELECT r FROM RoomAllocationException  r ORDER BY r.roomAllocationExceptionId DESC");
        return query.getResultList();
    }
    
}
