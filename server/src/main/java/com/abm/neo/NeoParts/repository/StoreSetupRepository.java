package com.abm.neo.NeoParts.repository;

import com.abm.neo.NeoParts.entity.StoreSetupDao;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by apatel2 on 9/26/17.
 */

@Transactional
public interface StoreSetupRepository extends JpaRepository<StoreSetupDao, Integer> {

    List<StoreSetupDao> findAll();


}
