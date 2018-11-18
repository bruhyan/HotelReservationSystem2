
package Entity;

import javax.persistence.Entity;

@Entity
public class SalesManager extends EmployeeEntity {

    public SalesManager() {
    }

    public SalesManager(String name, String contactNumber, String email, String password, String address) {
        super(name, contactNumber, email, password, address);
    } 
}
