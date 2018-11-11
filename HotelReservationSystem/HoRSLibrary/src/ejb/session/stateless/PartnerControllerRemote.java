/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.PartnerEntity;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Bryan
 */

public interface PartnerControllerRemote {

    public PartnerEntity createPartnerEntity(PartnerEntity partner);

    public List<PartnerEntity> retrieveAllPartner();
    
}
