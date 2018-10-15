/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import javax.persistence.Entity;

/**
 *
 * @author Bryan
 */
@Entity
public class OperationManager extends EmployeeEntity {

    public OperationManager() {
    }

    public OperationManager(String name, String contactNumber, String email, String password, String address) {
        super(name, contactNumber, email, password, address);
    }
    
}
