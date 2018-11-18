
package ejb.session.stateless;

import Entity.TransactionEntity;

public interface TransactionControllerRemote {

    public TransactionEntity retrieveTransactionById(Long transactionId);

    public TransactionEntity payTransaction(Long transactionId);

    public TransactionEntity createNewTransaction(TransactionEntity transaction);
    
}
