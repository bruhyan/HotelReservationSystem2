
package ejb.session.stateless;

import Entity.RoomAllocationException;
import java.util.List;


public interface RoomAllocationExceptionControllerRemote {

   public List<RoomAllocationException> retrieveTodayException();
    
}
