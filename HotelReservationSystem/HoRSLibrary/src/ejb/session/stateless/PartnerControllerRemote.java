
package ejb.session.stateless;

import Entity.PartnerEntity;
import java.util.List;


public interface PartnerControllerRemote {

    public PartnerEntity createPartnerEntity(PartnerEntity partner);

    public List<PartnerEntity> retrieveAllPartner();
    
}
