package com.abm.neo.NeoParts.entity;

import javax.persistence.*;

/**
 * Created by apatel2 on 5/16/17.
 */
@Entity
@Table(name = "model")
public class ModelDao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int modelId;
    private String name;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }
}
