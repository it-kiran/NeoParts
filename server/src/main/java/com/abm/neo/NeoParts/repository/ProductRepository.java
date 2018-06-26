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

    @Query(value = "SELECT\n" +
            "p.product_no,\n" +
            "p.description,\n" +
            "p.category_id,\n" +
            "p.brand_id,\n" +
            "p.vendor_id,\n" +
            "p.model_id,\n" +
            "im.image,\n" +
            "i.tier1, \n" +
            "i.tier2,\n" +
            "i.tier3,\n" +
            "sum(i.quantity)\n" +
            "FROM product p\n" +
            "LEFT JOIN product_image im \n" +
            "ON im.product_no = p.product_no\n" +
            "left outer join product_inventory i\n" +
            "on i.product_no = p.product_no\n" +
            "WHERE p.active = 1 \n" +
            "AND p.ecommerce = 1 \n" +
            "AND (p.description like '%' ?1 '%' or p.product_no=?1)\n" +
            "group by p.product_no,\n" +
            "p.description,\n" +
            "p.category_id,\n" +
            "p.brand_id,\n" +
            "p.vendor_id,\n" +
            "p.model_id,\n" +
            "im.image,\n" +
            "i.tier1, \n" +
            "i.tier2,\n" +
            "i.tier3;", nativeQuery = true)
    List<Object[]> getProductForSearch(String searchInput);

    List<ProductDao> findAll();

    ProductDao findOneByProductNo(String productNo);

    @Query("SELECT p FROM ProductDao p WHERE p.active = true ORDER BY p.productNo ASC")
        //@Cacheable(value = "products",  key = "#root.target.KEY")
    List<ProductDao> getAllActiveProduct();

    @Query(value = "SELECT distinct model_id FROM product WHERE brand_id = ? AND MODEL_ID <> 'NULL' ORDER BY model_id DESC", nativeQuery = true)
    List<Integer> getModelDetailsForBrand(int brandId);

    @Query("SELECT p.productNo, p.description FROM ProductDao p WHERE p.active = true AND p.ecommerce = true ORDER BY p.productNo ASC")
    List<Object[]> getProductForSearchBar();

    @Query(value = "SELECT p.product_id,p.product_no,p.description,p.category_id,p.brand_id,p.vendor_id,p.model_id,p.cost,p.retail,p.quantity, i.image FROM product p\n" +
            "LEFT JOIN product_image i ON i.product_no = p.product_no\n" +
            "WHERE active = 1 AND ecommerce = 1 AND p.category_id = ?1", nativeQuery = true)
    List<Object[]> getEcommerceProductsByCategory(int category_id);

    @Query(value = "SELECT\n" +
            "p.product_id,\n" +
            "p.product_no,\n" +
            "p.description,\n" +
            "p.category_id,\n" +
            "p.brand_id,\n" +
            "p.vendor_id,\n" +
            "p.model_id,\n" +
            "im.image,\n" +
            "i.tier1, \n" +
            "i.tier2,\n" +
            "i.tier3,\n" +
            "sum(i.quantity)\n" +
            "FROM product p\n" +
            "LEFT JOIN product_image im \n" +
            "ON im.product_no = p.product_no\n" +
            "left outer join product_inventory i\n" +
            "on i.product_no = p.product_no\n" +
            "WHERE active = 1 \n" +
            "AND ecommerce = 1 \n" +
            "AND p.model_id = ?1\n" +
            "group by p.product_no,\n" +
            "p.description,\n" +
            "p.category_id,\n" +
            "p.brand_id,\n" +
            "p.vendor_id,\n" +
            "p.model_id,\n" +
            "im.image,\n" +
            "i.tier1, \n" +
            "i.tier2,\n" +
            "i.tier3", nativeQuery = true)
    List<Object[]> getEcommerceProductsByBrand(int modelId);

    @Query(value = "SELECT p.product_id,\n" +
            "p.product_no,\n" +
            "p.description,\n" +
            "p.category_id,\n" +
            "p.brand_id,\n" +
            "p.vendor_id,\n" +
            "p.model_id,\n" +
            "im.image,\n" +
            "i.tier1,\n" +
            "i.tier2,\n" +
            "i.tier3,\n" +
            "sum(i.quantity)\n" +
            "FROM product p\n" +
            "LEFT JOIN product_image im\n" +
            "ON im.product_no = p.product_no\n" +
            "inner join product_inventory i\n" +
            "on i.product_no = p.product_no\n" +
            "WHERE active = 1 \n" +
            "AND ecommerce = 1 \n" +
            "group by p.product_no,\n" +
            "p.description,\n" +
            "p.category_id,\n" +
            "p.brand_id,\n" +
            "p.vendor_id,\n" +
            "p.model_id,\n" +
            "im.image,\n" +
            "i.tier1,\n" +
            "i.tier2,\n" +
            "i.tier3", nativeQuery = true)
    List<Object[]> getAllActiveProducts();
}
