/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.PartnerEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.PartnerNotFoundException;

/**
 *
 * @author Bryan
 */
@Stateless
@Local(PartnerControllerLocal.class)
@Remote(PartnerControllerRemote.class)
public class PartnerController implements PartnerControllerRemote, PartnerControllerLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public PartnerEntity createPartnerEntity(PartnerEntity partner) {
        em.persist(partner);
        em.flush();
        return partner;
    }
    
    @Override
    public List<PartnerEntity> retrieveAllPartner() {
        Query query = em.createQuery("SELECT p FROM PartnerEntity p");
        return query.getResultList();
    }
    
    public PartnerEntity retrievePartnerByEmail(String email) throws PartnerNotFoundException {
        Query query = em.createQuery("SELECT p FROM PartnerEntity p WHERE p.email = :inEmail");
        query.setParameter("inEmail", email);
        try {
            PartnerEntity partner = (PartnerEntity)query.getSingleResult();
            return partner;
        }catch (NoResultException ex) {
            throw new PartnerNotFoundException("Partner not found");
        }
    }

    
}
