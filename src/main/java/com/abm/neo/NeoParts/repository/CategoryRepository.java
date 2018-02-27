package com.abm.neo.NeoParts.repository;

import com.abm.neo.NeoParts.entity.CategoryDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by apatel2 on 5/16/17.
 */

@Transactional
public interface CategoryRepository extends JpaRepository<CategoryDao, Integer> {

    List<CategoryDao> findAll();
}
