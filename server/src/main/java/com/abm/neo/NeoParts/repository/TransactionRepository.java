package com.abm.neo.NeoParts.repository;

import com.abm.neo.NeoParts.entity.TransactionDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by apatel2 on 5/18/17.
 */

@Transactional
public interface TransactionRepository extends JpaRepository<TransactionDao, Integer> {

    List<TransactionDao> findAll();

    @Query("SELECT SUM(transactionBalance) from TransactionDao WHERE status = 'Pending' AND customerPhoneno = ?1 ")
    Double getTransactionDueAmountByCustomer(String phoneNo);
}
