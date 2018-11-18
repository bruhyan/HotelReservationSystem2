
package ejb.session.stateless;

import Entity.TransactionEntity;
import java.util.Date;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@Local(TransactionControllerLocal.class)
@Remote(TransactionControllerRemote.class)
public class TransactionController implements TransactionControllerRemote, TransactionControllerLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public TransactionEntity retrieveTransactionById(Long transactionId) {
       TransactionEntity transaction = em.find(TransactionEntity.class, transactionId);
       return transaction;
    }
    
    @Override
    public TransactionEntity createNewTransaction(TransactionEntity transaction) {
        em.persist(transaction);
        em.flush();
        return transaction;
    }
    
    @Override
    public TransactionEntity payTransaction(Long transactionId) {
        TransactionEntity transaction = em.find(TransactionEntity.class, transactionId);
        transaction.setDatePaid(new Date());
        transaction.setIsPaid(true);
        return transaction;
    }

    
}
