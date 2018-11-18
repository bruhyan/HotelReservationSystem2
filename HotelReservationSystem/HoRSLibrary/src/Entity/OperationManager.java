package Entity;

import javax.persistence.Entity;

@Entity
public class OperationManager extends EmployeeEntity {

    public OperationManager() {
    }

    public OperationManager(String name, String contactNumber, String email, String password, String address) {
        super(name, contactNumber, email, password, address);
    }
    
}
