package com.abm.neo.NeoParts.repository;

import com.abm.neo.NeoParts.dto.CustomerDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by apatel2 on 5/17/17.
 */

@Transactional
public interface CustomerRepository extends JpaRepository<CustomerDao, String> {

    List<CustomerDao> findAll();

    CustomerDao findByPhoneNo(String phoneNo);

    CustomerDao findByEmail(String email);

    CustomerDao findByEmailAndPassword(String email, String password);

    @Modifying
    @Query(value = "UPDATE customer set password = ?1 where email = ?2", nativeQuery = true)
    void changePassword(String password, String email);

}
