
package ejb.session.stateless;

import Entity.TransactionEntity;

public interface TransactionControllerLocal {
    public TransactionEntity retrieveTransactionById(Long transactionId);

    public TransactionEntity payTransaction(Long transactionId);

    public TransactionEntity createNewTransaction(TransactionEntity transaction);
}
