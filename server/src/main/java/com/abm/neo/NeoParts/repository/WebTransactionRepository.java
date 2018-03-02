package com.abm.neo.NeoParts.repository;

import com.abm.neo.NeoParts.entity.WebTransactionDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

@Transactional
public interface WebTransactionRepository extends JpaRepository<WebTransactionDao, Integer> {

    WebTransactionDao findOneByCustomerPhoneno(String phoneNo);
}
