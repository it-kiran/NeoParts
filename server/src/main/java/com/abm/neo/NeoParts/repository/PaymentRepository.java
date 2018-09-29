package com.abm.pos.ABMPos.repository;

import com.abm.pos.ABMPos.dao.CategoryDao;
import com.abm.pos.ABMPos.dao.PaymentDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apatel2 on 9/28/17.
 */

@Transactional
public interface PaymentRepository extends JpaRepository<PaymentDao, Integer> {

    List<PaymentDao> findAll();


    List<PaymentDao> findAllByTransactionComId(int transactionCompId);

//    @Query("SELECT SUM(cash), sum(credit), SUM(debit), SUM(checkAmount), SUM(storeCredit), SUM(loyalty) FROM PaymentDao WHERE date BETWEEN ?1 AND ?2 AND (status = 'Complete' OR status = 'Return' OR status = 'Pending') ")
//    List<Object[]> sumOfAllPayments(String startDate, String endDate);
//
//    @Query("SELECT SUM(cash), sum(credit), SUM(debit), SUM(checkAmount) FROM PaymentDao WHERE date BETWEEN ?1 AND ?2 AND status = 'Pending' ")
//    List<Object[]> sumOfPendingPayments(String startDate, String endDate);

//    @Modifying
//    @Query(value = "INSERT INTO transaction_payment (transaction_com_id, status, date, cash, credit, debit, check_amount, store_credit, loyalty, layby, change_for_cash, credit_card_last4) VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12)", nativeQuery = true)
//    void insertPaymentDetail(int transactionComId, String status, String date, double cash, double credit, double debit, double checkAmount, double storeCredit, double loyalty, double layby, double changeForCash, double creditCardLast4);

    @Query(value = "SELECT * FROM transaction_payment where transaction_com_id = ?1", nativeQuery = true)
    List<Object[]> getPaymentDetailsByTransactionId(int id);

    @Modifying
    @Query(value = "DELETE FROM transaction_payment where transaction_com_id = ?1", nativeQuery = true)
    void deletePaymentDetails(int transactionComId);

    @Query(value = "SELECT p.transaction_payment_id, p.transaction_com_id, p.status,p.date, p.type, p.amount, p.note,\n" +
            "t.total_amount, t.customer_phoneno, t.customer_first_last_name,t.transaction_balance, p.username from transaction_payment p\n" +
            "INNER JOIN transaction t on t.transaction_com_id = p.transaction_com_id\n" +
            "WHERE p.date BETWEEN ?1 AND ?2 " +
            "ORDER BY p.date DESC", nativeQuery = true)
    List<Object[]> getPaymentHistory(String startDate, String endDate);

}
