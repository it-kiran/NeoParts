package com.abm.neo.NeoParts.repository;


import com.abm.neo.NeoParts.entity.WebTransactionDao;
import com.abm.neo.NeoParts.entity.WebTransactionLineItemDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by apatel2 on 5/18/17.
 */

@Transactional
public interface WebTransactionLineItemRepository extends JpaRepository<WebTransactionLineItemDao, Integer> {

    List<WebTransactionLineItemDao> findAll();

    List<WebTransactionLineItemDao> findAllByCustomerPhoneNo(String PhonoeNo);

    @Modifying
    @Query(value = "DELETE FROM web_transaction_line_item where customer_phone_no = ?1 AND transaction_line_item_Id = ?2", nativeQuery = true)
    void deleteLineItemByCustomer(String phoneNo, int transactionLineItemId);


}
