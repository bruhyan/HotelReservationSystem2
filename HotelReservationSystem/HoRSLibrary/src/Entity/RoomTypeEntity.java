/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import util.enumeration.AmenitiesType;

/**
 *
 * @author mdk12
 */
@Entity
public class RoomTypeEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomTypeId;
    @OneToMany(mappedBy="roomType")
    private List<RoomEntity> roomList;
    @ManyToMany
    private List<RoomRatesEntity> roomRateList;
    @Column(length = 256, nullable =false)
    private String roomTypeName;
    @Column(length = 256, nullable =false)
    private String description;
    @Column(nullable = false)
    private Integer size;
    @Column(length = 256)
    private String bed;
    @Column(length = 256)
    private String amenities; //amenities using String for now, could think about enums or smt next time
    @Column(length = 2)
    private Integer capacity;
    @Column(nullable = false)
    private boolean isDisabled;
    @Column(nullable = false)
    private Integer ranking;

    public RoomTypeEntity() {
        this.roomList = new ArrayList<>();
        this.roomRateList = new ArrayList<>();
    }

    public RoomTypeEntity(String roomName, String description, Integer size, String bed, String amenities, Integer capacity, Integer ranking) {
        this();
        this.roomTypeName = roomName;
        this.description = description;
        this.size = size;
        this.bed = bed;
        this.amenities = amenities;
        this.capacity = capacity;
        this.isDisabled = false;
        this.ranking = ranking;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    
    public void addRoomRate(RoomRatesEntity roomRate){
            this.roomRateList.add(roomRate);
    }
    public boolean isIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }
    
    

    public List<RoomEntity> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<RoomEntity> roomList) {
        this.roomList = roomList;
    }

    public List<RoomRatesEntity> getRoomRateList() {
        return roomRateList;
    }

    public void setRoomRateList(List<RoomRatesEntity> roomRateList) {
        this.roomRateList = roomRateList;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getBed() {
        return bed;
    }

    public void setBed(String bed) {
        this.bed = bed;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
    
    
    
    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomTypeId != null ? roomTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomTypeId fields are not set
        if (!(object instanceof RoomTypeEntity)) {
            return false;
        }
        RoomTypeEntity other = (RoomTypeEntity) object;
        if ((this.roomTypeId == null && other.roomTypeId != null) || (this.roomTypeId != null && !this.roomTypeId.equals(other.roomTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.RoomTypeEntity[ id=" + roomTypeId + " ]";
    }
    
}
