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

/**
 *
 * @author Bryan
 */
@Entity
public class GuestRelationOfficer extends EmployeeEntity {

    public GuestRelationOfficer() {
    }

    public GuestRelationOfficer(String name, String contactNumber, String email, String password, String address) {
        super(name, contactNumber, email, password, address);
    }
    

}
