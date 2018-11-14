/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.TransactionEntity;
import javax.ejb.Local;

/**
 *
 * @author Bryan
 */
@Local
public interface TransactionControllerLocal {
    public TransactionEntity retrieveTransactionById(Long transactionId);

    public TransactionEntity payTransaction(Long transactionId);

    public TransactionEntity createNewTransaction(TransactionEntity transaction);
}
