/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.RoomAllocationException;


public interface RoomAllocationExceptionControllerLocal {
    public RoomAllocationException saveException(RoomAllocationException exception);
}
