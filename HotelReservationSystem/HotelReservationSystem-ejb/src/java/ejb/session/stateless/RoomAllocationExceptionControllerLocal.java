
package ejb.session.stateless;

import Entity.RoomAllocationException;


public interface RoomAllocationExceptionControllerLocal {
    public RoomAllocationException saveException(RoomAllocationException exception);
}
