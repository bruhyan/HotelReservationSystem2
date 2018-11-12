/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author mdk12
 */
@Entity
public class BookingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;
    @OneToOne
    private RoomEntity room;
    @OneToOne
    private RoomTypeEntity roomType;
    @ManyToOne //Owned by reservation
    private ReservationEntity reservation;

    public BookingEntity() {
    }

    //for reservations, to allocate room at 2am on current day.
    //use this for walk in if check in date is in the future
    public BookingEntity(RoomTypeEntity roomType, ReservationEntity reservation){
        this();
        this.roomType = roomType;
        this.reservation = reservation;
    }
    
    //for walk in but allocate room immediately
    public BookingEntity(RoomEntity room, ReservationEntity reservation) {
        this();
        this.room = room;
        this.roomType = room.getRoomType();
        this.reservation = reservation;
    }
    

    public RoomTypeEntity getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomTypeEntity roomType) {
        this.roomType = roomType;
    }

    
    
    public RoomEntity getRoom() {
        return room;
    }

    public void setRoom(RoomEntity room) {
        this.room = room;
    }

    public ReservationEntity getReservation() {
        return reservation;
    }

    public void setReservation(ReservationEntity reservation) {
        this.reservation = reservation;
    }
    
    
    
    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bookingId != null ? bookingId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the bookingId fields are not set
        if (!(object instanceof BookingEntity)) {
            return false;
        }
        BookingEntity other = (BookingEntity) object;
        if ((this.bookingId == null && other.bookingId != null) || (this.bookingId != null && !this.bookingId.equals(other.bookingId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.BookingEntity[ id=" + bookingId + " ]";
    }
    
}
