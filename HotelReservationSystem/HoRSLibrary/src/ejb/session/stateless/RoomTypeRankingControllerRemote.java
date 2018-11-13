/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.RoomTypeRanking;


public interface RoomTypeRankingControllerRemote {
    public RoomTypeRanking getRoomTypeRanking();
    public void setRoomTypeRank(Long roomTypeId, int rank);
}
