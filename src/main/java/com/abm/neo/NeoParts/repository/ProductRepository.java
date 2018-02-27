package com.abm.neo.NeoParts.repository;

import com.abm.neo.NeoParts.entity.ProductDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by apatel2 on 5/16/17.
 */

@Transactional
public interface ProductRepository extends JpaRepository<ProductDao, String> {

    public static final String KEY = "products";



    List<ProductDao> findAll();

    @Query(value = "SELECT distinct model_id FROM product WHERE brand_id = ? AND MODEL_ID <> 'NULL' ORDER BY model_id DESC", nativeQuery = true)
    List<Integer> getModelDetailsForBrand(int brandId);



}
