package com.abm.neo.NeoParts.dto;

import com.abm.neo.NeoParts.entity.ModelDao;

import java.util.List;

/**
 * Created by asp5045 on 11/17/16.
 */
public class WebBrandDto {

    private int brandId;
    private String brandName;
    private byte[] brandImage;
    private List<ModelDao> modelDtoList;

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public byte[] getBrandImage() {
        return brandImage;
    }

    public void setBrandImage(byte[] brandImage) {
        this.brandImage = brandImage;
    }

    public List<ModelDao> getModelDtoList() {
        return modelDtoList;
    }

    public void setModelDtoList(List<ModelDao> modelDtoList) {
        this.modelDtoList = modelDtoList;
    }
}
