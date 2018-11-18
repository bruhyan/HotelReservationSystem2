package Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity
public class EmployeeEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long employeeId;
    @Column(length = 256, nullable = false)
    private String name;
    @Column(length = 8, nullable = false, unique = true)
    private String contactNumber;
    @Column(length = 256, nullable = false, unique = true)
    private String email;
    @Column(length = 32, nullable = false)
    private String password;
    @Column(length = 256, nullable = false)
    private String address;
    @OneToMany(mappedBy="employee")
    private List<TransactionEntity> transactionList;

    public EmployeeEntity() {
        transactionList = new ArrayList<>();
    }

    public EmployeeEntity(String name, String contactNumber, String email, String password, String address) {
        this();
        this.name = name;
        this.contactNumber = contactNumber;
        this.email = email;
        this.password = password;
        this.address = address;
    }

    public List<TransactionEntity> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<TransactionEntity> transactionList) {
        this.transactionList = transactionList;
    }
    
    public void addTransaction(TransactionEntity transaction){
        this.transactionList.add(transaction);
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeId != null ? employeeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the employeeId fields are not set
        if (!(object instanceof EmployeeEntity)) {
            return false;
        }
        EmployeeEntity other = (EmployeeEntity) object;
        if ((this.employeeId == null && other.employeeId != null) || (this.employeeId != null && !this.employeeId.equals(other.employeeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Employee[ id=" + employeeId + " ]";
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
}
