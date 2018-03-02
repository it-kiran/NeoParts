package com.abm.neo.NeoParts.manager;


import com.abm.neo.NeoParts.dto.MenuDto;
import com.abm.neo.NeoParts.dto.WebBrandDto;
import com.abm.neo.NeoParts.entity.BrandDao;
import com.abm.neo.NeoParts.entity.CategoryDao;
import com.abm.neo.NeoParts.entity.ModelDao;
import com.abm.neo.NeoParts.repository.BrandRepository;
import com.abm.neo.NeoParts.repository.CategoryRepository;
import com.abm.neo.NeoParts.repository.ModelRepository;
import com.abm.neo.NeoParts.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WebMenuManager {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    private ModelRepository modelRepository;
    

    public MenuDto getWebMenu() {

        MenuDto menuDto = new MenuDto();

        List<CategoryDao> categoryDtoList;
        List<BrandDao> brandDtoList;
        List<ModelDao> modelDtoList;

        List<WebBrandDto> webBrandDtoList = new ArrayList<WebBrandDto>();

        categoryDtoList = categoryRepository.findAll();
        brandDtoList = brandRepository.findAll();

        for(int i = 0; i<brandDtoList.size(); i++)
        {
            WebBrandDto webBrandDto = new WebBrandDto();
            webBrandDto.setBrandId(brandDtoList.get(i).getBrandId());
            webBrandDto.setBrandName(brandDtoList.get(i).getName());

            //Calling method to get all models details for particular brand id.
            modelDtoList = getModelListForBrand(brandDtoList.get(i).getBrandId());
            webBrandDto.setModelDtoList(modelDtoList);
            webBrandDtoList.add(webBrandDto);
            webBrandDtoList.set(i, webBrandDto);
        }
        menuDto.setWebBrandDtoList(webBrandDtoList);
        menuDto.setCategoryDtoList(categoryDtoList);

        System.out.println("Send Web Menu Details Successfully");

        return menuDto;
    }
    private List<ModelDao> getModelListForBrand(int brandId) {

        List<ModelDao> modelDtoListGlobal = new ArrayList<ModelDao>();
        List<Integer> modelNos;
        modelNos =  productRepository.getModelDetailsForBrand(brandId);
        for(int i = 0;i<modelNos.size(); i++)
        {
            ModelDao modelDao = new ModelDao();
            List<ModelDao> modelDtoList;
            modelDtoList = modelRepository.findAllByModelId(modelNos.get(i));

            if(modelDtoList.size() != 0)
            {
                modelDao.setModelId(modelDtoList.get(0).getModelId());
                modelDao.setName(modelDtoList.get(0).getName());

                modelDtoListGlobal.add(modelDao);
                modelDtoListGlobal.set(i,modelDao);
            }
        }
        return modelDtoListGlobal;
    }
}
