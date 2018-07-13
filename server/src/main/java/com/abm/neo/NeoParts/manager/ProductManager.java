package com.abm.neo.NeoParts.manager;

import com.abm.neo.NeoParts.dto.ProductEcomerceDto;
import com.abm.neo.NeoParts.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    public List<ProductEcomerceDto> getEcommerceProductsByModel(int modelId) {

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
//                productEcomerceDto.setRetail(Double.parseDouble(j[2].toString()));

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

                productEcomerceDto.setProductId(Integer.parseInt(j[0].toString()));
                productEcomerceDto.setProductNo(j[1].toString());
                productEcomerceDto.setDescription(j[2].toString());
                productEcomerceDto.setCategoryId(j[3].toString());
                productEcomerceDto.setBrandId(j[4].toString());
                productEcomerceDto.setVendorId(j[5].toString());
                if(null != j[6]){
                    productEcomerceDto.setModelId(j[6].toString());

                }
                if(null != j[7]) {

                    productEcomerceDto.setImage((byte[]) j[7]);

                    // This is helpful to send null on ui so i can show no image found image, otherwise its sending [] this ui and only description is showing on product grid.
                    if(productEcomerceDto.getImage().length < 1)
                    {
                        productEcomerceDto.setImage(null);
                    }
                }

                productEcomerceDto.setTier1(Double.parseDouble(j[8].toString()));
                productEcomerceDto.setTier2(Double.parseDouble(j[9].toString()));
                productEcomerceDto.setTier3(Double.parseDouble(j[10].toString()));
                productEcomerceDto.setQuantity(Integer.parseInt(j[11].toString()));



                productEcomerceDtoList.add(productEcomerceDto);
            }
        }

        return productEcomerceDtoList;
    }

        public void insertProductImage(String productNo, MultipartFile image_file) throws IOException {

            LobHandler lobHandler = new DefaultLobHandler();

            //Here getting image as MultipartFile and then getting input stream of the file and then getting the size of the file.
            SqlLobValue lobValue = new SqlLobValue(image_file.getInputStream(), (int) image_file.getSize(), lobHandler);

            int a = jdbcTemplate.update("UPDATE product_image SET image = ? WHERE product_no = ?" ,new Object[]{lobValue, productNo}, new int[] {Types.BLOB, Types.LONGNVARCHAR} );
//                jdbcTemplate.update("INSERT INTO  product_image ('product_no', 'image') VALUE (?1,?2)" , lobValue, productNo);


            System.out.println(1);


        }

    public List<ProductEcomerceDto> getAllProduct() {

        return  setEcomerceDto(productRepository.getAllActiveProducts(new PageRequest(0, 1)));
    }

    public List<ProductEcomerceDto> getFeaturedProducts() {
        return setEcomerceDto(productRepository.getAllFeaturedProducts());
    }

    public List<ProductEcomerceDto> getNewProducts() {
        return setEcomerceDto(productRepository.getAllNewProducts());

    }
}
