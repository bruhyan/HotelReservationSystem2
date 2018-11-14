/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Bryan
 */
@Entity
public class PartnerEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partnerId;
    @Column(length = 256, unique = true, nullable = false)
    private String email;
    @Column(length = 32, nullable = false)
    private String password;
    @Column(length = 256, nullable = false)
    private String name;
    @Column(length = 8, nullable = false, unique = true)
    private String contactNumber;
    @Column(nullable = false)
    private Date createdAt;
    @OneToMany(mappedBy="partner")
    private List<CustomerEntity> customersList;

    public PartnerEntity() {
        this.customersList = new ArrayList<>();
    }

    public PartnerEntity(String email, String password, String name, String contactNumber, Date createdAt) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.contactNumber = contactNumber;
        this.createdAt = createdAt;
    }
    
    

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getPartnerId() != null ? getPartnerId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the partnerId fields are not set
        if (!(object instanceof PartnerEntity)) {
            return false;
        }
        PartnerEntity other = (PartnerEntity) object;
        if ((this.getPartnerId() == null && other.getPartnerId() != null) || (this.getPartnerId() != null && !this.partnerId.equals(other.partnerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.PartnerEntity[ id=" + getPartnerId() + " ]";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<CustomerEntity> getCustomersList() {
        return customersList;
    }

    public void setCustomersList(List<CustomerEntity> customersList) {
        this.customersList = customersList;
    }
    
}
