/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.TransactionEntity;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Bryan
 */
@Stateless
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
