package Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class RoomAllocationException implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomAllocationExceptionId;
    private List<String> exceptions;
    private Date date;
    

    public RoomAllocationException() {
        this.exceptions = new ArrayList<>();
        date = new Date(); //Today
    }

    public RoomAllocationException(List<String> exceptions) {
        this();
        this.exceptions = exceptions;
    }

    public List<String> getExceptions() {
        return exceptions;
    }

    public void setExceptions(List<String> exceptions) {
        this.exceptions = exceptions;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    


    public Long getRoomAllocationExceptionId() {
        return roomAllocationExceptionId;
    }

    public void setRoomAllocationExceptionId(Long roomAllocationExceptionId) {
        this.roomAllocationExceptionId = roomAllocationExceptionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomAllocationExceptionId != null ? roomAllocationExceptionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomAllocationExceptionId fields are not set
        if (!(object instanceof RoomAllocationException)) {
            return false;
        }
        RoomAllocationException other = (RoomAllocationException) object;
        if ((this.roomAllocationExceptionId == null && other.roomAllocationExceptionId != null) || (this.roomAllocationExceptionId != null && !this.roomAllocationExceptionId.equals(other.roomAllocationExceptionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.RoomAllocationException[ id=" + roomAllocationExceptionId + " ]";
    }
    
}
