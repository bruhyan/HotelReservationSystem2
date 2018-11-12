/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Bryan
 */
@Entity
public class RoomTypeRanking implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomTypeRankingId;
    private List<RoomTypeEntity> roomTypes;

    public RoomTypeRanking() {
        this.roomTypes = new ArrayList<>();
    }
    
    

    public Long getRoomTypeRankingId() {
        return roomTypeRankingId;
    }

    public void setRoomTypeRankingId(Long roomTypeRankingId) {
        this.roomTypeRankingId = roomTypeRankingId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomTypeRankingId != null ? roomTypeRankingId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomTypeRankingId fields are not set
        if (!(object instanceof RoomTypeRanking)) {
            return false;
        }
        RoomTypeRanking other = (RoomTypeRanking) object;
        if ((this.roomTypeRankingId == null && other.roomTypeRankingId != null) || (this.roomTypeRankingId != null && !this.roomTypeRankingId.equals(other.roomTypeRankingId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.RoomTypeRanking[ id=" + roomTypeRankingId + " ]";
    }

    public List<RoomTypeEntity> getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(List<RoomTypeEntity> roomTypes) {
        this.roomTypes = roomTypes;
    }
    
}
