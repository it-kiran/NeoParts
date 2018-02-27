package com.abm.neo.NeoParts.repository;

import com.abm.neo.NeoParts.entity.BrandDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by apatel2 on 5/16/17.
 */
public interface BrandRepository extends JpaRepository<BrandDao, Integer> {

    List<BrandDao> findAll();
}
