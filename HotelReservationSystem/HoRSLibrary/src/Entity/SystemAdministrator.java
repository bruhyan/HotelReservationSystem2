
package Entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class SystemAdministrator  extends EmployeeEntity {

    public SystemAdministrator() {
    }
    
    public SystemAdministrator(String name, String contactNumber, String email, String password, String address) {
        super(name, contactNumber, email, password, address);
    }

    
    
}
