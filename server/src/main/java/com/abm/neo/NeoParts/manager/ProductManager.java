package com.abm.neo.NeoParts.manager;

import com.abm.neo.NeoParts.dto.ProductEcomerceDto;
import com.abm.neo.NeoParts.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Lob;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductManager {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ProductEcomerceDto> getProductForSearch(String searchInput) {

        List<Object[]> result = productRepository.getProductForSearch(searchInput);
        return setEcomerceDto(result);
    }

    public List<ProductEcomerceDto> getEcommerceProductsByCategory(int categoryId) {

        List<Object[]> result = productRepository.getEcommerceProductsByCategory(categoryId);
        return setEcomerceDto(result);
    }

    public List<ProductEcomerceDto> getEcommerceProductsByBrand(int modelId) {

        List<Object[]> result = productRepository.getEcommerceProductsByBrand(modelId);
        return setEcomerceDto(result);
    }

    public List<ProductEcomerceDto> getProductDetails() {

        List<Object[]> result = productRepository.getProductForSearchBar();
        List<ProductEcomerceDto> productEcomerceDtoList = new ArrayList<>();

        if(null != result)
        {
            for(Object[] j: result)
            {
                ProductEcomerceDto productEcomerceDto = new ProductEcomerceDto();

                productEcomerceDto.setProductNo(j[0].toString());
                productEcomerceDto.setDescription(j[1].toString());
                productEcomerceDto.setRetail(Double.parseDouble(j[2].toString()));

                productEcomerceDtoList.add(productEcomerceDto);
            }
        }
        return productEcomerceDtoList;
    }

    private List<ProductEcomerceDto>setEcomerceDto(List<Object[]> result){

        List<ProductEcomerceDto> productEcomerceDtoList = new ArrayList<>();

        if(null != result) {
            for (Object[] j : result)
            {
                ProductEcomerceDto productEcomerceDto = new ProductEcomerceDto();

                productEcomerceDto.setProductNo(j[0].toString());
                productEcomerceDto.setDescription(j[1].toString());
                productEcomerceDto.setCategoryId(j[2].toString());
                productEcomerceDto.setBrandId(j[3].toString());
                productEcomerceDto.setVendorId(j[4].toString());
                if(null != j[5]){
                    productEcomerceDto.setModelId(j[5].toString());

                }
                productEcomerceDto.setCost(Double.parseDouble(j[6].toString()));
                productEcomerceDto.setRetail(Double.parseDouble(j[7].toString()));
                productEcomerceDto.setQuantity(Integer.parseInt(j[8].toString()));
                if(null != j[9]) {
                    productEcomerceDto.setImage((byte[]) j[9]);
                }

                productEcomerceDtoList.add(productEcomerceDto);
            }
        }

        return productEcomerceDtoList;
    }

        public void insertProductImage(String productNo, MultipartFile image_file) throws IOException {

            LobHandler lobHandler = new DefaultLobHandler();

            //Here getting image as MultipartFile and then getting input stream of the file and then getting the size of the file.
            SqlLobValue lobValue = new SqlLobValue(image_file.getInputStream(), (int) image_file.getSize(), lobHandler);

            int a = jdbcTemplate.update("UPDATE product_image SET image = ? WHERE product_no = ?" ,new Object[]{lobValue, productNo}, new int[] {Types.BLOB, Types.INTEGER} );
//                jdbcTemplate.update("INSERT INTO  product_image ('product_no', 'image') VALUE (?1,?2)" , lobValue, productNo);


            System.out.println(1);


        }
    }
