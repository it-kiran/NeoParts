package com.abm.neo.NeoParts.repository;


import com.abm.neo.NeoParts.entity.WebTransactionDao;
import com.abm.neo.NeoParts.entity.WebTransactionLineItemDao;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by apatel2 on 5/18/17.
 */

@Transactional
public interface WebTransactionLineItemRepository extends JpaRepository<WebTransactionLineItemDao, Integer> {

    List<WebTransactionLineItemDao> findAll();

    List<WebTransactionLineItemDao> findAllByCustomerPhoneNo(String PhonoeNo);


}
