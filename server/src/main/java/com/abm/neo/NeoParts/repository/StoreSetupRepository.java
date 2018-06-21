package com.abm.pos.ABMPos.repository;

import com.abm.pos.ABMPos.dao.StoreSetupDao;
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
