/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author mdk12
 */
@Entity
public class RoomRatesEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomRatesId;
    private String name;
    private BigDecimal ratePerNight;
    private Date validityStart;
    private Date validityEnd;

    public RoomRatesEntity() {
    }

    public RoomRatesEntity(String name, BigDecimal ratePerNight, Date validityStart, Date validityEnd) {
        this();
        this.name = name;
        this.ratePerNight = ratePerNight;
        this.validityStart = validityStart;
        this.validityEnd = validityEnd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getRatePerNight() {
        return ratePerNight;
    }

    public void setRatePerNight(BigDecimal ratePerNight) {
        this.ratePerNight = ratePerNight;
    }

    public Date getValidityStart() {
        return validityStart;
    }

    public void setValidityStart(Date validityStart) {
        this.validityStart = validityStart;
    }

    public Date getValidityEnd() {
        return validityEnd;
    }

    public void setValidityEnd(Date validityEnd) {
        this.validityEnd = validityEnd;
    }
    
    
    

    public Long getRoomRatesId() {
        return roomRatesId;
    }

    public void setRoomRatesId(Long roomRatesId) {
        this.roomRatesId = roomRatesId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomRatesId != null ? roomRatesId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomRatesId fields are not set
        if (!(object instanceof RoomRatesEntity)) {
            return false;
        }
        RoomRatesEntity other = (RoomRatesEntity) object;
        if ((this.roomRatesId == null && other.roomRatesId != null) || (this.roomRatesId != null && !this.roomRatesId.equals(other.roomRatesId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.RoomRatesEntity[ id=" + roomRatesId + " ]";
    }
    
}